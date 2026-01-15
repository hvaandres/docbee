package us.docbee.docbeeapp.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun convertMillisWithoutTimeZone(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val localDate = instant.toLocalDateTime(TimeZone.UTC).date

    val day = localDate.dayOfMonth.toString().padStart(2, '0')
    val month = localDate.monthNumber.toString().padStart(2, '0')
    val year = localDate.year

    return "$year / $month / $day"
}