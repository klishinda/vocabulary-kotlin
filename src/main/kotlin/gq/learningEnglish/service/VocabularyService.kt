package gq.learningEnglish.service

import gq.learningEnglish.dao.EnglishWordsDao
import gq.learningEnglish.dao.RussianWordsDao
import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.dao.WordsDao
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class VocabularyService(
    private val russianWordsDao: RussianWordsDao,
    private val englishWordsDao: EnglishWordsDao,
    private val wordsDao: WordsDao,
    private val vocabularyDao: VocabularyDao
) {
    fun addRussianWord(newWord: Word) : Int {
        return russianWordsDao.addRussianWord(newWord)
    }

    fun addEnglishWord(newWord: Word) : Int {
        return englishWordsDao.addEnglishWord(newWord)
    }

    fun addWord(newWord: Word) : Long {
        return wordsDao.addWord(newWord)
    }

    fun addWordPair(firstWordId: Long, secondWordId: Long, userId: Int) : Int {
        val firstWord = wordsDao.getWord(firstWordId)
        val secondWord = wordsDao.getWord(secondWordId)
        println("$firstWord $secondWord")
        return vocabularyDao.addVocabularyRecord(firstWord, secondWord, userId);
    }
}