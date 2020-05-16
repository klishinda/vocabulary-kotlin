package gq.learningEnglish.model

data class Word(
        val name: String,
        val partOfSpeech: Int,
        val description: String?
) {
    var id: Long? = null
}