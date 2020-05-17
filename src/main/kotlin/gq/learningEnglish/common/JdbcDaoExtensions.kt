package gq.learningEnglish.common

import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import kotlin.reflect.KClass

inline fun <reified T : Any> JdbcDao.queryList(sql: String, vararg args: Any?): List<T> {
    return queryExecFun<T, List<T>>({ queryList(sql, it, *args) }, { queryList(sql, it, *args) })
}

inline fun <reified T : Any> JdbcDao.query(sql: String, vararg args: Any?): T {
    return queryExecFun<T, T>({ query(sql, it, *args) }, { query(sql, it, *args) })
}

inline fun <reified T : Any> JdbcDao.querySingle(sql: String, vararg args: Any?): T? {
    return queryExecFun<T, T?>({ querySingle(sql, it, *args) }, { querySingle(sql, it, *args) })
}

inline fun <reified T : Any> JdbcDao.namedQuery(sql: String, paramSource: SqlParameterSource): T {
    return queryExecFun<T, T>({ namedQuery(sql, it, paramSource) }, { namedQuery(sql, it, paramSource) })
}

inline fun <reified T : Any> JdbcDao.namedQuerySingle(sql: String, paramSource: SqlParameterSource): T? {
    return queryExecFun<T, T?>({ namedQuerySingle(sql, SingleColumnRowMapper(T::class.java), paramSource) },
            { namedQuerySingle(sql, it, paramSource) })
}

inline fun <reified T : Any> JdbcDao.namedQueryList(sql: String, paramSource: SqlParameterSource): List<T> {
    return queryExecFun<T, List<T>>({ namedQueryList(sql, SingleColumnRowMapper(T::class.java), paramSource) },
            { namedQueryList(sql, it, paramSource) })
}

fun <T : Any> isMappable(kClass: KClass<T>): Boolean {
    return SERIALIZABLE_CLASSES.contains(kClass)
}

inline fun <reified T : Any, R> JdbcDao.queryExecFun(
        mappableFun: JdbcDao.(KClass<T>) -> R,
        rowMapperFun: JdbcDao.(KotlinRowMapper<T>) -> R
): R {
    T::class.let {
        return if (isMappable(it)) {
            mappableFun(it)
        } else {
            rowMapperFun(KotlinRowMapper(it))
        }
    }
}