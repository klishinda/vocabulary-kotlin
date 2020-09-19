package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.annotations.DbField

data class Answer(
    @DbField(value = "vocabulary_id")
    val vocabularyId: Int,
    @DbField(value = "answer_word_id")
    val answerWordId: Int,
    @DbField(value = "answer_word")
    val answerWord: String?
) {
    var userAnswer: String? = null
    var result: Boolean = false
}