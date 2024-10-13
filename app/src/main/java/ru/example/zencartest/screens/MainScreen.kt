package ru.example.zencartest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.example.zencartest.R
import ru.example.zencartest.model.UserModel
import ru.example.zencartest.util.SdfConverter
import ru.example.zencartest.viewmodel.UserViewModel
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val authUser = userViewModel.authUser.observeAsState().value
    val dataUsers = userViewModel.dataUsers.observeAsState(listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(stringResource(R.string.main_screen)) }
            )
        }
    ) { innerPadding ->
        if (authUser != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                LazyColumn {
                    item {
                        CardAuthUser(
                            authUser
                        ) { userViewModel.logout() }
                    }

                    items(dataUsers.value) { user ->
                        CardUser(
                            authUser.registrationDate,
                            user
                        ) { userViewModel.removeUser(user) }
                    }
                }
            }
        }
    }
}

@Composable
fun CardAuthUser(
    authUser: UserModel,
    logout: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            CustomTextField("${stringResource(R.string.user_id)}: ${authUser.id}")
            CustomTextField("${stringResource(R.string.login)}: ${authUser.login}")
            CustomTextField(
                "${stringResource(R.string.birthday)}: ${
                    SdfConverter.formatFromOffsetDateTime(
                        authUser.birthDate
                    )
                }"
            )
            CustomTextField("${stringResource(R.string.password)}: ${authUser.password}")
            CustomTextField(
                "${stringResource(R.string.date_register)}: ${
                    SdfConverter.formatFromOffsetDateTime(
                        authUser.registrationDate
                    )
                }"
            )

            Button(
                onClick = logout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = stringResource(R.string.logout))
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
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            CustomTextField("${stringResource(R.string.login)}: ${user.login}")
            CustomTextField(
                "${stringResource(R.string.birthday)}: ${
                    SdfConverter.formatFromOffsetDateTime(
                        user.birthDate
                    )
                }"
            )
            CustomTextField(
                "${stringResource(R.string.date_register)}: ${
                    SdfConverter.formatFromOffsetDateTime(
                        user.registrationDate
                    )
                }"
            )

            if (dateRegAuthUser.isBefore(user.registrationDate)) {
                Button(
                    onClick = removeUser,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.delete))
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