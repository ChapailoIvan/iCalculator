package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.Operator

sealed interface Action {
    class FirstNumberChange(val value: String): Action
    class SecondNumberChange(val value: String): Action
    class OperatorChange(val operator: Operator): Action
}