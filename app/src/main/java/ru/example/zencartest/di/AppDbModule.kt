package ru.example.zencartest.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.example.zencartest.db.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppDbModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(appDb: AppDb) = appDb.userDao()
}