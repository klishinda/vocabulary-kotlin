package gq.learningEnglish.controller

import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.service.QuizService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller

@Controller
class QuizController(private val questionnaire: QuizService) {
    @Bean
    fun startQuiz() {
        questionnaire.getRandomWords(5, RandomWordsMode.ABSOLUTE_RANDOM)
    }
}