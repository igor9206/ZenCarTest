package ru.example.zencartest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.example.zencartest.model.UserModel
import ru.example.zencartest.util.SdfConverter
import ru.example.zencartest.viewmodel.UserViewModel
import java.time.OffsetDateTime

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val authUser = userViewModel.authUser.observeAsState().value
    val dataUsers = userViewModel.dataUsers.observeAsState(listOf())
    if (authUser != null) {
        LazyColumn(modifier = modifier) {
            item { Text("Main Screen", modifier = Modifier.padding(8.dp), fontSize = 24.sp) }

            item { CardAuthUser(authUser = authUser, logout = { userViewModel.logout() }) }

            items(dataUsers.value) { user ->
                CardUser(
                    authUser.registrationDate,
                    user
                ) { userViewModel.removeUser(user) }
            }
        }
    }
}

@Composable
fun CardAuthUser(
    modifier: Modifier = Modifier,
    authUser: UserModel,
    logout: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors().copy(containerColor = Color.LightGray),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = modifier.padding(8.dp)) {
            CustomTextField("User id: ${authUser.id}")
            CustomTextField("Login: ${authUser.login}")
            CustomTextField("Birthday: ${SdfConverter.formatFromOffsetDateTime(authUser.birthDate)}")
            CustomTextField("Password: ${authUser.password}")
            CustomTextField("Date registration: ${SdfConverter.formatFromOffsetDateTime(authUser.registrationDate)}")

            Button(
                onClick = logout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = "Logout")
            }
        }
    }
}

@Composable
fun CardUser(
    dateRegAuthUser: OffsetDateTime,
    user: UserModel,
    removeUser: () -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors().copy(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            CustomTextField("User id: ${user.id}")
            CustomTextField("Login: ${user.login}")
            CustomTextField("Birthday: ${SdfConverter.formatFromOffsetDateTime(user.birthDate)}")
            CustomTextField("Password: ${user.password}")
            CustomTextField("Date registration: ${SdfConverter.formatFromOffsetDateTime(user.registrationDate)}")

            if (dateRegAuthUser.isBefore(user.registrationDate)) {
                Button(
                    onClick = removeUser,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .padding(6.dp), fontSize = 16.sp
    )
}