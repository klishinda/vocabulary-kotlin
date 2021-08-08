package gq.learningEnglish.controller

import gq.learningEnglish.common.infrastructure.interfaces.Logger
import gq.learningEnglish.model.Word
import gq.learningEnglish.service.VocabularyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vocabulary")
class VocabularyController(private val vocabularyService: VocabularyService) : Logger {

    @PostMapping("/add-word")
    fun addWord(@RequestBody word: Word, @RequestParam user: String) =
        vocabularyService.addWord(word, user).also { log.info("new word $it") }

    @PostMapping("/add-pair")
    fun addWordPair(@RequestParam firstId: Long, @RequestParam secondId: Long, @RequestParam user: String) =
        vocabularyService.addWordPair(firstId, secondId, user).also { log.info("new vocabulary $it") }

    @PostMapping("/add-words-and-pair")
    fun addWordPair(@RequestBody words: List<Word>, @RequestParam user: String) =
        vocabularyService.addWordsAndPair(words, user).also { log.info("new vocabulary id $it") }

    @GetMapping("/translate")
    fun getTranslate(@RequestParam wordId: Long, @RequestParam user: String) =
        vocabularyService.getTranslate(wordId, user).also { log.info(it.toString()) }

    @GetMapping("/unused-words")
    fun getUnusedWords() = vocabularyService.getUnusedWords().also { log.info(it.toString()) }
}
