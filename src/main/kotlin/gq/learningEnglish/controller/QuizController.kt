package gq.learningEnglish.controller

import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.service.QuizService
import gq.learningEnglish.service.QuizUserService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller

@Controller
class QuizController(private val questionnaire: QuizService, private val userQuestionnaire: QuizUserService) {
    @Bean
    fun startQuiz() {
        //questionnaire.getRandomWords(5, RandomWordsMode.ABSOLUTE_RANDOM)
        userQuestionnaire.quiz(5, RandomWordsMode.ABSOLUTE_RANDOM)
    }
}