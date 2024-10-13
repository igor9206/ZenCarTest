package ru.example.zencartest.navigation

sealed class ScreenRouts(val route: String) {
    data object AuthGraph : ScreenRouts("auth_graph")

    data object AuthScreen : ScreenRouts("auth_screen")
    data object MainScreen : ScreenRouts("main_screen")
}