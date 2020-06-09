package gq.learningEnglish.model

data class Word(
    var id: Long? = null,
    val name: String,
    var description: String? = null,
    val partOfSpeech: Int
)