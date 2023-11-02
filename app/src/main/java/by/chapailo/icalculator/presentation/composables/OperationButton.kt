package by.chapailo.icalculator.presentation.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import by.chapailo.icalculator.domain.Operator

@Composable
fun OperationButton(
    operator: Operator,
    enabled: Boolean,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        content = { Icon(imageVector = imageVector, contentDescription = "$operator") },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (enabled)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
        )
    )
}