package presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import presentation.auth.mvi.AuthIntent
import presentation.auth.mvi.AuthState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import presentation.common.formFieldNavigation
import androidx.compose.ui.platform.LocalFocusManager
import presentation.common.formFieldNavigation

@Composable
fun RegisterScreen(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(
                    top = if (isDesktop) 20.dp else 12.dp,
                    bottom = if (isDesktop) 24.dp else 96.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthCard(
                title = "Create account",
                subtitle = "Register to save favorites, watchlist movies and quiz statistics.",
                isDesktop = isDesktop
            ) {
                OutlinedTextField(
                    value = state.fullName,
                    onValueChange = {
                        onIntent(AuthIntent.FullNameChanged(it))
                    },
                    label = {
                        Text("Full name")
                    },
                    enabled = !state.isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .formFieldNavigation(
                            focusManager = focusManager
                        )
                )

                OutlinedTextField(
                    value = state.username,
                    onValueChange = {
                        onIntent(AuthIntent.UsernameChanged(it))
                    },
                    label = {
                        Text("Username")
                    },
                    supportingText = {
                        Text("At least 3 characters: letters, numbers or underscore.")
                    },
                    enabled = !state.isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .formFieldNavigation(
                            focusManager = focusManager
                        )
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onIntent(AuthIntent.PasswordChanged(it))
                    },
                    label = {
                        Text("Password")
                    },
                    supportingText = {
                        Text("At least 8 characters.")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !state.isLoading,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onIntent(AuthIntent.RegisterClicked)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .formFieldNavigation(
                            focusManager = focusManager,
                            onSubmit = {
                                focusManager.clearFocus()
                                onIntent(AuthIntent.RegisterClicked)
                            }
                        )
                )

                AuthLoadingButton(
                    text = "Register",
                    loadingText = "Creating account...",
                    isLoading = state.isLoading,
                    onClick = {
                        onIntent(AuthIntent.RegisterClicked)
                    }
                )

                TextButton(
                    onClick = onLoginClick,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? Login")
                }

                OutlinedButton(
                    onClick = onBackClick,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }

                state.errorMessage?.let { message ->
                    AuthErrorBanner(message = message)
                }
            }
        }
    }
}