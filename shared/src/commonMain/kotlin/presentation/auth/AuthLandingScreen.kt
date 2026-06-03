package presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AuthLandingScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 900.dp else maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(
                    top = if (isDesktop) 20.dp else 12.dp,
                    bottom = if (isDesktop) 20.dp else 96.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
            ) {
                if (isDesktop) {
                    Row(
                        modifier = Modifier.padding(32.dp),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AuthLandingIntro(
                            isDesktop = true,
                            modifier = Modifier.weight(1f)
                        )

                        AuthLandingActions(
                            isDesktop = true,
                            onLoginClick = onLoginClick,
                            onRegisterClick = onRegisterClick,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        AuthLandingIntro(
                            isDesktop = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        AuthLandingActions(
                            isDesktop = false,
                            onLoginClick = onLoginClick,
                            onRegisterClick = onRegisterClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthLandingIntro(
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (isDesktop) {
                12.dp
            } else {
                8.dp
            }
        )
    ) {
        Text(
            text = "Showtime",
            style = if (isDesktop) {
                MaterialTheme.typography.displaySmall
            } else {
                MaterialTheme.typography.headlineLarge
            },
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Browse movies, manage favorites, build your watchlist and play movie quizzes.",
            style = if (isDesktop) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isDesktop) {
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthLandingActions(
    isDesktop: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (isDesktop) {
                12.dp
            } else {
                10.dp
            }
        )
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create account")
        }
    }
}