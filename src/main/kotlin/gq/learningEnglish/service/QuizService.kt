package gq.learningEnglish.service

import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.model.enums.RandomWordsMode
import gq.learningEnglish.model.enums.RandomWordsMode.*
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class QuizService(private val vocabularyDao: VocabularyDao) {

    fun getRandomWords(
        numberOfRandomWords: Int,
        userId: Long,
        wordsMode: RandomWordsMode
    ): Map<Question, List<Answer>> {
        val (firstLanguageCount, secondLanguageCount) = getWordCount(numberOfRandomWords, wordsMode)
        return vocabularyDao.getWordsForQuiz(firstLanguageCount, secondLanguageCount, userId)
    }

    private fun getWordCount(numberOfRandomWords: Int, wordsMode: RandomWordsMode): Pair<Int, Int> {
        return when (wordsMode) {
            ENGLISH -> 0 to numberOfRandomWords
            RUSSIAN -> numberOfRandomWords to 0
            ABSOLUTE_RANDOM -> getNumberForRandom(numberOfRandomWords)
        }
    }

    private fun getNumberForRandom(numberOfRandomWords: Int): Pair<Int, Int> {
        val averageNumber = floor(numberOfRandomWords / 2.toDouble()).toInt()
        return when ((numberOfRandomWords % 2) == 0) {
            true -> averageNumber to averageNumber
            false -> {
                when (Math.random() < MEAN) {
                    true -> averageNumber to averageNumber + 1
                    false -> averageNumber + 1 to averageNumber
                }
            }
        }
    }
}

private const val MEAN = 0.5