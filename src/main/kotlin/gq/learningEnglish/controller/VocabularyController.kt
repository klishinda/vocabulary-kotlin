package gq.learningEnglish.controller

import gq.learningEnglish.model.enums.AvailableLanguages.*
import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vocabulary")
class VocabularyController(private val vocabularyService: VocabularyService) {

    @PostMapping("/start")
    fun start() {
        println("Start")
        val rusWord = Word(name = "СНЕГ", language = RUSSIAN, partOfSpeech = 101)
        val engWord = Word(name = "SNOW", language = ENGLISH, partOfSpeech = 101)
        val addedIdRus: Long = vocabularyService.addWord(rusWord)
        val addedIdEng: Long = vocabularyService.addWord(engWord)
        val addedVocabularyId: Long = vocabularyService.addWordPair(addedIdRus, addedIdEng, USER_ID)
        println("$addedVocabularyId $addedIdRus $addedIdEng")
    }

    @PostMapping("/add-word")
    fun addWord(@RequestBody word: Word) = vocabularyService.addWord(word).also { println(it) }

    @PostMapping("/add-pair")
    fun addWordPair(@RequestParam firstId: Long, @RequestParam secondId: Long) =
        vocabularyService.addWordPair(firstId, secondId, USER_ID).also { println(it) }

    @GetMapping("/translate")
    fun getTranslate(@RequestParam wordId: Long) = vocabularyService.getTranslate(wordId, USER_ID).also { println(it) }
}

private const val USER_ID = 101L