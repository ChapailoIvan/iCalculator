package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.Operator

sealed interface Action {
    class NumberChange(val numberId: Int, val value: String): Action
    class RowOperatorChange(val rowId: Int, val operator: Operator): Action
    object RoundingModePickerClick: Action
    class RoundingModePick(val roundingModeUi: RoundingModeUi): Action

}