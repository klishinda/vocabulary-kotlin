package gq.learningEnglish.service

import gq.learningEnglish.dao.RussianWordsDao
import gq.learningEnglish.model.Word
import org.springframework.stereotype.Service

@Service
class VocabularyService(private val russianWordsDao: RussianWordsDao) {
    fun addRussianWord(newWord: Word) : Int {
        return russianWordsDao.addRussianWord(newWord)
    }
}