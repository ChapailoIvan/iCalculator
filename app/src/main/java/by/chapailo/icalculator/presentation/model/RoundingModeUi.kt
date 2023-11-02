package by.chapailo.icalculator.presentation.model

import by.chapailo.icalculator.domain.RoundingMode

enum class RoundingModeUi(val alias: String) {
    MATHEMATICAL(alias = "Mathematical"),
    TRUNCATION(alias = "Truncation"),
    ACCOUNTING(alias = "Accounting")
}

fun RoundingMode.toRoundingModeUi(): RoundingModeUi {
    return when(this) {
        RoundingMode.MATHEMATICAL -> RoundingModeUi.MATHEMATICAL
        RoundingMode.TRUNCATION -> RoundingModeUi.TRUNCATION
        RoundingMode.ACCOUNTING -> RoundingModeUi.ACCOUNTING
    }
}

fun RoundingModeUi.toRoundingMode(): RoundingMode {
    return when(this) {
        RoundingModeUi.MATHEMATICAL -> RoundingMode.MATHEMATICAL
        RoundingModeUi.TRUNCATION -> RoundingMode.TRUNCATION
        RoundingModeUi.ACCOUNTING -> RoundingMode.ACCOUNTING
    }
}


