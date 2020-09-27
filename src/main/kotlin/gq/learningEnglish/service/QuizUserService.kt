package gq.learningEnglish.service

import gq.learningEnglish.dao.CommonDao
import gq.learningEnglish.dao.QuizDao
import gq.learningEnglish.model.enums.RandomWordsMode
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuizUserService(
    private val quizService: QuizService,
    private val quizDao: QuizDao,
    private val commonDao: CommonDao
) {

    fun quiz(numberOfRandomWords: Int, wordsMode: RandomWordsMode, username: String) {
        val userId = commonDao.getUserId(username)
        val quizMap = quizService.getRandomWords(numberOfRandomWords, userId, wordsMode)
        println("Let's start! Write translation to the next words (${quizMap.size} total)")
        quizMap.forEach { (question, answers) ->
            println("${question.askingWord} ${question.description.orEmpty()}")
            processQuiz(answers)
        }
        processResults(userId, quizMap, wordsMode)
    }

    private fun processQuiz(answers: List<Answer>) {
        val scanner = Scanner(System.`in`)
        val wrongAnswers: MutableList<String> = mutableListOf()
        for (answer in answers) {
            println("Your answer: ")
            val userAnswer = scanner.nextLine().toUpperCase()
            if (answers.stream().anyMatch { s: Answer -> s.answerWord == userAnswer && !s.result }) {
                answers.stream().filter { s: Answer -> s.answerWord == userAnswer }
                    .forEach { a: Answer ->
                        a.result = true
                        a.userAnswer = userAnswer
                    }
            } else {
                wrongAnswers.add(userAnswer)
            }
        }
        for (wrongAnswer in wrongAnswers) {
            answers.stream().filter { s: Answer -> s.userAnswer == null && !s.result }
                .forEach { a: Answer -> a.userAnswer = wrongAnswer }
        }
    }

    private fun processResults(userId: Long, quizMap: Map<Question, List<Answer>>, wordsMode: RandomWordsMode) {
        val launchId = quizDao.addLaunchInfo(userId, wordsMode)
        val results = quizMap.values.flatten()
        println("Общая статистика по ответам:")
        quizMap.forEach { (question, answers) ->
            println("\nВопрос: ${question.askingWord} ${question.description.orEmpty()}")
            for (answer in answers) {
                println("Правильный ответ: ${answer.answerWord}")
                println("Ваш ответ: ${answer.userAnswer}. ${resultMapping[answer.result]}")
                quizDao.addHistory(answer.setAnswerHistoryMap(launchId, question.askingWordId))
            }
        }
        println("\nОбщее количество слов: ${results.size}")
        println("Количество правильных ответов: ${results.filter { x -> x.result }.size}")
    }
}

private val resultMapping = mapOf(true to "CORRECT", false to "WRONG")
