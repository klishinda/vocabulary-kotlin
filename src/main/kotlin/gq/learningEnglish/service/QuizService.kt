package gq.learningEnglish.service

import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.model.enums.QuestionnaireModes
import gq.learningEnglish.model.enums.QuestionnaireModes.*
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class QuizService(private val vocabularyDao: VocabularyDao) {

    fun getWordsForQuiz(
        numberOfWords: Int,
        userId: Long,
        wordsMode: QuestionnaireModes
    ): Map<Question, List<Answer>> {
        return when (wordsMode) {
            LESS_USED -> vocabularyDao.getLessUsedWords(numberOfWords, userId)
            else -> {
                val (firstLanguageCount, secondLanguageCount) = getWordCount(numberOfWords, wordsMode)
                vocabularyDao.getRandomWords(firstLanguageCount, secondLanguageCount, userId)
            }
        }
    }

    private fun getWordCount(numberOfRandomWords: Int, wordsMode: QuestionnaireModes): Pair<Int, Int> {
        return when (wordsMode) {
            ENGLISH_RANDOM -> 0 to numberOfRandomWords
            RUSSIAN_RANDOM -> numberOfRandomWords to 0
            ALL_RANDOM -> getNumberForRandom(numberOfRandomWords)
            else -> getNumberForRandom(numberOfRandomWords)
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
