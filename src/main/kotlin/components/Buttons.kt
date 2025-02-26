package components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeButtonOption(title: String, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = {onClick()},
        modifier = Modifier.width(150.dp).height(60.dp),
        elevation = ButtonDefaults.elevation(8.dp)
    ) {
        Text(title)
    }
}