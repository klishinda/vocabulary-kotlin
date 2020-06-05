package gq.learningEnglish.controller

import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vocabulary")
class VocabularyController (private val vocabularyService : VocabularyService) {

    @PostMapping("/start")
    fun start() {
        println("Start")
        val rusWord = Word("БЕЛЫЙ2", 102)
        val engWord = Word("WHITE2", 101)
        val addedIdRus: Int = vocabularyService.addRussianWord(rusWord)
        val addedIdEng: Int = vocabularyService.addEnglishWord(engWord)
        val addedVocabularyId: Int = vocabularyService.addWordPair(rusWord, engWord, 101)
        println("$addedVocabularyId $addedIdRus $addedIdEng")
    }
}