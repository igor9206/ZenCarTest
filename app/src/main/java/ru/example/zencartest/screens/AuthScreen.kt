package ru.example.zencartest.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthScreen(
    navigateToMainScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text("Auth Screen")
}