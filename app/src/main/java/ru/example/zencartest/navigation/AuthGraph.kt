package ru.example.zencartest.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.example.zencartest.screens.AuthScreen
import ru.example.zencartest.viewmodel.AuthViewModel

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    navigation(
        route = ScreenRouts.AuthGraph.route,
        startDestination = ScreenRouts.AuthScreen.route
    ) {
        composable(ScreenRouts.AuthScreen.route) {
            AuthScreen(authViewModel = authViewModel)
        }
    }
}