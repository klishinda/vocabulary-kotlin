package gq.learningEnglish.dao

import gq.learningEnglish.common.JdbcDao
import gq.learningEnglish.common.query
import gq.learningEnglish.model.Word
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class WordsDao(private val jdbc: JdbcDao) {

    fun getWord(id: Long): Word {
        return jdbc.query(GET_WORD, id)
    }

    fun addWord(word: Word) : Long {
        word.run {
            val sqlParams = MapSqlParameterSource(mapOf(
                "word" to name.toUpperCase(),
                "language" to language,
                "description" to description,
                "partOfSpeech" to partOfSpeech))
            return jdbc.namedUpdate(ADD_WORD, sqlParams).toLong()
        }
    }
}

private const val ADD_WORD = "insert into words(id, word, language, description, part_of_speech) values (default, :word, :language, :description, :partOfSpeech)"
private const val GET_WORD = "select w.id, w.word, w.language, w.description, w.part_of_speech from words w where w.id = ?"