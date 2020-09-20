package gq.learningEnglish.dao

import gq.learningEnglish.common.infrastructure.JdbcDao
import gq.learningEnglish.common.infrastructure.namedQuery
import gq.learningEnglish.common.infrastructure.query
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class WordsDao(private val jdbc: JdbcDao) {

    fun getWord(id: Long): Word = jdbc.query(GET_WORD, id)

    fun addWord(word: Word): Long = jdbc.namedQuery(ADD_WORD, word.sqlParams)
}

private const val ADD_WORD =
    """insert into words(id, word, language, description, part_of_speech)
    values (default, :word, :language, :description, :part_of_speech) returning id"""
private const val GET_WORD =
    "select w.id, w.word, w.language, w.description, w.part_of_speech from words w where w.id = ?"
