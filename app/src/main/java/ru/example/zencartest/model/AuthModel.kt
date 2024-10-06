package ru.example.zencartest.model

data class AuthModel(
    val id: Int = 0,
    val isUserLoggedIn: Boolean = false,
    val user: UserModel? = null
)