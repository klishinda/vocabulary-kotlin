package gq.learningEnglish.model

import gq.learningEnglish.common.SerializationType
import gq.learningEnglish.common.interfaces.DbEntity
import gq.learningEnglish.common.annotations.DbField

data class Word(
    @DbField("id")
    val id: Long? = null,
    @DbField("word")
    val name: String,
    @DbField(value = "language", type = SerializationType.TEXT)
    val language: AvailableLanguages,
    @DbField("description")
    val description: String? = null,
    @DbField("partOfSpeech")
    val partOfSpeech: Int
) : DbEntity