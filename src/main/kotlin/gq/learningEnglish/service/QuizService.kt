package gq.learningEnglish.service

import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class QuizService(private val vocabularyDao: VocabularyDao) {
    var russianWordsCount = 0
    var englishWordsCount = 0

    fun getRandomWords(numberOfRandomWords: Int, wordsMode: RandomWordsMode): Map<Question, List<Answer>> {
        getWordCount(numberOfRandomWords, wordsMode)
        val result = vocabularyDao.getWordsForQuiz(russianWordsCount, englishWordsCount)
        result.forEach { (k, v) -> println("$k   $v")}
        return result
    }

    private fun getWordCount(numberOfRandomWords: Int, wordsMode: RandomWordsMode) {
        if (wordsMode == RandomWordsMode.ABSOLUTE_RANDOM) {
            russianWordsCount = floor(numberOfRandomWords / 2.toDouble()).toInt()
            englishWordsCount = floor(numberOfRandomWords / 2.toDouble()).toInt()
            if ((numberOfRandomWords % 2) == 1) {
                if (Math.random() < 0.5) {
                    russianWordsCount++
                } else {
                    englishWordsCount++
                }
            }
        }
        else if (wordsMode == RandomWordsMode.ENGLISH) {
            englishWordsCount = numberOfRandomWords
        } else if (wordsMode == RandomWordsMode.RUSSIAN) {
            russianWordsCount = numberOfRandomWords
        }
        println("$russianWordsCount $englishWordsCount")
    }
}