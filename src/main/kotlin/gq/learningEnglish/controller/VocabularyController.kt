package gq.learningEnglish.controller

import gq.learningEnglish.common.infrastructure.interfaces.Logger
import gq.learningEnglish.model.enums.AvailableLanguages.*
import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vocabulary")
class VocabularyController(private val vocabularyService: VocabularyService) : Logger {

    @PostMapping("/test")
    fun testFunction() {
        log.info("Start")
        val rusWord = Word(name = "СНЕГ", language = RUSSIAN, partOfSpeech = 101)
        val engWord = Word(name = "SNOW", language = ENGLISH, partOfSpeech = 101)
        val addedIdRus: Long = vocabularyService.addWord(rusWord, USER_NAME)
        val addedIdEng: Long = vocabularyService.addWord(engWord, USER_NAME)
        val addedVocabularyId: Long = vocabularyService.addWordPair(addedIdRus, addedIdEng, USER_NAME)
        log.info("vocabulary id = $addedVocabularyId, russian word id = $addedIdRus, english word id = $addedIdEng")
    }

    @PostMapping("/add-word")
    fun addWord(@RequestBody word: Word, @RequestParam user: String) =
        vocabularyService.addWord(word, user).also { log.info("new id $it") }

    @PostMapping("/add-pair")
    fun addWordPair(@RequestParam firstId: Long, @RequestParam secondId: Long, @RequestParam user: String) =
        vocabularyService.addWordPair(firstId, secondId, user).also { log.info("new id $it") }

    @GetMapping("/translate")
    fun getTranslate(@RequestParam wordId: Long, @RequestParam user: String) =
        vocabularyService.getTranslate(wordId, user).also { log.info(it.toString()) }

    @GetMapping("/unused-words")
    fun getUnusedWords() = vocabularyService.getUnusedWords().also { log.info(it.toString()) }
}

private const val USER_NAME = "Tronix"
