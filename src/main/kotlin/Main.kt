import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import constants.MKReportsConstants
import screens.HomeScreen

fun main() = application {
    Window(onCloseRequest = ::exitApplication,
        title = MKReportsConstants.APP.APP_NAME) {
        MKReports()
    }
}

@Composable
@Preview
fun MKReports() {
    MaterialTheme {
        HomeScreen()
    }
}


