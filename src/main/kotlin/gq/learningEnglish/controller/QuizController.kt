package gq.learningEnglish.controller

import gq.learningEnglish.model.enums.QuestionnaireModes
import gq.learningEnglish.service.QuizUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz")
class QuizController(private val userQuestionnaire: QuizUserService) {

    @PostMapping("/start")
    fun startQuiz(
        @RequestParam wordCount: Int,
        @RequestParam mode: QuestionnaireModes,
        @RequestParam user: String
    ) = userQuestionnaire.quiz(wordCount, mode, user)
}
