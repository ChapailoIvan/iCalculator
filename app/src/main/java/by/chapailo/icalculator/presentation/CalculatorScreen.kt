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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import by.chapailo.icalculator.domain.Operator
import by.chapailo.icalculator.presentation.composables.Divide
import by.chapailo.icalculator.presentation.composables.NumberField
import by.chapailo.icalculator.presentation.model.Action
import by.chapailo.icalculator.presentation.model.CalculatorScreenState
import by.chapailo.icalculator.presentation.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CalculationScreen(
    modifier: Modifier = Modifier,
    eventFlow: Flow<Event>,
    stateFlow: StateFlow<CalculatorScreenState>,
    onAction: (Action) -> Unit,
    showSnackbar: ((String) -> Unit)? = null
) {

    val state: State<CalculatorScreenState> =
        stateFlow.collectAsState()

    CollectWithLifecycle {
        eventFlow.collectLatest { event ->
            when (event) {
                is Event.ShowSnackbar ->
                    showSnackbar?.invoke(event.message)
            }
        }
    }

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
            OperationButton(
                operator = Operator.PLUS,
                enabled = state.value.operator == Operator.PLUS,
                imageVector = Icons.Default.Add,
                onAction = onAction
            )

            Spacer(modifier = Modifier.width(16.dp))

            OperationButton(
                operator = Operator.MINUS,
                enabled = state.value.operator == Operator.MINUS,
                imageVector = Icons.Default.Remove,
                onAction = onAction
            )

            Spacer(modifier = Modifier.width(16.dp))

            OperationButton(
                operator = Operator.MULTIPLY,
                enabled = state.value.operator == Operator.MULTIPLY,
                imageVector = Icons.Default.Close,
                onAction = onAction
            )

            Spacer(modifier = Modifier.width(16.dp))

            OperationButton(
                operator = Operator.DIVIDE,
                enabled = state.value.operator == Operator.DIVIDE,
                imageVector = Divide,
                onAction = onAction
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

@Composable
private fun OperationButton(
    operator: Operator,
    enabled: Boolean,
    imageVector: ImageVector,
    onAction: (Action.OperatorChange) -> Unit
) {
    Button(
        onClick = { onAction(Action.OperatorChange(operator)) },
        content = { Icon(imageVector = imageVector, contentDescription = "$operator") },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (enabled)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
        )
    )
}