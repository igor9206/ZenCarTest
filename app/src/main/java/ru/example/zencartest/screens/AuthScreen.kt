package ru.example.zencartest.screens

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.example.zencartest.R
import ru.example.zencartest.util.SdfConverter
import ru.example.zencartest.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {
    val mainItemModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = mainItemModifier,
            fontSize = 24.sp,
            text = stringResource(R.string.welcome)
        )

        Text(
            modifier = mainItemModifier,
            fontSize = 16.sp,
            text = if (authViewModel.isLoginScreen)
                stringResource(R.string.welcome_sign_in)
            else
                stringResource(R.string.welcome_register)
        )

        OutlinedTextField(
            modifier = mainItemModifier,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_person_24),
                    contentDescription = null
                )
            },
            value = authViewModel.fields.login.value,
            onValueChange = { authViewModel.updateLogin(it) },
            label = { Text(stringResource(R.string.login)) },
            singleLine = true,
            supportingText = {
                if (authViewModel.fields.login.isError) Text(text = authViewModel.fields.login.errorMsg)
            },
            isError = authViewModel.fields.login.isError
        )

        if (!authViewModel.isLoginScreen) {
            BirthDateField(modifier = mainItemModifier, authViewModel = authViewModel)
        }

        OutlinedTextField(
            modifier = mainItemModifier,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_lock_24),
                    contentDescription = null
                )
            },
            value = authViewModel.fields.password.value,
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.ic_visibility_off_24)
                else
                    painterResource(R.drawable.ic_visibility_24)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = { authViewModel.updatePassword(it) },
            label = { Text(stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            supportingText = {
                if (authViewModel.fields.password.isError) Text(text = authViewModel.fields.password.errorMsg)
            },
            isError = authViewModel.fields.password.isError
        )

        Button(
            modifier = mainItemModifier,
            onClick = { authViewModel.singInOrRegister() }
        ) {
            Text(
                if (authViewModel.isLoginScreen) stringResource(R.string.sign_in) else stringResource(
                    R.string.register
                )
            )
        }

        TextButton(
            modifier = mainItemModifier,
            onClick = { authViewModel.toggleScreen() }) {
            Text(
                if (authViewModel.isLoginScreen)
                    stringResource(R.string.no_account)
                else
                    stringResource(R.string.already_have_account)
            )
        }
    }
}

@Composable
fun BirthDateField(
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    showDatePicker = true
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }

    OutlinedTextField(
        modifier = modifier,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_calendar_month_24),
                contentDescription = null
            )
        },
        value = authViewModel.fields.birthDate.value.let {
            if (it.isBlank()) "" else SdfConverter.convertMillisToDate(it.toLong())
        },
        onValueChange = { authViewModel.updateBirthDate(it) },
        label = { Text(stringResource(R.string.birth_date)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        supportingText = {
            if (authViewModel.fields.birthDate.isError) Text(text = authViewModel.fields.birthDate.errorMsg)
        },
        isError = authViewModel.fields.birthDate.isError,
        interactionSource = interactionSource,
        readOnly = true,
    )

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                if (millis != null) {
                    authViewModel.updateBirthDate(millis.toString())
                    showDatePicker = false
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}