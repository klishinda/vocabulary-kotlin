package gq.learningEnglish.dao

import gq.learningEnglish.common.GET_USER_ID
import gq.learningEnglish.common.infrastructure.JdbcDao
import gq.learningEnglish.common.infrastructure.namedQuery
import gq.learningEnglish.common.infrastructure.query
import gq.learningEnglish.model.enums.RandomWordsMode
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class QuizDao(private var jdbc: JdbcDao) {

    fun getUserId(username: String): Long = jdbc.query(GET_USER_ID, username)

    fun addHistory(historyMap: MapSqlParameterSource) = jdbc.namedUpdate(ADD_HISTORY_RECORD, historyMap)

    fun addLaunchInfo(userId: Long, mode: RandomWordsMode): Long {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "userId" to userId,
                "mode" to mode.name
            )
        )
        return jdbc.namedQuery(ADD_LAUNCH_RECORD, sqlParams)
    }
}

private const val ADD_HISTORY_RECORD =
    """insert into history(id, launch_id, vocabulary_id, asking_word, user_answer, result)
    values (default, :launchId, :vocabularyId, :askingWord, :userAnswer, :result)"""
private const val ADD_LAUNCH_RECORD =
    "insert into launches(id, user_id, finish_time, mode) values (default, :userId, default, :mode) returning id"