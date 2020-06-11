package gq.learningEnglish.model

import gq.learningEnglish.common.DbEntity
import gq.learningEnglish.common.DbField

data class Word(
    @DbField("id")
    val id: Long? = null,
    @DbField("word")
    val name: String,
    @DbField("language")
    val language: String,
    @DbField("description")
    val description: String? = null,
    @DbField("partOfSpeech")
    val partOfSpeech: Int
) : DbEntity