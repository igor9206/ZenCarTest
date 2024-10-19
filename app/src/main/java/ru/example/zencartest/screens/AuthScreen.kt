package ru.example.zencartest.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    val trailIconPass =
        if (passwordVisible) R.drawable.ic_visibility_off_24 else R.drawable.ic_visibility_24
    val toastMessage by authViewModel.toastMessage.collectAsState()
    val imgScreen =
        if (authViewModel.isLoginScreen) R.drawable.ic_sign_in_24 else R.drawable.ic_sign_up_24
    val headScreen =
        if (authViewModel.isLoginScreen) R.string.sign_in else R.string.create_an_account
    val textButtonSignOrRegister =
        if (authViewModel.isLoginScreen) R.string.sign_in else R.string.register
    val textButtonChangeScreen =
        if (authViewModel.isLoginScreen) R.string.no_account else R.string.already_have_account

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            authViewModel.showToast(null)
        }
    }

    val scrollState = rememberScrollState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        if (!authViewModel.isLoginScreen) {
            IndeterminateCircularIndicator(authViewModel.loading)
        }

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(imgScreen),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(164.dp)
                    .padding(top = 4.dp),
            )

            if (authViewModel.isLoginScreen) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    text = stringResource(headScreen)
                )
            }

            if (!authViewModel.isLoginScreen) {
                SelectAvatarButton(authViewModel = authViewModel)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_person_24),
                        contentDescription = null
                    )
                },
                value = authViewModel.login.value,
                onValueChange = { authViewModel.updateLogin(it) },
                label = { Text(stringResource(R.string.login)) },
                singleLine = true,
                supportingText = {
                    if (authViewModel.login.isError)
                        Text(text = authViewModel.login.errorMsg)
                },
                isError = authViewModel.login.isError
            )

            if (!authViewModel.isLoginScreen) {
                BirthDateField(authViewModel = authViewModel)
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(trailIconPass),
                            contentDescription = null
                        )
                    }
                },
                value = authViewModel.password.value,
                onValueChange = { authViewModel.updatePassword(it) },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                supportingText = {
                    if (authViewModel.password.isError)
                        Text(text = authViewModel.password.errorMsg)
                },
                isError = authViewModel.password.isError
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = { authViewModel.signInOrRegister() }
            ) {
                Text(stringResource(textButtonSignOrRegister))
            }

            Spacer(modifier = Modifier.padding(4.dp))

            TextButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = { authViewModel.toggleScreen() }
            ) {
                Text(stringResource(textButtonChangeScreen))
            }
        }
    }
}

@Composable
fun BirthDateField(
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_calendar_month_24),
                contentDescription = null
            )
        },
        value = authViewModel.birthDate.value.let {
            if (it.isBlank()) "" else SdfConverter.convertMillisToDate(it.toLong())
        },
        onValueChange = { authViewModel.updateBirthDate(it) },
        label = { Text(stringResource(R.string.birth_date)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        supportingText = {
            if (authViewModel.birthDate.isError) Text(text = authViewModel.birthDate.errorMsg)
        },
        isError = authViewModel.birthDate.isError,
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

@Composable
fun SelectAvatarButton(
    authViewModel: AuthViewModel
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            authViewModel.updateAvatar(uri)
        }

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        contentAlignment = Alignment.Center
    ) {
        authViewModel.avatar?.let { img ->
            Image(
                bitmap = img.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { authViewModel.updateAvatar(null) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
            }
        } ?: run {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(loading: Boolean) {
    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}