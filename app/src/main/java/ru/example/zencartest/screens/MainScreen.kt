package ru.example.zencartest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.example.zencartest.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Main Screen")
        Button(onClick = { authViewModel.logout() }) {
            Text("exit account")
        }
    }
}