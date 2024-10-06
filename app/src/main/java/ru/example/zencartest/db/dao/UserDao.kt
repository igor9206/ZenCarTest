package ru.example.zencartest.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.example.zencartest.db.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getByLogin(login: String): UserEntity?

    @Insert
    suspend fun save(userEntity: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun removeById(id: Long)
}