package ru.example.zencartest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.example.zencartest.screens.MainScreen
import ru.example.zencartest.viewmodel.AuthViewModel

@Composable
fun RootGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val authState by authViewModel.dataAuthUser.collectAsState()
    val startDestination = if (authState.isUserLoggedIn)
        ScreenRouts.MainScreen.route else ScreenRouts.AuthGraph.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        authGraph(navController, authViewModel)
        composable(ScreenRouts.MainScreen.route) { MainScreen() }
    }
}