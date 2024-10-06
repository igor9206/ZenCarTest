package ru.example.zencartest.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.example.zencartest.screens.AuthScreen

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = ScreenRouts.AuthGraph.route,
        startDestination = ScreenRouts.AuthScreen.route
    ) {
        composable(ScreenRouts.AuthScreen.route) {
            AuthScreen(navigateToMainScreen = {
                navController.navigate(ScreenRouts.MainScreen.route) {
                    popUpTo(ScreenRouts.AuthGraph.route) { inclusive = true }
                }
            })
        }
    }
}