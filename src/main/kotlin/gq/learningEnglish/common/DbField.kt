package gq.learningEnglish.common

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DbField(
    val value: String = "",
    val updatable: Boolean = false,
    val insertable: Boolean = false,
    val type: SerializationType = SerializationType.UNDEFINED
)