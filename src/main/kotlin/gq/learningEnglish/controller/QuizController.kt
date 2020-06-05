package gq.learningEnglish.controller

import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.service.QuizUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz")
class QuizController(private val userQuestionnaire: QuizUserService) {

    @PostMapping("/start")
    fun startQuiz() {
        userQuestionnaire.quiz(5, RandomWordsMode.ABSOLUTE_RANDOM)
    }
}