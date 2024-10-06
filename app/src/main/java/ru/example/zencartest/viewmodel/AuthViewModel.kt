package ru.example.zencartest.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.example.zencartest.model.AuthModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

) : ViewModel() {

    val dataAuthUser = mutableStateOf(
        AuthModel(
            id = 1,
            isUserLoggedIn = false,
            user = null
        )
    )
}