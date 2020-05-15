package gq.learningEnglish.dao

import gq.learningEnglish.model.Word
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class RussianWordsDao(private var jdbc: NamedParameterJdbcTemplate) {
    fun addRussianWord(word: Word) : Int {
        word.run {
            val sqlParams = MapSqlParameterSource(mapOf("word" to name?.toUpperCase(), "description" to description, "partOfSpeech" to partOfSpeech))
            return jdbc.update(ADD_RUSSIAN_WORD, sqlParams)
        }
    }

    private companion object {
        const val ADD_RUSSIAN_WORD = "insert into russian_words(word, description, part_of_speech) values (:word, :description, :partOfSpeech)"
    }
}