package ru.example.zencartest.repository.user

import kotlinx.coroutines.flow.Flow
import ru.example.zencartest.model.UserModel

interface UserRepository {
    val authUserState: Flow<UserModel?>
    val dataUsers: Flow<List<UserModel>>

    fun logout()
    suspend fun removeUser(userModel: UserModel)
}