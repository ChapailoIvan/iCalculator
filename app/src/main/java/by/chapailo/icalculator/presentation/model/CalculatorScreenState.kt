package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.Operator

data class CalculatorScreenState(
    val firstNumber: String = "",
    val isFirstNumberValid: Boolean = true,
    val secondNumber: String = "",
    val isSecondNumberValid: Boolean = true,
    val resultNumber: String = "",
    val operator: Operator = Operator.PLUS
)