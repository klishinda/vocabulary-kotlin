package gq.learningEnglish.service

import gq.learningEnglish.dao.CommonDao
import gq.learningEnglish.dao.VocabularyDao
import gq.learningEnglish.dao.WordsDao
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class VocabularyService(
    private val wordsDao: WordsDao,
    private val vocabularyDao: VocabularyDao,
    private val commonDao: CommonDao
) {

    fun addWord(newWord: Word, user: String): Word {
        val userId = commonDao.getUserId(user)
        return wordsDao.addWord(newWord, userId)
    }

    fun addWordPair(firstWordId: Long, secondWordId: Long, user: String): Long {
        val userId = commonDao.getUserId(user)
        val firstWord = wordsDao.getWord(firstWordId, userId)
        val secondWord = wordsDao.getWord(secondWordId, userId)
        return vocabularyDao.addVocabularyRecord(firstWord, secondWord, userId)
    }

    fun addWordsAndPair(newWords: List<Word>, user: String): Long {
        val userId = commonDao.getUserId(user)
        val (firstWord, secondWord) = Pair(wordsDao.addWord(newWords[0], userId), wordsDao.addWord(newWords[1], userId))
        return vocabularyDao.addVocabularyRecord(firstWord, secondWord, userId)
    }

    fun getTranslate(wordId: Long, user: String): List<Word>? {
        val userId = commonDao.getUserId(user)
        return vocabularyDao.getTranslate(wordId, userId)
    }

    fun getUnusedWords(): List<Word>? = vocabularyDao.getUnusedWords()
}
