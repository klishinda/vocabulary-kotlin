package gq.learningEnglish.dao

import gq.learningEnglish.common.JdbcDao
import gq.learningEnglish.model.Word
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class RussianWordsDao(private val jdbc: JdbcDao) {
    fun addRussianWord(word: Word) : Int {
        word.run {
            val sqlParams = MapSqlParameterSource(mapOf(
                    "word" to name.toUpperCase(),
                    "description" to description,
                    "partOfSpeech" to partOfSpeech))
            return jdbc.namedUpdate(ADD_RUSSIAN_WORD, sqlParams)
        }
    }

    private companion object {
        const val ADD_RUSSIAN_WORD = "insert into public.russian_words(word, description, part_of_speech) values (:word, :description, :partOfSpeech)"
        //const val GET_RUSSIAN_WORD_ID = "select r.id, r.word, r.description, r.part_of_speech from russian_words r where r.word = :word"
    }
}