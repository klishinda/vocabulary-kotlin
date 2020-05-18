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

    fun addEnglishWord(newWord: Word) : Int {
        return englishWordsDao.addEnglishWord(newWord)
    }

    fun addWordPair(newRussianWord: Word, newEnglishWord: Word, userId: Int) : Int {
        russianWordsDao.getRussianWord(newRussianWord.name).run{
            if (this == null) {
                newRussianWord.id = russianWordsDao.addRussianWord(newRussianWord).toLong()
            } else {
                newRussianWord.id = id
            }
        }
        englishWordsDao.getEnglishWord(newEnglishWord.name).run{
            if (this == null) {
                newEnglishWord.id = englishWordsDao.addEnglishWord(newEnglishWord).toLong()
            } else {
                newEnglishWord.id = id
            }
        }
        println("rusId: "+newRussianWord.id+", engId: "+newEnglishWord.id)
        return vocabularyDao.addVocabularyRecord(newRussianWord, newEnglishWord, userId);
    }
}