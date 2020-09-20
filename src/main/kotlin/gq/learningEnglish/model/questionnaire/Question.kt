package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.infrastructure.annotations.DbField
import gq.learningEnglish.model.enums.AvailableLanguages

data class Question(
    @DbField(value = "asking_language")
    val askingLanguage: AvailableLanguages,
    @DbField(value = "asking_word_id")
    val askingWordId: Long,
    @DbField(value = "asking_word")
    val askingWord: String,
    @DbField
    val description: String?
)
