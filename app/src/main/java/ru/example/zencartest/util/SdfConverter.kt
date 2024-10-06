package ru.example.zencartest.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SdfConverter {
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}