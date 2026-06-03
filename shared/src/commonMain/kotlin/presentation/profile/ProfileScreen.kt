package presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.profile.mvi.ProfileState

@Composable
fun ProfileScreen(
    state: ProfileState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 1100.dp else maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(top = if (isDesktop) 20.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
            ) {
                ProfileHeader(
                    onBackClick = onBackClick
                )

                Spacer(Modifier.height(16.dp))
            }

            Box(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    state.isLoading && state.fullName.isBlank() && state.username.isBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.errorMessage != null && state.fullName.isBlank() && state.username.isBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (state.isOffline) {
                                    "You are offline. Profile data could not be loaded."
                                } else {
                                    state.errorMessage
                                },
                                color = if (state.isOffline) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    else -> {
                        ProfileContent(
                            state = state,
                            isDesktop = isDesktop,
                            onLogoutClick = onLogoutClick
                        )
                    }
                }
            }
        }
    }
}