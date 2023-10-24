package by.chapailo.icalculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.icalculator.domain.Operator
import by.chapailo.icalculator.presentation.model.Action
import by.chapailo.icalculator.presentation.model.CalculatorScreenState
import by.chapailo.icalculator.presentation.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

typealias FirstNumberChange = Action.FirstNumberChange
typealias SecondNumberChange = Action.SecondNumberChange
typealias OperatorChange = Action.OperatorChange

class CalculatorViewModel : ViewModel() {

    companion object {
        private const val TAG = "CalculatorViewModel"
        private val numberRegex: Regex =
            "^[+-]?([0-9]{1,3}(\\s?[0-9]{3})*([\\.\\,][0-9]+)?)\$".toRegex()
    }

    private val mutableStateFlow: MutableStateFlow<CalculatorScreenState> =
        MutableStateFlow(CalculatorScreenState())
    val stateFlow: StateFlow<CalculatorScreenState> = mutableStateFlow.asStateFlow()

    private val eventChannel: Channel<Event> = Channel()
    val eventFlow: Flow<Event> = eventChannel.receiveAsFlow()

    private val decimalFormatSymbols: DecimalFormatSymbols =
        DecimalFormatSymbols(Locale.GERMANY).apply {
            groupingSeparator = ' '
            decimalSeparator = '.'
        }
    private val decimalFormat: DecimalFormat =
        DecimalFormat("###,###.#######", decimalFormatSymbols)

    private fun recalculate() = with(stateFlow.value) {
        viewModelScope.launch(Dispatchers.Default) {
            if (!isFirstNumberValid || !isSecondNumberValid) {
                mutableStateFlow.update { state -> state.copy(resultNumber = "") }
                return@launch
            }

            val firstNumber = this@with.firstNumber.mapToBigDecimal()
            val secondNumber = this@with.secondNumber.mapToBigDecimal()

            mutableStateFlow.update { state ->
                state.copy(
                    resultNumber = calculate(
                        firstNumber,
                        secondNumber,
                        operator
                    ).toResultFormat()
                )
            }
        }
    }

    fun handle(action: Action) {
        when (action) {
            is FirstNumberChange -> {
                mutableStateFlow
                    .update { state ->
                        state.copy(
                            firstNumber = action.value.trimStart(),
                            isFirstNumberValid = isFirstNumberValid(number = action.value)
                        )
                    }
            }

            is SecondNumberChange -> {
                mutableStateFlow
                    .update { state ->
                        state.copy(
                            secondNumber = action.value.trimStart(),
                            isSecondNumberValid = isSecondNumberValid(numberr = action.value)
                        )
                    }
            }

            is OperatorChange -> {
                mutableStateFlow
                    .update { state ->
                        state.copy(
                            operator = action.operator,
                            isSecondNumberValid = isSecondNumberValid(operatorr = action.operator)
                        )
                    }
            }
        }

        recalculate()
    }

    private fun calculate(
        first: BigDecimal,
        second: BigDecimal,
        operator: Operator
    ): BigDecimal {
        return when (operator) {
            Operator.PLUS -> first.plus(second)
            Operator.MINUS -> first.minus(second)
            Operator.MULTIPLY -> first.multiply(second)
            Operator.DIVIDE -> first.divide(second, 6, RoundingMode.HALF_UP)
        }.stripTrailingZeros()
    }

    private fun isFirstNumberValid(number: String): Boolean {
        return number.matches(numberRegex)
    }
    private fun isSecondNumberValid(numberr: String? = null, operatorr: Operator? = null): Boolean {
        val operator: Operator = operatorr ?: mutableStateFlow.value.operator
        val number: String = numberr ?: mutableStateFlow.value.secondNumber

        if (!number.matches(numberRegex)) {
            return false
        }

        if (operator == Operator.DIVIDE && number.mapToBigDecimal() == BigDecimal.ZERO) {
            eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
            return false
        }

        return true
    }


    private fun String.mapToBigDecimal(): BigDecimal {
        return replace(",", ".")
            .filterNot { it.isWhitespace() }
            .toBigDecimal()
    }

    private fun BigDecimal.toResultFormat(): String {
        return decimalFormat.format(this)
    }

}