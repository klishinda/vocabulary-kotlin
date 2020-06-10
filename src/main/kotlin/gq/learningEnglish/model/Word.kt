package gq.learningEnglish.model

import gq.learningEnglish.common.DbField

data class Word(
    val id: Long? = null,
    @DbField("word")
    val name: String,
    val language: String,
    val description: String? = null,
    val partOfSpeech: Int
)