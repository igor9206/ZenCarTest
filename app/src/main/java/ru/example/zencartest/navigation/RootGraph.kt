package ru.example.zencartest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.example.zencartest.screens.MainScreen

@Composable
fun RootGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        authGraph(navController)
        composable(ScreenRouts.MainScreen.route) { MainScreen() }
    }
}