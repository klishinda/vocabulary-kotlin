package gq.learningEnglish.dao

import gq.learningEnglish.common.JdbcDao
import gq.learningEnglish.common.namedQuery
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class HistoryDao(private var jdbc: JdbcDao) {

    fun addHistory(historyMap: MapSqlParameterSource): Long = jdbc.namedQuery(ADD_HISTORY_RECORD, historyMap)
}

private const val ADD_HISTORY_RECORD =
    """insert into history(id, adding_time, user_id, vocabulary_id, asking_word, user_answer, result)
    values (default, default, :userId, :vocabularyId, :askingWord, :userAnswer, :result) returning id"""