package gq.learningEnglish.common.infrastructure

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import javax.sql.DataSource
import kotlin.reflect.KClass

class JdbcDao(dataSource: DataSource) {
    private val jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    fun <T> queryList(sql: String, rowMapper: RowMapper<T>, vararg args: Any?): List<T> {
        return jdbcTemplate.query(sql, rowMapper, *args)
    }

    fun <T : Any> queryList(sql: String, elementType: KClass<T>, vararg args: Any?): List<T> {
        return jdbcTemplate.queryForList(sql, elementType.java, *args)
    }

    fun queryRowList(sql: String, vararg args: Any?): List<Map<String, Any>> = jdbcTemplate.queryForList(sql, *args)

    fun <T : Any> query(sql: String, requiredType: KClass<T>, vararg args: Any?): T {
        return jdbcTemplate.queryForObject(sql, requiredType.java, *args)
    }

    fun <T> query(sql: String, rowMapper: RowMapper<T>, vararg args: Any?): T {
        return jdbcTemplate.queryForObject(sql, rowMapper, *args)!!
    }

    fun <T : Any> querySingle(sql: String, requiredType: KClass<T>, vararg args: Any?): T? {
        return queryList(sql, requiredType, *args).firstOrNull()
    }

    fun <T> querySingle(sql: String, rowMapper: RowMapper<T>, vararg args: Any?): T? {
        return queryList(sql, rowMapper, *args).firstOrNull()
    }

    fun queryMap(sql: String, vararg args: Any?): Map<String, Any?> = jdbcTemplate.queryForMap(sql, *args)

    fun update(sql: String, vararg args: Any?): Int = jdbcTemplate.update(sql, *args)

    fun <T> namedQuery(sql: String, rowMapper: RowMapper<T>, paramSource: SqlParameterSource): T {
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper)!!
    }

    fun <T : Any> namedQuery(sql: String, requiredType: KClass<T>, paramSource: SqlParameterSource): T {
        return namedParameterJdbcTemplate.queryForObject(sql, paramSource, requiredType.java)!!
    }

    fun <T> namedQuerySingle(sql: String, rowMapper: RowMapper<T>, paramSource: SqlParameterSource): T? {
        return namedQueryList(sql, rowMapper, paramSource).firstOrNull()
    }

    fun <T> namedQueryList(sql: String, rowMapper: RowMapper<T>, paramSource: SqlParameterSource): List<T> {
        return namedParameterJdbcTemplate.query(sql, paramSource, rowMapper)
    }

    fun namedQueryMap(sql: String, paramSource: SqlParameterSource): Map<String, Any?> {
        return namedParameterJdbcTemplate.queryForMap(sql, paramSource)
    }

    fun namedUpdate(sql: String, paramSource: SqlParameterSource): Int {
        return namedParameterJdbcTemplate.update(sql, paramSource)
    }
}
