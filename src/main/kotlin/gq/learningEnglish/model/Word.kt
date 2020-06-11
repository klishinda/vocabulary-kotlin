package gq.learningEnglish.model

import gq.learningEnglish.common.interfaces.DbEntity
import gq.learningEnglish.common.annotations.DbField

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