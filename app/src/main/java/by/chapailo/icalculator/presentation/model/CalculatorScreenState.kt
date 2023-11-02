package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.Operator

data class CalculatorScreenState(
    val firstNumber: String = "0",
    val isFirstNumberValid: Boolean = true,
    val secondNumber: String = "0",
    val isSecondNumberValid: Boolean = true,
    val thirdNumber: String = "0",
    val isThirdNumberValid: Boolean = true,
    val fourthNumber: String = "0",
    val isFourthNumberValid: Boolean = true,
    val resultNumber: String = "0",
    val roundedResultNumber: String = "0",
    val operator1: Operator = Operator.PLUS,
    val operator2: Operator = Operator.PLUS,
    val operator3: Operator = Operator.PLUS,
    val isRoundingModesPickerExpanded: Boolean = false,
    val roundingModes: List<RoundingModeUi> = RoundingModeUi.values().toList(),
    val pickedRoundingMode: RoundingModeUi = RoundingModeUi.MATHEMATICAL
)