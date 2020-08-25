package gq.learningEnglish.model

import gq.learningEnglish.common.SerializationType
import gq.learningEnglish.common.interfaces.DbEntity
import gq.learningEnglish.common.annotations.DbField
import gq.learningEnglish.model.enums.AvailableLanguages

data class Word(
    @DbField
    val id: Long? = null,
    @DbField("word")
    val name: String,
    @DbField(value = "language", type = SerializationType.TEXT)
    val language: AvailableLanguages,
    @DbField
    val description: String? = null,
    @DbField(value = "part_of_speech")
    val partOfSpeech: Int? = null
) : DbEntity