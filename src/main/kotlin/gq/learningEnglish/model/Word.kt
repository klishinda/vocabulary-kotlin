package gq.learningEnglish.model

class Word() {
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var partOfSpeech: Int? = null

    constructor (id: Long, name: String, partOfSpeech: Int) : this() {
        this.id = id
        this.name = name
        this.partOfSpeech = partOfSpeech
    }
}