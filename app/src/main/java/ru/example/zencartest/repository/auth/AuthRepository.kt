package ru.example.zencartest.repository.auth

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.model.AuthModel
import java.time.OffsetDateTime

interface AuthRepository {
    val dataAuthState: StateFlow<AuthModel>

    suspend fun register(
        login: String,
        password: String,
        birthDate: OffsetDateTime,
        avatar: Bitmap?
    ): Result<Unit>

    suspend fun signIn(login: String, password: String): Result<Unit>
    fun logout()
}