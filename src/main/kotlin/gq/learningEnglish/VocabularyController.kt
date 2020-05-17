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
        val rusWord = Word(1, "БЕЛЫЙ2", 102)
        val engWord = Word(2, "WHITE2", 101)
        //val addedId: Int = vocabularyService.addRussianWord(rusWord)
        val addedVocabularyId: Int = vocabularyService.addWordPair(rusWord, engWord, 101)
        println("$addedVocabularyId ")
    }
}