package by.chapailo.icalculator.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.chapailo.icalculator.domain.Operator
import by.chapailo.icalculator.presentation.composables.NumberField
import by.chapailo.icalculator.presentation.model.Action
import by.chapailo.icalculator.presentation.model.CalculatorScreenState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CalculationScreen(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<CalculatorScreenState>,
    onAction: (Action) -> Unit
) {
    val state: State<CalculatorScreenState> =
        stateFlow.collectAsState()

    Column(modifier = modifier) {
        NumberField(
            value = state.value.firstNumber,
            onValueChange = { newFirstNumber -> onAction(Action.FirstNumberChange(newFirstNumber)) },
            isError = !state.value.isFirstNumberValid && state.value.firstNumber.isNotEmpty(),
            placeholder = "First number"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onAction(Action.OperatorChange) },
                content = { Icon(Icons.Default.Add, contentDescription = null) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.value.operator == Operator.PLUS)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAction(Action.OperatorChange) },
                content = { Icon(Icons.Default.Remove, contentDescription = null) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.value.operator == Operator.MINUS)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }

        NumberField(
            value = state.value.secondNumber,
            onValueChange = { newSecondNumber -> onAction(Action.SecondNumberChange(newSecondNumber)) },
            isError = !state.value.isSecondNumberValid && state.value.firstNumber.isNotEmpty(),
            placeholder = "Second number"
        )

        Spacer(modifier = Modifier.height(16.dp))

        NumberField(
            value = state.value.resultNumber,
            onValueChange = { },
            placeholder = "Result",
            readOnly = true
        )
    }
}