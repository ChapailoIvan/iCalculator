package by.chapailo.icalculator.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.chapailo.icalculator.domain.Operator

@Composable
fun OperationRow(
    modifier: Modifier = Modifier,
    activeOperator: Operator,
    onOperatorChange: (Operator) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        OperationButton(
            operator = Operator.PLUS,
            enabled = activeOperator == Operator.PLUS,
            imageVector = Icons.Default.Add,
            onClick = { onOperatorChange(Operator.PLUS) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        OperationButton(
            operator = Operator.MINUS,
            enabled = activeOperator == Operator.MINUS,
            imageVector = Icons.Default.Remove,
            onClick = { onOperatorChange(Operator.MINUS) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        OperationButton(
            operator = Operator.MULTIPLY,
            enabled = activeOperator == Operator.MULTIPLY,
            imageVector = Icons.Default.Close,
            onClick = { onOperatorChange(Operator.MULTIPLY) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        OperationButton(
            operator = Operator.DIVIDE,
            enabled = activeOperator == Operator.DIVIDE,
            imageVector = Divide,
            onClick = { onOperatorChange(Operator.DIVIDE) }
        )
    }
}