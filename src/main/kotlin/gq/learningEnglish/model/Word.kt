package gq.learningEnglish.model

class Word() {
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var partOfSpeech: Int? = null

    constructor (name: String, partOfSpeech: Int) : this() {
        this.name = name
        this.partOfSpeech = partOfSpeech
    }
}