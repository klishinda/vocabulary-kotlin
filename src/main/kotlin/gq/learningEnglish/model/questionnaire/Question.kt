package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.annotations.DbField
import gq.learningEnglish.model.enums.AvailableLanguages

data class Question(
    @DbField(value = "asking_language")
    var askingLanguage: AvailableLanguages,
    @DbField(value = "asking_word_id")
    var askingWordId: Int,
    @DbField(value = "asking_word")
    var askingWord: String,
    @DbField
    var description: String?
)