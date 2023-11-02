package by.chapailo.icalculator.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.icalculator.domain.Operator
import by.chapailo.icalculator.domain.RoundingMode
import by.chapailo.icalculator.presentation.model.Action
import by.chapailo.icalculator.presentation.model.CalculatorScreenState
import by.chapailo.icalculator.presentation.model.Event
import by.chapailo.icalculator.presentation.model.toRoundingMode
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
import java.math.RoundingMode as JavaRoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CalculatorViewModel : ViewModel() {

    companion object {
        private const val TAG = "CalculatorViewModel"

        private const val INTERIM_SCALE = 10
        private const val RESULT_SCALE = 6

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
        DecimalFormat("###,###.######", decimalFormatSymbols)

    fun handle(action: Action) {
        when (action) {
            is Action.NumberChange -> {
                mutableStateFlow.update { state ->
                    when (action.numberId) {
                        1 -> state.copy(firstNumber = action.value.trimStart())
                        2 -> state.copy(secondNumber = action.value.trimStart())
                        3 -> state.copy(thirdNumber = action.value.trimStart())
                        4 -> state.copy(fourthNumber = action.value.trimStart())
                        else -> state
                    }
                }
            }

            is Action.RowOperatorChange -> {
                mutableStateFlow.update { state ->
                    when (action.rowId) {
                        1 -> state.copy(operator1 = action.operator)
                        2 -> state.copy(operator2 = action.operator)
                        3 -> state.copy(operator3 = action.operator)
                        else -> state
                    }
                }
            }

            is Action.RoundingModePick -> {
                mutableStateFlow.update { state ->
                    state.copy(pickedRoundingMode = action.roundingModeUi)
                }
            }

            is Action.RoundingModePickerClick -> {
                mutableStateFlow.update { state ->
                    state.copy(isRoundingModesPickerExpanded = !state.isRoundingModesPickerExpanded)
                }
            }
        }

        validate()
        recalculate()
    }

    private fun validate(): Unit = mutableStateFlow.update { state ->
        state.copy(
            isFirstNumberValid = state.firstNumber.isValidNumber(),
            isSecondNumberValid = state.secondNumber.isValidNumber(),
            isThirdNumberValid = state.thirdNumber.isValidNumber(),
            isFourthNumberValid = state.fourthNumber.isValidNumber()
        )
    }


    private fun String.isValidNumber(): Boolean = isNotBlank() && matches(numberRegex)

    private fun CalculatorScreenState.isAllNumbersValid(): Boolean {
        return isFirstNumberValid && isSecondNumberValid && isThirdNumberValid && isFourthNumberValid
    }

    private fun recalculate() {
        viewModelScope.launch(Dispatchers.Default) {
            mutableStateFlow.update { state -> state.clearResult() }

            with(mutableStateFlow.value) {
                Log.d(TAG, "$TAG::state::${this@with}")

                if (!isAllNumbersValid())
                    return@launch

                val firstNumber = firstNumber.mapToBigDecimal()
                val secondNumber = secondNumber.mapToBigDecimal()
                val thirdNumber = thirdNumber.mapToBigDecimal()
                val fourthNumber = fourthNumber.mapToBigDecimal()

                val result1 = safeCalculate(
                    first = secondNumber,
                    second = thirdNumber,
                    operator = operator2,
                    onDivisionByZero = {
                        mutableStateFlow.update { state -> state.copy(isThirdNumberValid = false) }
                        eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
                        return@launch
                    }
                )

                val result =
                    if (operator1 == Operator.MULTIPLY || operator1 == Operator.DIVIDE) {
                        val result2 = safeCalculate(
                            first = firstNumber,
                            second = result1,
                            operator = operator1,
                            onDivisionByZero = {
                                mutableStateFlow.update { state ->
                                    state.copy(
                                        isSecondNumberValid = false,
                                        isThirdNumberValid = false
                                    )
                                }
                                eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
                                return@launch
                            }
                        )

                        safeCalculate(
                            first = result2,
                            second = fourthNumber,
                            operator = operator3,
                            onDivisionByZero = {
                                mutableStateFlow.update { state ->
                                    state.copy(isFourthNumberValid = false)
                                }
                                eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
                                return@launch
                            }
                        )
                    } else if (operator3 == Operator.MULTIPLY || operator3 == Operator.DIVIDE) {
                        val result2 = safeCalculate(
                            first = result1,
                            second = fourthNumber,
                            operator = operator3,
                            onDivisionByZero = {
                                mutableStateFlow.update { state ->
                                    state.copy(isFourthNumberValid = false)
                                }
                                eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
                                return@launch
                            }
                        )

                        safeCalculate(
                            first = firstNumber,
                            second = result2,
                            operator = operator1,
                            onDivisionByZero = {
                                mutableStateFlow.update { state ->
                                    state.copy(
                                        isSecondNumberValid = false,
                                        isThirdNumberValid = false
                                    )
                                }
                                eventChannel.trySend(Event.ShowSnackbar("Error: division by zero"))
                                return@launch
                            }
                        )
                    } else {
                        calculate(
                            first = calculate(firstNumber, result1, operator1),
                            second = fourthNumber,
                            operator = operator3
                        )
                    }

                val roundedResult = when (this@with.pickedRoundingMode.toRoundingMode()) {
                    RoundingMode.MATHEMATICAL -> result.setScale(0, JavaRoundingMode.HALF_UP)
                    RoundingMode.TRUNCATION -> result.setScale(0, JavaRoundingMode.DOWN)
                    RoundingMode.ACCOUNTING -> result.setScale(0, JavaRoundingMode.HALF_EVEN)
                }

                mutableStateFlow.update { state ->
                    state.copy(
                        resultNumber = result.toResultFormat(),
                        roundedResultNumber = roundedResult.toResultFormat()
                    )
                }
            }
        }
    }

    private inline fun safeCalculate(
        first: BigDecimal,
        second: BigDecimal,
        operator: Operator,
        onDivisionByZero: () -> Unit
    ): BigDecimal {
        if (operator == Operator.DIVIDE && second == BigDecimal.ZERO)
            onDivisionByZero()

        return calculate(first, second, operator)
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
            Operator.DIVIDE -> first.divide(second, INTERIM_SCALE, JavaRoundingMode.HALF_UP)
        }.stripTrailingZeros().setScale(INTERIM_SCALE, JavaRoundingMode.HALF_UP)
    }

    private fun CalculatorScreenState.clearResult(): CalculatorScreenState {
        return copy(resultNumber = "", roundedResultNumber = "")
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