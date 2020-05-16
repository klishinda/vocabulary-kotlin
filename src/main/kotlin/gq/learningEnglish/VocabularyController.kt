package gq.learningEnglish

import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller

@Controller
class VocabularyController (private val vocabularyService : VocabularyService) {
    @Bean
    fun start() {
        println("Start")
        val rusWord = Word("БЕЛЫЙ2", 102, "Test")
        val addedId: Int = vocabularyService.addRussianWord(rusWord)
        println(addedId)
    }
}