package gq.learningEnglish.model.mapper

import gq.learningEnglish.common.KotlinRowMapper
import gq.learningEnglish.model.questionnaire.Answer
import gq.learningEnglish.model.questionnaire.Question
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class QuestionnaireMapper : RowMapper<Map<Question, List<Answer>>> {

    override fun mapRow(rs: ResultSet, rownum: Int): Map<Question, List<Answer>> {
        val quizMap = mutableMapOf<Question, MutableList<Answer>>()

        do {
            val question = KotlinRowMapper(Question::class).mapRow(rs, rownum)
            val answer = KotlinRowMapper(Answer::class).mapRow(rs, rownum)

            if (quizMap[question] == null) {
                quizMap[question] = mutableListOf(answer)
            } else {
                quizMap[question]!!.add(answer)
            }
        } while (rs.next())
        return quizMap
    }
}