package gq.learningEnglish.dao

import gq.learningEnglish.model.Word
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class VocabularyDao(private var jdbc: NamedParameterJdbcTemplate) {
    fun addVocabularyRecord(russianWord: Word, englishWord: Word, userId: Int): Int {
        val sqlParams = MapSqlParameterSource(mapOf("userId" to userId, "rusId" to russianWord.id, "engId" to englishWord.id))
        return jdbc.update(ADD_VOCABULARY_PAIR, sqlParams)
    }

    private companion object {
        const val ADD_VOCABULARY_PAIR = "insert into vocabulary(user_id, russian_id, english_id) values (:userId, :rusId, :engId)"
    }
}