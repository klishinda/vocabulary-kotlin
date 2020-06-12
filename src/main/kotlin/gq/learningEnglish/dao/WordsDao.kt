package gq.learningEnglish.dao

import gq.learningEnglish.common.JdbcDao
import gq.learningEnglish.common.namedQuery
import gq.learningEnglish.common.query
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class WordsDao(private val jdbc: JdbcDao) {

    fun getWord(id: Long): Word = jdbc.query(GET_WORD, id)

    fun addWord(word: Word): Long = jdbc.namedQuery(ADD_WORD, word.sqlParams)
}

private const val ADD_WORD =
    """insert into words(id, word, language, description, part_of_speech)
    values (default, :word, :language, :description, :partOfSpeech) returning id"""
private const val GET_WORD =
    "select w.id, w.word, w.language, w.description, w.part_of_speech from words w where w.id = ?"