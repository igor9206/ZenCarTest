package ru.example.zencartest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.zencartest.db.dao.UserDao
import ru.example.zencartest.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
}