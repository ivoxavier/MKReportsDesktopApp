import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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


