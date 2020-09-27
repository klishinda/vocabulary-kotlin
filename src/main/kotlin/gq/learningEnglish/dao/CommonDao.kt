package gq.learningEnglish.dao

import gq.learningEnglish.common.infrastructure.JdbcDao
import gq.learningEnglish.common.infrastructure.query
import org.springframework.stereotype.Service

@Service
class CommonDao(private var jdbc: JdbcDao) {

    fun getUserId(username: String): Long = jdbc.query(GET_USER_ID, username)
}

private const val GET_USER_ID = "select id from users where login = ?"
