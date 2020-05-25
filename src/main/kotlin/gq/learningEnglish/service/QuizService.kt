package gq.learningEnglish.service

import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class QuizService(private val vocabularyDao: VocabularyDao) {
    fun getRandomWords(numberOfRandomWords: Int, wordsMode: RandomWordsMode): Map<Question, List<Answer>> {
        var russianWordsNumber = 0
        var englishWordsNumber = 0
        if (wordsMode == RandomWordsMode.ABSOLUTE_RANDOM) {
            russianWordsNumber = floor(numberOfRandomWords / 2.toDouble()).toInt()
            englishWordsNumber = floor(numberOfRandomWords / 2.toDouble()).toInt()
            if ((numberOfRandomWords % 2) == 1) {
                if (Math.random() < 0.5) {
                    russianWordsNumber++
                } else {
                    englishWordsNumber++
                }
            }
        }
        else if (wordsMode == RandomWordsMode.ENGLISH) {
            englishWordsNumber = numberOfRandomWords
        } else if (wordsMode == RandomWordsMode.RUSSIAN) {
            russianWordsNumber = numberOfRandomWords
        }
        println("$russianWordsNumber $englishWordsNumber")
        val result = vocabularyDao.getWordsForQuiz(russianWordsNumber, englishWordsNumber)
        result.forEach { (k, v) -> println("$k   $v")}
        return result
    }
}