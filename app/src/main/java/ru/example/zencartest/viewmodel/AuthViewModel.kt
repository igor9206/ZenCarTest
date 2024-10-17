package ru.example.zencartest.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.zencartest.R
import ru.example.zencartest.error.MsgError
import ru.example.zencartest.repository.auth.AuthRepository
import java.time.OffsetDateTime
import java.util.Date
import javax.inject.Inject

data class AuthFields(
    val login: FieldState,
    val birthDate: FieldState,
    val password: FieldState
)

data class FieldState(
    val value: String = "",
    val isError: Boolean = false,
    val errorMsg: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext
    private val context: Context
) : ViewModel() {
    var loading by mutableStateOf(false)
        private set

    val dataAuthUser = authRepository.dataAuthState

    var isLoginScreen by mutableStateOf(true)
        private set

    var fields by mutableStateOf(
        AuthFields(
            login = FieldState(),
            birthDate = FieldState(),
            password = FieldState()
        )
    )
        private set

    var avatar by mutableStateOf<Bitmap?>(null)
        private set

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    fun showToast(message: String?) {
        val msg = when (message) {
            MsgError.USER_ALREADY_EXIST.toString() -> context.getString(R.string.user_already_exist)
            MsgError.USER_NOT_FOUND.toString() -> context.getString(R.string.user_not_found)
            MsgError.PASSWORD_WRONG.toString() -> context.getString(R.string.wrong_password)
            null -> null
            else -> context.getString(R.string.unknown_error)
        }
        _toastMessage.value = msg
    }

    private fun updateLoadState(state: Boolean) {
        loading = state
    }

    fun toggleScreen() {
        isLoginScreen = !isLoginScreen
        resetFields()
    }

    fun updateLogin(input: String) {
        fields = fields.copy(login = fields.login.copy(value = input))
        if (fields.login.isError) {
            checkFields()
        }
    }

    fun updateBirthDate(input: String) {
        fields = fields.copy(birthDate = fields.birthDate.copy(value = input))
        if (fields.birthDate.isError) {
            checkFields()
        }
    }

    fun updatePassword(input: String) {
        fields = fields.copy(password = fields.password.copy(value = input))
        if (fields.password.isError) {
            checkFields()
        }
    }

    fun updateAvatar(uri: Uri?) {
        if (uri == null) {
            avatar = null
            return
        }

        context.contentResolver.openInputStream(uri).use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            avatar = bitmap
        }
    }

    fun signInOrRegister() {
        checkFields()
        if (isLoginScreen) signIn() else register()
    }

    private fun signIn() = viewModelScope.launch {
        if (fields.login.isError || fields.password.isError) {
            return@launch
        }
        authRepository.signIn(fields.login.value, fields.password.value)
            .onSuccess {
                delay(2000)
                resetFields()
            }
            .onFailure { showToast(it.message) }
    }

    private fun register() = viewModelScope.launch {
        if (fields.login.isError || fields.birthDate.isError || fields.password.isError) {
            return@launch
        }
        updateLoadState(true)

        authRepository.register(
            login = fields.login.value,
            password = fields.password.value,
            birthDate = Date(fields.birthDate.value.toLong()).toInstant()
                .atOffset(OffsetDateTime.now().offset),
            avatar = avatar
        ).onSuccess {
            updateLoadState(false)
            delay(2000)
            resetFields()
            isLoginScreen = true
        }.onFailure {
            updateLoadState(false)
            showToast(it.message)
        }
    }

    private fun checkFields() {
        loginIsValid()
        birthDateIsValid()
        passwordIsValid()
    }

    private fun loginIsValid() {
        val isLoginBlank = fields.login.value.isBlank()
        fields = fields.copy(
            login = fields.login.copy(
                isError = isLoginBlank,
                errorMsg = if (isLoginBlank) context.getString(R.string.empty_field) else ""
            )
        )
    }

    private fun birthDateIsValid() {
        val isBirthDateBlank = fields.birthDate.value.isBlank()
        fields = fields.copy(
            birthDate = fields.birthDate.copy(
                isError = isBirthDateBlank,
                errorMsg = if (isBirthDateBlank) context.getString(R.string.empty_field) else ""
            )
        )
    }

    private fun passwordIsValid() {
        val isPasswordBlank = fields.password.value.isBlank()
        fields = fields.copy(
            password = fields.password.copy(
                isError = isPasswordBlank,
                errorMsg = if (isPasswordBlank) context.getString(R.string.empty_field) else ""
            )
        )
    }

    private fun resetFields() {
        fields = AuthFields(
            login = FieldState(),
            birthDate = FieldState(),
            password = FieldState()
        )
        avatar = null
    }
}