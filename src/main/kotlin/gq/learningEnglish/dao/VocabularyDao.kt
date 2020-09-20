package gq.learningEnglish.dao

import gq.learningEnglish.common.infrastructure.JdbcDao
import gq.learningEnglish.common.infrastructure.namedQuery
import gq.learningEnglish.common.infrastructure.namedQueryList
import gq.learningEnglish.common.infrastructure.queryList
import gq.learningEnglish.model.mapper.QuestionnaireMapper
import gq.learningEnglish.model.Word
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Service

@Service
class VocabularyDao(private var jdbc: JdbcDao) {

    fun addVocabularyRecord(firstWord: Word, secondWord: Word, userId: Long): Long {
        val sqlParams =
            MapSqlParameterSource(
                mapOf(
                    "userId" to userId,
                    "firstId" to firstWord.id,
                    "secondId" to secondWord.id
                )
            )
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

    fun getTranslate(wordId: Long, userId: Long): List<Word>? {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "wordId" to wordId,
                "userId" to userId
            )
        )
        return jdbc.namedQueryList(GET_TRANSLATE, sqlParams)
    }

    fun getUnusedWords(): List<Word>? = jdbc.queryList(UNUSED_WORDS)
}

private const val ADD_VOCABULARY_PAIR =
    "insert into vocabulary(user_id, first_word_id, second_word_id) values (:userId, :firstId, :secondId) returning id"
private const val GET_RANDOM_WORDS =
    """with wrd as (
        (select w.* from words w where w.language = 'ENGLISH' and exists (
            select 1 from vocabulary vv where vv.first_word_id = w.id or vv.second_word_id = w.id)
        order by random() limit :numberOfEnglishWords)
        union all
        (select w.* from words w where w.language = 'RUSSIAN' and exists (
            select 1 from vocabulary vv where vv.first_word_id = w.id or vv.second_word_id = w.id)
        order by random() limit :numberOfRussianWords)
    )
    select wrd.id as asking_word_id, wrd.word as asking_word, wrd.description, v.id as vocabulary_id,
    translate.id as answer_word_id, translate.word as answer_word, wrd.language as asking_language from wrd
    join public.vocabulary v on v.first_word_id = wrd.id
    join public.words translate on translate.id = v.second_word_id
    union all
    select wrd.id as asking_word_id, wrd.word as asking_word, wrd.description, v.id as vocabulary_id,
    translate.id as answer_word_id, translate.word as answer_word, wrd.language as asking_language from wrd
    join public.vocabulary v on v.second_word_id = wrd.id
    join public.words translate on translate.id = v.first_word_id"""
private const val GET_TRANSLATE =
    """select * from words where id in (
        select second_word_id from vocabulary where user_id = :userId and first_word_id = :wordId
        union all
        select first_word_id from vocabulary where user_id = :userId and second_word_id = :wordId
    )"""
private const val UNUSED_WORDS =
    """select * from words where id not in (
        select first_word_id from vocabulary
        union
        select second_word_id from vocabulary
    )"""