package ru.example.zencartest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.example.zencartest.navigation.RootGraph
import ru.example.zencartest.navigation.ScreenRouts
import ru.example.zencartest.ui.theme.ZenCarTestTheme
import ru.example.zencartest.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZenCarTestTheme {
                val autViewModel: AuthViewModel = hiltViewModel()
                val authState by autViewModel.dataAuthUser.collectAsState()
                val startDestination =
                    if (authState.isUserLoggedIn)
                        ScreenRouts.MainScreen.route
                    else
                        ScreenRouts.AuthGraph.route

                RootGraph(startDestination = startDestination)
            }
        }
    }
}