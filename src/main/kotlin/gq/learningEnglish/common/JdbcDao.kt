package gq.learningEnglish.common

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

    fun update(sql: String, vararg args: Any?): Int = jdbcTemplate.update(sql, *args)

    fun namedUpdate(sql: String, paramSource: SqlParameterSource): Int {
        return namedParameterJdbcTemplate.update(sql, paramSource)
    }
}