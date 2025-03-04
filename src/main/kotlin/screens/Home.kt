package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import components.HomeButtonOption
import constants.MKReportsConstants
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import java.text.SimpleDateFormat
import java.util.*

/*use for when the options open a subMenu*/
enum class ScreenState {
    HOME,
    PROGRAMS
}

@Composable
fun HomeScreen() {

    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val currentDate = sdf.format(Date())
    var showNewWindow by remember { mutableStateOf(false) }
    var newWindowTitle by remember { mutableStateOf("") }
    var windowSize by remember { mutableStateOf(DpSize(1400.dp, 1100.dp)) }


    var currentScreen by remember { mutableStateOf(ScreenState.HOME) }


    Column(modifier = Modifier.background(MKReportsConstants.APP.COLORS.MARYKAY_PINK)) {
        // Top Bar (always present)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MKReportsConstants.APP.COLORS.MARYKAY_PINK),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = currentDate, fontWeight = FontWeight.Bold)
        }

        // visible when currentscreen is not HOME
        if (currentScreen != ScreenState.HOME) {
            Button(modifier = Modifier.padding(8.dp),
                onClick = { currentScreen = ScreenState.HOME }) {
                Text("Voltar ao Menu")
            }
        }


        AnimatedVisibility(
            visible = currentScreen == ScreenState.HOME,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (currentScreen == ScreenState.HOME) {

                val buttonTitles = listOf(
                    MKReportsConstants.BUTTONS.MONTH_SALES,
                    MKReportsConstants.BUTTONS.PROGRAMS,
                    "Relatório 2", "Configurações",
                    "Opçao1",
                    "Opção 7", "Opção 8", "Opção 9"
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    modifier = Modifier.background(MKReportsConstants.APP.COLORS.MARYKAY_PINK)
                ) {
                    items(buttonTitles) { title ->
                        HomeButtonOption(title, onClick = {
                            when (title) {
                                MKReportsConstants.BUTTONS.MONTH_SALES -> {
                                    newWindowTitle = MKReportsConstants.REPORTS.MONTH_SALES
                                    showNewWindow = true
                                }
                                MKReportsConstants.BUTTONS.PROGRAMS -> {
                                    currentScreen = ScreenState.PROGRAMS
                                }


                                // other options

                            }
                        })
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = currentScreen == ScreenState.PROGRAMS,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            if(currentScreen == ScreenState.PROGRAMS) {
                ProgramsScreen()
            }
        }
    }

    if(showNewWindow){
        Window(onCloseRequest = {showNewWindow = false}, title = newWindowTitle,
            state = WindowState(size = windowSize) ){
            when(newWindowTitle){
                MKReportsConstants.REPORTS.MONTH_SALES -> MonthSales()
            }
        }
    }
}


@Composable
fun ProgramsScreen() {
    var showNewWindow by remember { mutableStateOf(false) }
    var newWindowTitle by remember { mutableStateOf("") }
    var windowSize by remember { mutableStateOf(DpSize(1400.dp, 1100.dp)) }
    val programButtonTitles = listOf(
        MKReportsConstants.BUTTONS.LADDER_OF_SUCESS_REPORT,
        "Programa 2",
        "Programa 3",
        "Programa 4"
    )
    Column {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Pode ajustar o número de colunas
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.background(MKReportsConstants.APP.COLORS.MARYKAY_PINK)
        ) {
            items(programButtonTitles) { programTitle ->
                HomeButtonOption(programTitle,onClick = {
                    when(programTitle){
                        MKReportsConstants.BUTTONS.LADDER_OF_SUCESS_REPORT -> {
                            newWindowTitle = "Ladder of Success"
                            showNewWindow = true
                        }
                    }
                })

            }
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
