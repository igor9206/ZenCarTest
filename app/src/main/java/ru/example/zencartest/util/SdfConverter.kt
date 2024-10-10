package ru.example.zencartest.util

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale

object SdfConverter {
    private const val PATTERN = "dd.MM.yyyy"

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat(PATTERN, Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun formatFromOffsetDateTime(offsetDateTime: OffsetDateTime): String {
        val date = Date.from(offsetDateTime.toInstant())
        val formatter = SimpleDateFormat(PATTERN, Locale.getDefault())
        return formatter.format(date)
    }
}