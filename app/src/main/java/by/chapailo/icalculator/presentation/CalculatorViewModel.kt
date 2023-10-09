package by.chapailo.icalculator.presentation

import androidx.lifecycle.ViewModel
import by.chapailo.icalculator.domain.Operator
import by.chapailo.icalculator.presentation.model.Action
import by.chapailo.icalculator.presentation.model.CalculatorScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

typealias FirstNumberChange = Action.FirstNumberChange
typealias SecondNumberChange = Action.SecondNumberChange
typealias OperatorChange = Action.OperatorChange

class CalculatorViewModel : ViewModel() {

    companion object {
        private const val TAG = "CalculatorViewModel"
        private val numberRegex: Regex = "^[-+]?(\\d+([\\.,]\\d*)?|[\\.,]\\d+)$".toRegex()
    }

    private val mutableStateFlow: MutableStateFlow<CalculatorScreenState> =
        MutableStateFlow(CalculatorScreenState())
    val stateFlow: StateFlow<CalculatorScreenState> = mutableStateFlow.asStateFlow()

    private fun validateAndRecalculate() {
        validate()

        with(stateFlow.value) {
            if (!isFirstNumberValid || !isSecondNumberValid) {
                mutableStateFlow.update { state -> state.copy(resultNumber = "") }
                return@with
            }

            val firstNumber = this.firstNumber.toBigDecimal()
            val secondNumber = this.secondNumber.toBigDecimal()

            val resultNumber = if (operator == Operator.PLUS) firstNumber + secondNumber
            else firstNumber - secondNumber

            mutableStateFlow.update { state ->
                state.copy(resultNumber = resultNumber.toString())
            }
        }
    }

    private fun validate() {
        mutableStateFlow
            .update { state ->
                state.copy(
                    isFirstNumberValid = state.firstNumber.matches(numberRegex),
                    isSecondNumberValid = state.secondNumber.matches(numberRegex)
                )
            }
    }

    fun handle(action: Action) {
        when (action) {
            is FirstNumberChange -> {
                mutableStateFlow
                    .update { state -> state.copy(firstNumber = action.value.toBigDecimalFormat()) }
            }

            is SecondNumberChange -> {
                mutableStateFlow
                    .update { state -> state.copy(secondNumber = action.value.toBigDecimalFormat()) }
            }

            is OperatorChange -> {
                mutableStateFlow
                    .update { state -> state.copy(operator = state.changeOperator()) }
            }
        }

        validateAndRecalculate()
    }

    private fun String.toBigDecimalFormat(): String =
        replace(",", ".")

    private fun CalculatorScreenState.changeOperator(): Operator =
        if (operator == Operator.PLUS) Operator.MINUS else Operator.PLUS

}