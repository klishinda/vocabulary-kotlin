package gq.learningEnglish.service

import gq.learningEnglish.dao.EnglishWordsDao
import gq.learningEnglish.dao.RussianWordsDao
import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class VocabularyService(
    private val russianWordsDao: RussianWordsDao,
    private val englishWordsDao: EnglishWordsDao,
    private val vocabularyDao: VocabularyDao
) {
    fun addRussianWord(newWord: Word) : Int {
        return russianWordsDao.addRussianWord(newWord)
    }

    fun addWordPair(russianWord: Word, englishWord: Word, userId: Int) : Int {
        val russianWords = russianWordsDao.getRussianWord(russianWord.name)
        val englishWords = englishWordsDao.getEnglishWord(englishWord.name)
        checkWords(russianWords, englishWords, russianWord, englishWord)
        println("rusId: "+russianWord.id+", engId: "+englishWord.id)
        return vocabularyDao.addVocabularyRecord(russianWord, englishWord, userId);
    }

    private fun checkWords(
        russianWords: List<Word>?,
        englishWords: List<Word>?,
        russianWord: Word,
        englishWord: Word
    ) {
        if (russianWords == null) {
            russianWord.id = russianWordsDao.addRussianWord(russianWord).toLong()
        } else if (russianWords.size == 1) {
            russianWord.id = russianWords[0].id
        }
        if (englishWords == null) {
            englishWord.id = englishWordsDao.addEnglishWord(englishWord).toLong()
        } else if (englishWords.size == 1) {
            englishWord.id = englishWords[0].id
        }
    }
}