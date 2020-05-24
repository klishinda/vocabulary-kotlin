package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.DbField
import gq.learningEnglish.model.AvailableLanguages

data class Question (
    @DbField(value = "asking_language")
    //var askingLanguage: AvailableLanguages? = null
    var askingLanguage: String,
    @DbField(value = "asking_word_id")
    var askingWordId : Int,
    @DbField(value = "asking_word")
    var askingWord: String,
    @DbField(value = "description")
    var description: String?
)