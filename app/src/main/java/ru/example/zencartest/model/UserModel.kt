package ru.example.zencartest.model

import java.time.OffsetDateTime

data class UserModel(
    val id: Int,
    val login: String,
    val birthDate: OffsetDateTime,
    val password: String,
    val registrationDate: OffsetDateTime = OffsetDateTime.now(),
    val avatarPath: String? = null
)