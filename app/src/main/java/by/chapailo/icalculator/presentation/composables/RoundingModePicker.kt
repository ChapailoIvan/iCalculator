package by.chapailo.icalculator.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import by.chapailo.icalculator.presentation.model.RoundingModeUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundingModePicker(
    modifier: Modifier = Modifier,
    values: List<RoundingModeUi>,
    pickedValue: RoundingModeUi,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (RoundingModeUi) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = "Rounding mode: ${pickedValue.alias}",
                readOnly = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = { }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(!expanded) }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        text = { Text(text = value.alias) },
                        onClick = {
                            onValueChange(value)
                            onExpandedChange(!expanded)
                        }
                    )
                }
            }
        }
    }
}