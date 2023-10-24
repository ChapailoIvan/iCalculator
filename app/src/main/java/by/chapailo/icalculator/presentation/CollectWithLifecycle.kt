package by.chapailo.icalculator.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

@Composable
fun CollectWithLifecycle(
    collectBlock: suspend () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                collectBlock()
            }
        }
    }
}