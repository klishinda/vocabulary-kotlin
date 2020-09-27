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
        val addedIdRus: Long = vocabularyService.addWord(rusWord, USER_NAME)
        val addedIdEng: Long = vocabularyService.addWord(engWord, USER_NAME)
        val addedVocabularyId: Long = vocabularyService.addWordPair(addedIdRus, addedIdEng, USER_NAME)
        println("$addedVocabularyId $addedIdRus $addedIdEng")
    }

    @PostMapping("/add-word")
    fun addWord(@RequestBody word: Word, @RequestParam user: String) =
        vocabularyService.addWord(word, user).also { println(it) }

    @PostMapping("/add-pair")
    fun addWordPair(@RequestParam firstId: Long, @RequestParam secondId: Long, @RequestParam user: String) =
        vocabularyService.addWordPair(firstId, secondId, user).also { println(it) }

    @GetMapping("/translate")
    fun getTranslate(@RequestParam wordId: Long, @RequestParam user: String) =
        vocabularyService.getTranslate(wordId, user).also { println(it) }

    @GetMapping("/unused-words")
    fun getUnusedWords() = vocabularyService.getUnusedWords().also { println(it) }
}

private const val USER_NAME = "Tronix"
