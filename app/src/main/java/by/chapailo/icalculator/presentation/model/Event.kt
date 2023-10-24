package by.chapailo.icalculator.presentation.model

sealed interface Event {
    class ShowSnackbar(val message: String): Event
}