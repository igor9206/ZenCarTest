package ru.example.zencartest.repository.auth

import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.auth.AuthApp
import ru.example.zencartest.db.dao.UserDao
import ru.example.zencartest.db.entity.UserEntity
import ru.example.zencartest.model.AuthModel
import ru.example.zencartest.model.UserModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApp: AuthApp,
    private val userDao: UserDao
) : AuthRepository {
    override val dataAuthState: StateFlow<AuthModel> = authApp.authState

    override suspend fun register(
        userModel: UserModel
    ) {
        val checkUser = userDao.getByLogin(userModel.login)
        if (checkUser == null) {
            userDao.insert(UserEntity.toEntity(userModel))
            authApp.setAuth(
                AuthModel(
                    id = userModel.id,
                    isUserLoggedIn = true,
                    user = userModel
                )
            )
        } else {
            println("пользователь уже зарегистрирован")
        }

    }

    override suspend fun signIn(
        login: String,
        password: String
    ) {
        val user = userDao.getByLogin(login)
        if (user != null) {
            if (user.password == password) {
                authApp.setAuth(
                    AuthModel(0, true, user.toModel())
                )
            } else {
                println("password incorrect")
            }
        } else {
            println("пользователь не найден")
        }
    }

    override fun logout() {
        authApp.removeAuth()
    }
}