package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ConsultantCard
import constants.MKReportsConstants

@Composable
fun Consultants() {
    val totalConsultorasUnidade = 150
    val totalConsultorasEquipa = 235


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                MKReportsConstants.REPORTS.CONSULTANTS,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }//Screen name

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConsultantCard("Unidade", totalConsultorasUnidade)


            ConsultantCard("Equipa", totalConsultorasEquipa)
        }
    }
}

