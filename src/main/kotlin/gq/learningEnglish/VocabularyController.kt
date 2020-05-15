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
        val rusWord = Word(445,"БЕЛЫЙ2", 102)
        val addedId: Int = vocabularyService.addRussianWord(rusWord)
        println(addedId)
    }
}