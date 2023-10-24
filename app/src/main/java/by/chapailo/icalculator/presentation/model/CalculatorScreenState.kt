package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.Operator

data class CalculatorScreenState(
    val firstNumber: String = "",
    val isFirstNumberValid: Boolean = false,
    val secondNumber: String = "",
    val isSecondNumberValid: Boolean = false,
    val resultNumber: String = "",
    val operator: Operator = Operator.PLUS
)