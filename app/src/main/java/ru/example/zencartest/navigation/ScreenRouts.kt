package ru.example.zencartest.navigation

sealed class ScreenRouts(val route: String) {
    object AuthGraph : ScreenRouts("auth_graph")

    object AuthScreen : ScreenRouts("auth_screen")
    object MainScreen : ScreenRouts("main_screen")
}