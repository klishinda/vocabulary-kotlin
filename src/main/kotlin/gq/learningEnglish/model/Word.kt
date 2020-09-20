package gq.learningEnglish.model

import gq.learningEnglish.common.infrastructure.SerializationType
import gq.learningEnglish.common.infrastructure.interfaces.DbEntity
import gq.learningEnglish.common.infrastructure.annotations.DbField
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