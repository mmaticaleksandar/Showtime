package presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun SystemBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
)