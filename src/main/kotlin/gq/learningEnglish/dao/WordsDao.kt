package gq.learningEnglish.dao

import gq.learningEnglish.common.infrastructure.JdbcDao
import gq.learningEnglish.common.infrastructure.namedQuery
import gq.learningEnglish.common.infrastructure.query
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class WordsDao(private val jdbc: JdbcDao) {

    fun getWord(id: Long, userId: Long): Word = jdbc.query(GET_WORD, id, userId)

    fun addWord(word: Word, userId: Long): Word = jdbc.namedQuery(ADD_WORD, word.sqlParams.addValue("userId", userId))
}

private const val ADD_WORD =
    """insert into words(id, user_id, word, language, description, part_of_speech)
        values (default, :userId, upper(:word), :language, :description, :part_of_speech)
        on conflict (user_id, word, language, description) do update set word = excluded.word
    returning *"""
private const val GET_WORD =
    "select w.id, w.word, w.language, w.description, w.part_of_speech from words w where w.id = ? and user_id = ?"
