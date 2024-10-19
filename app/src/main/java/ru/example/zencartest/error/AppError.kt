package ru.example.zencartest.error

enum class MsgError {
    USER_NOT_FOUND, USER_ALREADY_EXIST, PASSWORD_WRONG
}

sealed class AppError(val message: MsgError) {
    data object UserNotFound : AppError(MsgError.USER_NOT_FOUND)
    data object UserAlreadyExist : AppError(MsgError.USER_ALREADY_EXIST)
    data object WrongPassword : AppError(MsgError.PASSWORD_WRONG)
}