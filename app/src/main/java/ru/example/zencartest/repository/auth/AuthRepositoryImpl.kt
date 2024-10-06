package ru.example.zencartest.repository.auth

import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.auth.AuthApp
import ru.example.zencartest.db.dao.UserDao
import ru.example.zencartest.model.AuthModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApp: AuthApp,
    private val userDao: UserDao
) : AuthRepository {
    override val dataAuthState: StateFlow<AuthModel> = authApp.authState
}