package gq.learningEnglish.model.questionnaire

import gq.learningEnglish.common.infrastructure.annotations.DbField
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

data class Answer(
    @DbField(value = "vocabulary_id")
    val vocabularyId: Long,
    @DbField(value = "answer_word_id")
    val answerWordId: Long,
    @DbField(value = "answer_word")
    val answerWord: String
) {
    var userAnswer: String? = null
    var result: Boolean = false

    fun setAnswerHistoryMap(launchId: Long, askingWordId: Long): MapSqlParameterSource {
        return MapSqlParameterSource(
            mapOf(
                "launchId" to launchId,
                "vocabularyId" to vocabularyId,
                "askingWord" to askingWordId,
                "userAnswer" to userAnswer,
                "result" to result
            )
        )
    }
}
