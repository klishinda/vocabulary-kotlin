package gq.learningEnglish.common

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.JdbcUtils
import java.sql.ResultSet
import kotlin.reflect.*
import kotlin.reflect.KTypeProjection.Companion.STAR
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType

class KotlinRowMapper<T : Any>(
        private val mappedClass: KClass<T>,
        private val jsonMapper: ObjectMapper = jacksonObjectMapper()
) : RowMapper<T>, Logger {

    private var javaMode: Boolean = false
    private val dbFields = HashMap<String, Int>()
    private val mappedFields = HashMap<KParameter, Any?>()

    constructor(mappedClass: KClass<T>, javaMode: Boolean) : this(mappedClass) {
        this.javaMode = javaMode
    }

    override fun mapRow(rs: ResultSet, rowNum: Int): T {
        return when {
            mappedClass.isData -> mapDataClass(rs)
            javaMode -> BeanPropertyRowMapper(mappedClass.java).mapRow(rs, rowNum)
            else -> mapSimpleClass(rs)
        }
    }

    private fun mapDataClass(rs: ResultSet): T {
        processDbFields(rs)
        mappedClass.primaryConstructor!!.parameters.forEach { mapConstructorFields(rs, it) }
        return mappedClass.primaryConstructor!!.callBy(mappedFields)
    }

    private fun mapConstructorFields(rs: ResultSet, parameter: KParameter) {
        if (!parameter.type.isMarkedNullable) {
            log.debug(
                    "Warning, field ${parameter.name} of ${mappedClass.simpleName} " +
                            "is not nullable, that may cause IllegalArgumentException!"
            )
        }
        val index = getDbFieldIndex(parameter.name!!)
        mappedFields[parameter] = rs.getValue(index, parameter, jsonMapper)
    }

    private fun mapSimpleClass(rs: ResultSet): T {
        processDbFields(rs)
        val instance = mappedClass.constructors.first { it.parameters.isEmpty() }.call()
        setMutableProperties(rs, instance)
        return instance
    }

    private fun setMutableProperties(rs: ResultSet, instance: T) {
        instance::class.memberProperties
                .filter { it.visibility == KVisibility.PUBLIC }
                .filterIsInstance<KMutableProperty<*>>()
                .forEach {
                    val index = getDbFieldIndex(it.name)
                    it.setter.call(instance, rs.getValue(index, it))
                }
    }

    private fun processDbFields(rs: ResultSet) {
        val rsMetaData = rs.metaData
        val columnCount = rsMetaData.columnCount
        for (i in 1..columnCount) {
            dbFields[rsMetaData.getColumnName(i).toLowerCase()] = i
        }
    }

    private fun getDbFieldIndex(name: String): Int? {
        val parameterName = dbFields[getParameterName(name)]
        val underscoreName = dbFields[underscoreName(name)]
        return when {
            parameterName != null -> parameterName
            underscoreName != null -> underscoreName
            else -> null
        }
    }

    private fun underscoreName(name: String): String {
        val result = StringBuilder()
        result.append(name.substring(0, 1).toLowerCase())
        for (i in 1 until name.length) {
            val s = name.substring(i, i + 1)
            val slc = s.toLowerCase()
            if (s != slc) {
                result.append("_").append(slc)
            } else {
                result.append(s)
            }
        }
        return result.toString()
    }

    private fun getParameterName(name: String): String {
        val field = mappedClass.declaredMemberProperties.find { it.name == name }
        val dbFieldAnnotationValue = field?.findAnnotation<DbField>()?.value
        return when {
            dbFieldAnnotationValue != null -> dbFieldAnnotationValue.toLowerCase()
            else -> name.toLowerCase()
        }
    }

    private companion object {

        fun ResultSet.getValue(index: Int?, parameter: KParameter, jsonMapper: ObjectMapper): Any? {
            return when {
                index == null -> null
                parameter.isEnum -> parameter.getEnumValue(getResultSetValue(index, parameter))
                columnTypeIsTextArray(index) -> sqlArrayToList(index)
                columnTypeIsJson(index) && !parameter.isSerializable -> jsonToObject(index, parameter, jsonMapper)
                else -> getResultSetValue(index, parameter)
            }
        }

        fun ResultSet.getValue(index: Int?, property: KMutableProperty<*>): Any? {
            return when (index) {
                null -> null
                else -> getResultSetValue(index, property)
            }
        }

        private fun ResultSet.getResultSetValue(index: Int, parameter: KParameter): Any? {
            return JdbcUtils.getResultSetValue(this, index, parameter.type.javaType as Class<*>)
        }

        private fun ResultSet.getResultSetValue(index: Int, property: KMutableProperty<*>): Any? {
            return JdbcUtils.getResultSetValue(this, index, property.returnType.javaType as Class<*>)
        }

        private fun ResultSet.columnTypeIsJson(index: Int): Boolean {
            return JSON_TYPES.contains(metaData.getColumnTypeName(index))
        }

        private fun ResultSet.columnTypeIsTextArray(index: Int): Boolean {
            return TEXT_ARRAY_TYPES.contains(metaData.getColumnTypeName(index))
        }

        private fun ResultSet.jsonToObject(index: Int, parameter: KParameter, mapper: ObjectMapper): Any? {
            val stringValue: String? = getString(index)
            return when {
                parameter.type.arguments.isNotEmpty() -> toGenericType(stringValue, parameter, mapper)
                else -> mapper.readValue(stringValue, parameter.clazz.java)
            }
        }

        private fun toGenericType(value: String?, parameter: KParameter, mapper: ObjectMapper): Any? {
            if (value == null) {
                return null
            }
            val factory = mapper.typeFactory
            val typeArguments = parameter.type.arguments
            val javaType = parameter.clazz.run {
                when {
                    isSubclassOf(Map::class) -> factory.constructMapType(
                            HashMap::class.java,
                            toJavaTypeRecursively(typeArguments[0], factory),
                            toJavaTypeRecursively(typeArguments[1], factory)
                    )
                    isSubclassOf(List::class) -> factory.constructCollectionType(
                            ArrayList::class.java,
                            toJavaTypeRecursively(typeArguments.first(), factory)
                    )
                    isSubclassOf(Set::class) -> factory.constructCollectionType(
                            HashSet::class.java,
                            toJavaTypeRecursively(typeArguments.first(), factory)
                    )
                    else -> factory.constructParametricType(
                            this::class.java,
                            *typeArguments.map { toJavaTypeRecursively(it, factory) }.toTypedArray()
                    )
                }
            }
            return mapper.readValue(value, javaType)
        }

        private fun toJavaTypeRecursively(projection: KTypeProjection, factory: TypeFactory): JavaType {
            if (projection.type == null) {
                return factory.constructType(toJavaClass(projection))
            }
            projection.type!!.run {
                return when {
                    this.arguments.isEmpty() -> factory.constructType(toJavaClass(projection))
                    this.isSubtypeOf(MAP_TYPE) -> factory.constructMapType(
                            HashMap::class.java,
                            toJavaTypeRecursively(this.arguments[0], factory),
                            toJavaTypeRecursively(this.arguments[1], factory)
                    )
                    this.isSubtypeOf(LIST_TYPE) -> factory.constructCollectionType(
                            ArrayList::class.java,
                            toJavaTypeRecursively(this.arguments.first(), factory)
                    )
                    this.isSubtypeOf(SET_TYPE) -> factory.constructCollectionType(
                            HashSet::class.java,
                            toJavaTypeRecursively(this.arguments.first(), factory)
                    )
                    else -> factory.constructParametricType(
                            toJavaClass(projection),
                            *arguments.map { toJavaTypeRecursively(it, factory) }.toTypedArray()
                    )
                }
            }
        }

        private fun toJavaClass(projection: KTypeProjection): Class<*> {
            val type: KType = projection.type ?: ANY_TYPE
            return (type.classifier as KClass<*>).javaObjectType
        }

        @Suppress("UNCHECKED_CAST")
        private fun ResultSet.sqlArrayToList(index: Int): Any? {
            return (getArray(index)?.array as Array<String>?)?.toList()
        }

        private val KParameter.clazz: KClass<*>
            get() = type.classifier as KClass<*>

        private val KParameter.isEnum: Boolean
            get() = clazz.isSubclassOf(Enum::class)

        private val KParameter.isSerializable: Boolean
            get() = SERIALIZABLE_CLASSES.contains(clazz)

        @Suppress("UNCHECKED_CAST")
        private fun KParameter.getEnumValue(resultValue: Any?): Any? {
            val unknownEnum = type.classifier as KClass<Enum<*>>
            return unknownEnum.java.enumConstants.firstOrNull { it.name == resultValue }
        }

        private val TEXT_ARRAY_TYPES = setOf("_text", "_varchar")
        private val JSON_TYPES = setOf("jsonb", "json")

        private val ANY_TYPE = Any::class.createType()
        private val MAP_TYPE = Map::class.createType(listOf(STAR, STAR))
        private val LIST_TYPE = List::class.createType(listOf(STAR))
        private val SET_TYPE = Set::class.createType(listOf(STAR))
    }
}