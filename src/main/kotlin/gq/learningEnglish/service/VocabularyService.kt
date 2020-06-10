package gq.learningEnglish.service

import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.dao.WordsDao
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class VocabularyService(
    private val wordsDao: WordsDao,
    private val vocabularyDao: VocabularyDao
) {

    fun addWord(newWord: Word): Long = wordsDao.addWord(newWord)

    fun addWordPair(firstWordId: Long, secondWordId: Long, userId: Int): Int {
        val firstWord = wordsDao.getWord(firstWordId)
        val secondWord = wordsDao.getWord(secondWordId)
        return vocabularyDao.addVocabularyRecord(firstWord, secondWord, userId);
    }
}