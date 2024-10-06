package ru.example.zencartest.repository.auth

import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.model.AuthModel

interface AuthRepository {
    val dataAuthState: StateFlow<AuthModel>
}