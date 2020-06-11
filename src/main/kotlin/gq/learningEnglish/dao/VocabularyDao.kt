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
    fun addVocabularyRecord(russianWord: Word, englishWord: Word, userId: Long): Long {
        val sqlParams =
            MapSqlParameterSource(mapOf("userId" to userId, "rusId" to russianWord.id, "engId" to englishWord.id))
        return jdbc.namedQuery(ADD_VOCABULARY_PAIR, sqlParams)
    }

    fun getWordsForQuiz(russianWordsNumber: Int, englishWordsNumber: Int): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "numberOfRussianWords" to russianWordsNumber,
                "numberOfEnglishWords" to englishWordsNumber
            )
        )
        return jdbc.namedQuery(GET_RANDOM_WORDS, QuestionnaireMapper(), sqlParams)
    }
}

private const val ADD_VOCABULARY_PAIR =
    "insert into vocabulary(user_id, first_word_id, second_word_id) values (:userId, :rusId, :engId) returning id"
private const val GET_RANDOM_WORDS = "with wrd as (\n" +
        "    (select w.* from words w where w.language = 'ENGLISH' and exists (select 1 from vocabulary vv where vv.first_word_id = w.id or vv.second_word_id = w.id) order by random() limit :numberOfEnglishWords)\n" +
        "    union all\n" +
        "    (select w.* from words w where w.language = 'RUSSIAN' and exists (select 1 from vocabulary vv where vv.first_word_id = w.id or vv.second_word_id = w.id) order by random() limit :numberOfRussianWords)\n" +
        ")\n" +
        "select wrd.id as asking_word_id, wrd.word as asking_word, wrd.description, v.id as vocabulary_id, translate.id as answer_word_id, translate.word as answer_word, wrd.language as asking_language from wrd\n" +
        "join public.vocabulary v on v.first_word_id = wrd.id\n" +
        "join public.words translate on translate.id = v.second_word_id\n" +
        "union all\n" +
        "select wrd.id as asking_word_id, wrd.word as asking_word, wrd.description, v.id as vocabulary_id, translate.id as answer_word_id, translate.word as answer_word, wrd.language as asking_language from wrd\n" +
        "join public.vocabulary v on v.second_word_id = wrd.id\n" +
        "join public.words translate on translate.id = v.first_word_id"