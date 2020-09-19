package gq.learningEnglish.service

import gq.learningEnglish.model.enums.RandomWordsMode
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuizUserService(private val quizService: QuizService) {

    fun quiz(numberOfRandomWords: Int, wordsMode: RandomWordsMode): Map<Question, List<Answer>> {
        val quizMap = quizService.getRandomWords(numberOfRandomWords, wordsMode)
        println("Let's start! Write translation to the next words (${quizMap.size} total)")
        // set only correct answers. All empty fields "result" means wrong answer
        quizMap.forEach { (question, answers) ->
            println("${question.askingWord} ${question.description.orEmpty()}")
            processQuiz(answers)
        }
        println(quizMap)

        return quizMap
    }

    private fun processQuiz(answers: List<Answer>) {
        val scanner = Scanner(System.`in`)
        for (answer in answers) {
            println("Your answer: ")
            val userAnswer = scanner.nextLine().toUpperCase()
            if (answers.stream().anyMatch { s: Answer -> s.answerWord.equals(userAnswer) && !s.result }) {
                answers.stream().filter { s: Answer -> s.answerWord.equals(userAnswer) }
                    .forEach { a: Answer -> a.result = true }
            }
        }
        for (answer in answers) {
            println(answer.result)
        }
    }
}