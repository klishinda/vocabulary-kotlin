package gq.learningEnglish.controller

import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vocabulary")
class VocabularyController (private val vocabularyService : VocabularyService) {

    @PostMapping("/start")
    fun start() {
        println("Start")
        val rusWord = Word(name = "СНЕГ", language = "RUSSIAN", partOfSpeech = 101)
        val engWord = Word(name = "SNOW", language = "ENGLISH", partOfSpeech = 101)
        val addedIdRus: Long = vocabularyService.addWord(rusWord)
        val addedIdEng: Long = vocabularyService.addWord(engWord)
        val addedVocabularyId: Long = vocabularyService.addWordPair(addedIdRus, addedIdEng, 101)
        println("$addedVocabularyId $addedIdRus $addedIdEng")
    }

    @PostMapping("/add-word")
    fun addRussianWord(
        @RequestParam name: String,
        @RequestParam language: String,
        @RequestParam partOfSpeech: Int
    ) = vocabularyService.addWord(Word(name = name, language = language, partOfSpeech = partOfSpeech)).also { println(it) }
}