package by.chapailo.icalculator.presentation.composables

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

private var _divide: ImageVector? = null
val Divide: ImageVector
    get() {
        if (_divide != null) {
            return _divide!!
        }
        _divide = materialIcon(name = "Filled.Divide") {
            materialPath {
                moveTo(19.0f, 6.41f)
                lineTo(17.59f, 5.0f)
                lineTo(12.0f, 10.59f)
                lineTo(10.59f, 12.0f)
                lineTo(5.0f, 17.59f)
                lineTo(6.41f, 19.0f)
                close()
            }
        }
        return _divide!!
    }