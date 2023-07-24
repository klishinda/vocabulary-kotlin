package gq.learningEnglish.dao

import gq.learningEnglish.common.infrastructure.*
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

    fun getRandomWords(russianWordsNumber: Int, englishWordsNumber: Int, userId: Long): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "numberOfRussianWords" to russianWordsNumber,
                "numberOfEnglishWords" to englishWordsNumber,
                "userId" to userId
            )
        )
        return jdbc.namedQuery(GET_RANDOM_WORDS, QuestionnaireMapper(), sqlParams)
    }

    fun getLessUsedWords(wordCount: Int, userId: Long): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "wordCount" to wordCount,
                "userId" to userId
            )
        )
        return jdbc.namedQuery(GET_LESS_USED_WORDS, QuestionnaireMapper(), sqlParams)
    }

    fun getWordsByPercentage(percentage: Int, userId: Long): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "percentage" to percentage,
                "userId" to userId
            )
        )
        return jdbc.namedQuery(GET_WORDS_BY_PERCENTAGE, QuestionnaireMapper(), sqlParams)
    }

    fun getWordsWithWrongAnswer(lastWordsNumber: Int, userId: Long): Map<Question, List<Answer>> {
        val sqlParams = MapSqlParameterSource(
            mapOf(
                "rank" to lastWordsNumber,
                "userId" to userId
            )
        )
        return jdbc.namedQuery(GET_WORDS_WITH_WRONG_ANSWER, QuestionnaireMapper(), sqlParams)
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
        (select w.*
         from words w
         where w.language = 'ENGLISH'
           and w.user_id = :userId
           and exists(select 1
                      from vocabulary vv
                      where vv.first_word_id = w.id
                         or vv.second_word_id = w.id)
         limit :numberOfEnglishWords)
        union all
        (select w.*
         from words w
         where w.language = 'RUSSIAN'
           and w.user_id = :userId
           and exists(select 1
                      from vocabulary vv
                      where vv.first_word_id = w.id
                         or vv.second_word_id = w.id)
         limit :numberOfRussianWords)
    )
    select wrd.id         as asking_word_id,
           wrd.word       as asking_word,
           wrd.description,
           v.id           as vocabulary_id,
           translate.id   as answer_word_id,
           translate.word as answer_word,
           wrd.language   as asking_language
    from wrd
             join public.vocabulary v on v.first_word_id = wrd.id or v.second_word_id = wrd.id
             join public.words translate on translate.id =
                                            case
                                                when v.first_word_id = wrd.id then v.second_word_id
                                                when v.second_word_id = wrd.id then v.first_word_id
                                            end
    order by random()"""
private const val GET_LESS_USED_WORDS =
    """select x.* from (
        select wrd.id         as asking_word_id,
               wrd.word       as asking_word,
               wrd.description,
               v.id           as vocabulary_id,
               translate.id   as answer_word_id,
               translate.word as answer_word,
               wrd.language   as asking_language
        from words wrd
        join (
            select w.id,
                   sum(case when h.asking_word = w.id then 1 else 0 end) all_calls
            from words w
            join vocabulary v on v.first_word_id = w.id or v.second_word_id = w.id
            left join history h on h.vocabulary_id = v.id
            where w.user_id = :userId
            group by v.id, w.id
        ) ww on ww.id = wrd.id
        join vocabulary v on v.first_word_id = wrd.id or v.second_word_id = wrd.id
        join words translate on translate.id =
                                case
                                    when v.first_word_id = wrd.id then v.second_word_id
                                    when v.second_word_id = wrd.id then v.first_word_id
                                end
        order by all_calls
        limit :wordCount) x
    order by random()"""
private const val GET_WORDS_BY_PERCENTAGE =
    """select percent_words.asking_word as asking_word_id,
           asking_word.word          as asking_word,
           asking_word.description,
           v.id                      as vocabulary_id,
           answer_word.id            as answer_word_id,
           answer_word.word          as answer_word,
           asking_word.language      as asking_language
    from (select distinct h.asking_word
          from history h
                   join launches l on l.id = h.launch_id
              and l.user_id = :userId
          group by h.vocabulary_id, h.asking_word
          having cast(sum(case when h.result then 1 else 0 end) as decimal(3, 0))
                     / cast(count(*) as decimal(3, 0)) * 100 <= :percentage
         ) percent_words
             join vocabulary v
                  on v.first_word_id = percent_words.asking_word or v.second_word_id = percent_words.asking_word
             join words asking_word on asking_word.id = percent_words.asking_word
             join words answer_word on answer_word.id =
                                       case
                                           when v.first_word_id = percent_words.asking_word then v.second_word_id
                                           when v.second_word_id = percent_words.asking_word then v.first_word_id
                                           end
    order by random()"""
private const val GET_WORDS_WITH_WRONG_ANSWER =
    """with failed_words as (
    select distinct x.asking_word
                  from (
                           select h.*,
                                  l.*,
                                  row_number()
                                  over (partition by h.vocabulary_id, h.asking_word order by l.finish_time desc) as rnk
                           from history h
                                    join launches l on l.id = h.launch_id
                           where l.user_id = 101
                           order by h.vocabulary_id, h.asking_word
                       ) x
                  where x.rnk < :rank
                    and x.result = false
        )
    select wrd.id         as asking_word_id,
           wrd.word       as asking_word,
           wrd.description,
           v.id           as vocabulary_id,
           translate.id   as answer_word_id,
           translate.word as answer_word,
           wrd.language   as asking_language
    from vocabulary v
    join failed_words f on f.asking_word in (v.first_word_id, v.second_word_id)
    join words wrd on wrd.id = f.asking_word
    join words translate on translate.id =
                                     case
                                         when v.first_word_id = wrd.id then v.second_word_id
                                         when v.second_word_id = wrd.id then v.first_word_id
                                         end
    order by random()"""
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
