package gq.learningEnglish.common.infrastructure

import java.math.BigDecimal
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KClass

val SERIALIZABLE_CLASSES: Set<KClass<*>> = setOf(
    Long::class,
    Int::class,
    String::class,
    Boolean::class,
    Byte::class,
    ByteArray::class,
    Short::class,
    Double::class,
    Float::class,
    BigDecimal::class,
    Timestamp::class,
    Time::class,
    Date::class,
    LocalDate::class,
    LocalTime::class,
    LocalDateTime::class
)
