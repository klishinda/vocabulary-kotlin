package gq.learningEnglish.common.infrastructure.interfaces

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import gq.learningEnglish.common.infrastructure.annotations.DbField
import gq.learningEnglish.common.infrastructure.SerializationType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

internal val jdbcObjectMapper = jacksonObjectMapper()

interface DbEntity {

    fun getUpdateSql(sqlTemplate: String): String {
        val sql = this.sqlUpdateParams.values.map { "${it.key} = :${it.key}" }.joinToString()
        return sqlTemplate.format(sql)
    }

    fun getSqlParams(objectMapper: ObjectMapper): MapSqlParameterSource {
        return dbProperties { it.getValue(this, objectMapper) }
    }

    fun getUpdateParams(objectMapper: ObjectMapper): MapSqlParameterSource {
        return dbProperties({ it.findAnnotation<DbField>()!!.updatable }, { it.getValue(this, objectMapper) })
    }

    fun getInsertParams(objectMapper: ObjectMapper): MapSqlParameterSource {
        return dbProperties({ it.findAnnotation<DbField>()!!.insertable }, { it.getValue(this, objectMapper) })
    }

    @get:JsonIgnore
    val sqlParams: MapSqlParameterSource
        get() = dbProperties { it.getValue(this) }

    @get:JsonIgnore
    val sqlUpdateParams: MapSqlParameterSource
        get() = dbProperties({ it.findAnnotation<DbField>()!!.updatable }, { it.getValue(this) })

    @get:JsonIgnore
    val sqlInsertParams: MapSqlParameterSource
        get() = dbProperties({ it.findAnnotation<DbField>()!!.insertable }, { it.getValue(this) })

    fun nonNullSqlParamMap(): Map<String, Any> {
        return sqlParams.values.filter { it.value != null }
    }

    private fun dbProperties(
        filterFun: (KProperty1<out Any, *>) -> Boolean = { true },
        getValue: (KProperty1<out Any, *>) -> Any?
    ): MapSqlParameterSource {
        val sqlParams = MapSqlParameterSource()
        this::class.declaredMemberProperties
            .filter { it.findAnnotation<DbField>() != null && filterFun(it) }
            .forEach { sqlParams.addValue(it.fieldName, getValue(it)) }
        return sqlParams
    }

    private val KProperty1<out Any, *>.fieldName: String
        get() = when {
            findAnnotation<DbField>()!!.value.isEmpty() -> name
            else -> findAnnotation<DbField>()!!.value
        }

    @Suppress("UNCHECKED_CAST")
    private fun KProperty1<out Any, *>.getValue(obj: Any, objectMapper: ObjectMapper = jdbcObjectMapper): Any? {
        return when (findAnnotation<DbField>()!!.type) {
            SerializationType.JSON -> convertObjectToJson(getter.call(obj), objectMapper)
            SerializationType.TEXT -> getter.call(obj)?.toString()
            SerializationType.TEXT_ARRAY -> (getter.call(obj) as Collection<String>?)?.toTypedArray()
            else -> getter.call(obj)
        }
    }

    private fun convertObjectToJson(value: Any?, objectMapper: ObjectMapper): String {
        return objectMapper.writeValueAsString(value)
    }
}