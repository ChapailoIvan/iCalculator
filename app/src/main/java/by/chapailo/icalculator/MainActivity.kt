package by.chapailo.icalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import by.chapailo.icalculator.presentation.CalculationScreen
import by.chapailo.icalculator.presentation.CalculatorScreenTopBar
import by.chapailo.icalculator.presentation.CalculatorViewModel
import by.chapailo.icalculator.ui.theme.ICalculatorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ICalculatorTheme {
                val viewModel = viewModel<CalculatorViewModel>()

                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars),
                    topBar = { CalculatorScreenTopBar() },
                    snackbarHost = {
                        SnackbarHost(
                            modifier = Modifier.padding(WindowInsets.ime.asPaddingValues()),
                            hostState = snackbarHostState
                        )
                    }
                ) { padding ->
                    CalculationScreen(
                        modifier = Modifier
                            .padding(padding),
                        eventFlow = viewModel.eventFlow,
                        stateFlow = viewModel.stateFlow,
                        onAction = viewModel::handle,
                        showSnackbar = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message = message)
                            }
                        }
                    )
                }

            }
        }
    }
}
