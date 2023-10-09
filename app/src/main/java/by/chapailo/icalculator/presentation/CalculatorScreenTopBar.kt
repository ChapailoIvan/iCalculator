package by.chapailo.icalculator.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreenTopBar() {
    LargeTopAppBar(
        title = { Text(text = "iCalculator") }
    )
}