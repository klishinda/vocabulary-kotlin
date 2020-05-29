package gq.learningEnglish.service

import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuizUserService(private val quizService: QuizService, private var scanner: Scanner = Scanner(System.`in`)) {
    fun quiz(numberOfRandomWords : Int, wordsMode : RandomWordsMode) : Map<Question, List<Answer>> {
        val quizMap = quizService.getRandomWords(numberOfRandomWords, wordsMode)
        println("Let's start! Write translation to the next words." + quizMap.size)

        var resultForPrint: String
        var countCorrectAnswers = 0
        var countAllWords = 0
        // set only correct answers. All empty fields "result" means wrong answer
        quizMap.forEach { (k, v) ->
            val questionWord: Question = k
            println(k.askingWord + " " + questionWord.description)
            for (answer in v) {
                countAllWords++
                println("Your answer: ")
                val userAnswer = scanner.nextLine().toUpperCase()
                if (v.stream().anyMatch { s: Answer -> s.answerWord.equals(userAnswer) && !s.result }) {
                    v.stream().filter { s: Answer -> s.answerWord.equals(userAnswer) }.forEach { a: Answer -> a.result = true }
                    resultForPrint = "CORRECT!"
                    countCorrectAnswers++
                } else {
                    resultForPrint = "INCORRECT!"
                }
                println("Your answer is $userAnswer. $resultForPrint")
            }
        }
        println("----------RESULTS----------")
        println("Correct answers: $countCorrectAnswers/$countAllWords")

        return quizMap
    }
}