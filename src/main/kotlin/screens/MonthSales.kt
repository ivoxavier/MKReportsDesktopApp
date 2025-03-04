package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import constants.MKReportsConstants


@Composable
fun MonthSales(){
    var consultantNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()){
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                MKReportsConstants.REPORTS.MONTH_SALES,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp)
        }
    }
}