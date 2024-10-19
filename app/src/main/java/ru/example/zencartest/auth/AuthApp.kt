package ru.example.zencartest.auth

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.example.zencartest.model.AuthModel
import ru.example.zencartest.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApp @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    private val idKey = "id"
    private val isUserLoggedInKey = "isUserLoggedIn"
    private val userKey = "user"

    private val _authState = MutableStateFlow(
        prefs.getAuthModel()
    )
    val authState: StateFlow<AuthModel> = _authState

    @Synchronized
    fun setAuth(authModel: AuthModel) {
        _authState.value = authModel
        prefs.saveAuthModel(authModel)
    }

    @Synchronized
    fun removeAuth() {
        _authState.value = AuthModel()
        prefs.edit().clear().apply()
    }

    private fun SharedPreferences.saveAuthModel(authModel: AuthModel) {
        edit().apply {
            putInt(idKey, authModel.id)
            putBoolean(isUserLoggedInKey, authModel.isUserLoggedIn)
            putString(userKey, Gson().toJson(authModel.user))
            apply()
        }
    }

    private fun SharedPreferences.getAuthModel(): AuthModel {
        val id = getInt(idKey, 0)
        val isUserLoggedIn = getBoolean(isUserLoggedInKey, false)
        val userJson = getString(userKey, null)
        val user = if (userJson != null) Gson().fromJson(userJson, UserModel::class.java) else null
        return AuthModel(id, isUserLoggedIn, user)
    }
}