package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.annotations.DbField

data class Answer(
    @DbField(value = "vocabulary_id")
    var vocabularyId: Int,
    @DbField(value = "answer_word_id")
    var answerWordId: Int,
    @DbField(value = "answer_word")
    var answerWord: String?
) {
    var result: Boolean = false
}