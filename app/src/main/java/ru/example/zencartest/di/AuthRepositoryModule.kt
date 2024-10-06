package ru.example.zencartest.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.example.zencartest.repository.auth.AuthRepository
import ru.example.zencartest.repository.auth.AuthRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}