package screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import components.HomeButtonOption
import constants.MKReportsConstants
import mappers.Screen
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.WindowState


@Composable
fun HomeScreen() {

    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val currentDate = sdf.format(Date())
    var showNewWindow by remember { mutableStateOf(false) }
    var newWindowTitle by remember { mutableStateOf("") }
    var windowSize by remember { mutableStateOf(DpSize(1400.dp, 1100.dp)) }

    val buttonTitles = listOf(
        MKReportsConstants.MAIN_GRID_BUTTONS.LADDER_OF_SUCESS_REPORT,
         "Relatório 2", "Configurações",
        "Opçao1",
        "Opção 7", "Opção 8", "Opção 9"
    )


    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text=currentDate, fontWeight = FontWeight.Bold)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 coluns fixed
        contentPadding = PaddingValues(40.dp), // grid padding
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        items(buttonTitles) { title ->
             HomeButtonOption(title, onClick = {
                 when(title){
                     MKReportsConstants.MAIN_GRID_BUTTONS.LADDER_OF_SUCESS_REPORT -> {
                         newWindowTitle = "Ladder of Success"
                         showNewWindow = true
                     }
                     //other cases
                 }
             })
        }
    }

    if(showNewWindow){
        Window(onCloseRequest = {showNewWindow = false}, title = newWindowTitle,
            state = WindowState(size = windowSize) ){
            when(newWindowTitle){
                "Ladder of Success" -> LadderOfSucess()
            }
        }
    }
}