package ru.example.zencartest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.example.zencartest.model.UserModel
import ru.example.zencartest.repository.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val authUser = userRepository.authUserState.asLiveData()

    val dataUsers = userRepository.dataUsers.asLiveData()

    fun logout() {
        userRepository.logout()
    }

    fun removeUser(user: UserModel) = viewModelScope.launch {
        userRepository.removeUser(user)
    }
}