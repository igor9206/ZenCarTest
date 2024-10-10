package ru.example.zencartest.repository.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.example.zencartest.auth.AuthApp
import ru.example.zencartest.db.dao.UserDao
import ru.example.zencartest.model.UserModel
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authApp: AuthApp
) : UserRepository {
    override val authUserState: Flow<UserModel?> = authApp.authState.map { it.user }

    override val dataUsers: Flow<List<UserModel>> = userDao.getAll().map { list ->
        list.map { entity -> entity.toModel() }
            .sortedByDescending { it.registrationDate }
    }

    override fun logout() {
        authApp.removeAuth()
    }

    override suspend fun removeUser(userModel: UserModel) {
        userDao.removeById(userModel.id)
    }
}