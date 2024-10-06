package ru.example.zencartest.repository.auth

import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.model.AuthModel
import ru.example.zencartest.model.UserModel

interface AuthRepository {
    val dataAuthState: StateFlow<AuthModel>

    suspend fun register(userModel: UserModel)
    suspend fun signIn(login: String, password: String)
    fun logout()
}