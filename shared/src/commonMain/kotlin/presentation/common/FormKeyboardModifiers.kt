package presentation.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

fun Modifier.formFieldNavigation(
    focusManager: FocusManager,
    onSubmit: (() -> Unit)? = null
): Modifier {
    return this.onPreviewKeyEvent { event ->
        if (event.type != KeyEventType.KeyDown) {
            return@onPreviewKeyEvent false
        }

        when (event.key) {
            Key.Tab -> {
                focusManager.moveFocus(FocusDirection.Next)
                true
            }

            Key.Enter, Key.NumPadEnter -> {
                if (onSubmit != null) {
                    onSubmit()
                    true
                } else {
                    focusManager.moveFocus(FocusDirection.Next)
                    true
                }
            }

            else -> false
        }
    }
}