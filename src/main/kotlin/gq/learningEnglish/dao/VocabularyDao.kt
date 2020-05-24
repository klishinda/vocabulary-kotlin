package gq.learningEnglish.dao

import gq.learningEnglish.common.*
import gq.learningEnglish.model.QuestionnaireMapper
import gq.learningEnglish.model.Word
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class VocabularyDao(private var jdbc: JdbcDao) {
    fun addVocabularyRecord(russianWord: Word, englishWord: Word, userId: Int): Int {
        val sqlParams = MapSqlParameterSource(mapOf("userId" to userId, "rusId" to russianWord.id, "engId" to englishWord.id))
        return jdbc.namedUpdate(ADD_VOCABULARY_PAIR, sqlParams)
    }

    fun getWordsForQuiz(russianWordsNumber: Int, englishWordsNumber: Int): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(mapOf("numberOfRussianWords" to russianWordsNumber, "numberOfEnglishWords" to englishWordsNumber))
        return jdbc.namedQuery(GET_RANDOM_WORDS, QuestionnaireMapper(), sqlParams)
    }

    private companion object {
        const val ADD_VOCABULARY_PAIR = "insert into vocabulary(user_id, russian_id, english_id) values (:userId, :rusId, :engId)"
        const val GET_RANDOM_WORDS = "select e.id as asking_word_id, e.word as asking_word, e.description, v.id as vocabulary_id, r.id as answer_word_id, r.word as answer_word, 'ENGLISH' as asking_language\n" +
                                        "from (select * from public.english_words order by random() limit :numberOfEnglishWords) e\n" +
                                        "join public.vocabulary v on v.english_id = e.id\n" +
                                        "join public.russian_words r on r.id = v.russian_id\n" +
                                        "union all\n" +
                                        "select r.id, r.word, r.description, v.id, e.id, e.word, 'RUSSIAN'\n" +
                                        "from (select * from public.russian_words order by random() limit :numberOfRussianWords) r\n" +
                                        "join public.vocabulary v on v.russian_id = r.id\n" +
                                        "join public.english_words e on e.id = v.english_id"
    }
}