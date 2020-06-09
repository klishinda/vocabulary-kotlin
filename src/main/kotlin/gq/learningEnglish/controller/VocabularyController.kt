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
        val rusWord = Word(name = "БЕЛЫЙ2", partOfSpeech = 102)
        val engWord = Word(name = "WHITE2", partOfSpeech = 101)
        val addedIdRus: Int = vocabularyService.addRussianWord(rusWord)
        val addedIdEng: Int = vocabularyService.addEnglishWord(engWord)
        val addedVocabularyId: Int = vocabularyService.addWordPair(rusWord, engWord, 101)
        println("$addedVocabularyId $addedIdRus $addedIdEng")
    }

    @PostMapping("/add-russian-word")
    fun addRussianWord(
        @RequestParam name: String,
        @RequestParam partOfSpeech: Int
    ) {
        val word = Word(name = name, partOfSpeech = partOfSpeech)
        vocabularyService.addRussianWord(word).also { println(it) }
    }

    @PostMapping("/add-english-word")
    fun addEnglishWord(
        @RequestParam name: String,
        @RequestParam partOfSpeech: Int
    ) {
        val word = Word(name = name, partOfSpeech = partOfSpeech)
        vocabularyService.addEnglishWord(word).also { println(it) }
    }
}