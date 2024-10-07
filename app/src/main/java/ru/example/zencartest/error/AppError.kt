package ru.example.zencartest.error

sealed class AppError(val message: String) {
    object UserNotFound : AppError("user not found")
    object UserAlreadyRegistered : AppError("user already registered")
    object WrongPassword : AppError("wrong password")
}