package gq.learningEnglish

import gq.learningEnglish.model.RandomWordsMode
import gq.learningEnglish.model.Word
import gq.learningEnglish.service.QuizService
import gq.learningEnglish.service.VocabularyService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller

@Controller
class VocabularyController (private val vocabularyService : VocabularyService, private val questionnaire: QuizService) {
    @Bean
    fun start() {
        println("Start")
        /*val rusWord = Word("БЕЛЫЙ2", 102)
        val engWord = Word("WHITE2", 101)
        val addedIdRus: Int = vocabularyService.addRussianWord(rusWord)
        val addedIdEng: Int = vocabularyService.addEnglishWord(engWord)
        val addedVocabularyId: Int = vocabularyService.addWordPair(rusWord, engWord, 101)
        println("$addedVocabularyId $addedIdRus $addedIdEng")*/
        questionnaire.getRandomWords(8, RandomWordsMode.ABSOLUTE_RANDOM)
    }
}