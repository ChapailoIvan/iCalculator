package by.chapailo.icalculator.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.chapailo.icalculator.presentation.composables.NumberField
import by.chapailo.icalculator.presentation.composables.OperationRow
import by.chapailo.icalculator.presentation.composables.RoundingModePicker
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
    val scrollState = rememberScrollState()

    WithLifecycle {
        eventFlow.collectLatest { event ->
            when (event) {
                is Event.ShowSnackbar ->
                    showSnackbar?.invoke(event.message)
            }
        }
    }

    Column(modifier = modifier.verticalScroll(scrollState).imePadding()) {
        NumberField(
            value = state.value.firstNumber,
            onValueChange = { newFirstNumber ->
                onAction(
                    Action.NumberChange(
                        numberId = 1,
                        value = newFirstNumber
                    )
                )
            },
            isError = !state.value.isFirstNumberValid && state.value.firstNumber.isNotEmpty(),
            placeholder = "First number"
        )

        OperationRow(
            activeOperator = state.value.operator1,
            onOperatorChange = { operator ->
                onAction(Action.RowOperatorChange(rowId = 1, operator = operator))
            }
        )

        NumberField(
            value = state.value.secondNumber,
            onValueChange = { newSecondNumber ->
                onAction(
                    Action.NumberChange(
                        numberId = 2,
                        value = newSecondNumber
                    )
                )
            },
            isError = !state.value.isSecondNumberValid && state.value.secondNumber.isNotEmpty(),
            placeholder = "Second number"
        )

        OperationRow(
            activeOperator = state.value.operator2,
            onOperatorChange = { operator ->
                onAction(Action.RowOperatorChange(rowId = 2, operator = operator))
            }
        )

        NumberField(
            value = state.value.thirdNumber,
            onValueChange = { newThirdNumber ->
                onAction(
                    Action.NumberChange(
                        numberId = 3,
                        value = newThirdNumber
                    )
                )
            },
            isError = !state.value.isThirdNumberValid && state.value.thirdNumber.isNotEmpty(),
            placeholder = "Third number"
        )

        OperationRow(
            activeOperator = state.value.operator3,
            onOperatorChange = { operator ->
                onAction(Action.RowOperatorChange(rowId = 3, operator = operator))
            }
        )

        NumberField(
            value = state.value.fourthNumber,
            onValueChange = { newFourthNumber ->
                onAction(
                    Action.NumberChange(
                        numberId = 4,
                        value = newFourthNumber
                    )
                )
            },
            isError = !state.value.isFourthNumberValid && state.value.fourthNumber.isNotEmpty(),
            placeholder = "Fourth number"
        )

        Spacer(modifier = Modifier.height(16.dp))

        NumberField(
            value = state.value.resultNumber,
            onValueChange = { },
            placeholder = "Result",
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundingModePicker(
            values = state.value.roundingModes,
            expanded = state.value.isRoundingModesPickerExpanded,
            pickedValue = state.value.pickedRoundingMode,
            onExpandedChange = { _ ->
                onAction(Action.RoundingModePickerClick)
            },
            onValueChange = { roundingModeUi ->
                onAction(Action.RoundingModePick(roundingModeUi))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NumberField(
            value = state.value.roundedResultNumber,
            onValueChange = { },
            placeholder = "Rounded result",
            readOnly = true
        )

    }
}