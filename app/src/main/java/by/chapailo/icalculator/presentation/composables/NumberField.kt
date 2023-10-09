package by.chapailo.icalculator.presentation.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    isError: Boolean = false,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        TextField(
            modifier = Modifier.horizontalScroll(scrollState),
            value = value,
            isError = isError,
            placeholder = { Text(text = placeholder) },
            onValueChange = onValueChange,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            maxLines = 1
        )
    }
}