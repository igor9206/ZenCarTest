package ru.example.zencartest.repository.auth

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import ru.example.zencartest.auth.AuthApp
import ru.example.zencartest.db.dao.UserDao
import ru.example.zencartest.db.entity.UserEntity
import ru.example.zencartest.error.AppError
import ru.example.zencartest.model.AuthModel
import java.io.File
import java.io.FileOutputStream
import java.time.OffsetDateTime
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val authApp: AuthApp,
    private val userDao: UserDao
) : AuthRepository {
    override val dataAuthState: StateFlow<AuthModel> = authApp.authState

    override suspend fun register(
        login: String,
        password: String,
        birthDate: OffsetDateTime,
        avatar: Bitmap?
    ): Result<Unit> = runCatching {
        return try {
            val checkUser = userDao.getByLogin(login)
            if (checkUser != null) {
                error(AppError.UserAlreadyExist.message)
            }

            val avatarPath = avatar?.let { saveBitmapToFile(avatar, login) }

            userDao.insert(
                UserEntity(
                    id = 0,
                    login = login,
                    password = password,
                    birthDate = birthDate.toString(),
                    registrationDate = OffsetDateTime.now().toString(),
                    avatarPath = avatarPath
                )
            )
            setAuth(login)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(
        login: String,
        password: String
    ): Result<Unit> = runCatching {
        return try {
            val user = userDao.getByLogin(login)
            user ?: error(AppError.UserNotFound.message)
            if (user.password != password) {
                error(AppError.WrongPassword.message)
            }
            setAuth(login)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        authApp.removeAuth()
    }

    private suspend fun setAuth(login: String) {
        val user = userDao.getByLogin(login) ?: return
        authApp.setAuth(AuthModel(user.id, true, user.toModel()))
    }

    private suspend fun saveBitmapToFile(bitmap: Bitmap, filename: String): String {
        val file = File(context.filesDir, filename)
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }
        return file.absolutePath
    }
}