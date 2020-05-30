package gq.learningEnglish.dao

import gq.learningEnglish.common.JdbcDao
import gq.learningEnglish.common.namedQuerySingle
import gq.learningEnglish.model.Word
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class EnglishWordsDao(private val jdbc: JdbcDao) {
    fun getEnglishWord(word: String?): Word? {
        val sqlParams = MapSqlParameterSource(mapOf("word" to word?.toUpperCase()))
        return jdbc.namedQuerySingle(GET_ENGLISH_WORD, sqlParams)
    }

    fun addEnglishWord(word: Word) : Int {
        word.run {
            val sqlParams = MapSqlParameterSource(mapOf(
                    "word" to name?.toUpperCase(),
                    "description" to description,
                    "partOfSpeech" to partOfSpeech))
            return jdbc.namedUpdate(ADD_ENGLISH_WORD, sqlParams)
        }
    }

    private companion object {
        const val ADD_ENGLISH_WORD = "insert into english_words(word, description, part_of_speech) values (:word, :description, :partOfSpeech)"
        const val GET_ENGLISH_WORD = "select e.id, e.word, e.description, e.part_of_speech from english_words e where e.word = :word"
    }
}