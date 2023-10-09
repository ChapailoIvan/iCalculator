package by.chapailo.icalculator.presentation.model

sealed interface Action {
    class FirstNumberChange(val value: String): Action
    class SecondNumberChange(val value: String): Action
    object OperatorChange: Action
}