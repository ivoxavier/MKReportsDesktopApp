package screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.BlankVerticalSpace
import constants.MKReportsConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.Database
import java.sql.Connection

data class TableRowData(
    val column1: String,
    val column2: String,
    val column3: String,
    // ... add more columns as needed
)

@Composable
fun LadderOfSucess() {
    var consultantNumber by remember { mutableStateOf("") }
    var selectedSemester by remember { mutableStateOf("all") } // "all", "1", "2", "3", "4"
    var selectedType by remember { mutableStateOf("Todos") }   // "DIQ", "Equipa", "Todos"
    val tableData = remember { mutableStateListOf<TableRowData>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) } // Loading indicator state

    // Function to fetch data (now only called on button click)
    fun fetchDataFromDatabase() {
        isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                connection = Database.getConnection()
                if (connection != null) {
                    var query = "SELECT EscadaSucesso_NumConsultora, EscadaSucesso_NomeConsultora,EscadaSucesso_NivelCarreira FROM TB_EscadaSucesso"  // Your base query
                    val conditions = mutableListOf<String>()

                    if (consultantNumber.isNotBlank()) {
                        conditions.add("consultant_number = '$consultantNumber'") // DANGEROUS!
                    }
                    if (selectedSemester != "all") {
                        conditions.add("semester = '$selectedSemester'")        // DANGEROUS!
                    }
                    if (selectedType != "Todos") {
                        val type = if (selectedType == "DIQ") "DIQ_TYPE" else "EQUIPA_TYPE"
                        conditions.add("type = '$type'")                     // DANGEROUS!
                    }

                    if (conditions.isNotEmpty()) {
                        query += " WHERE " + conditions.joinToString(" AND ")
                    }

                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(query)

                    val newData = mutableListOf<TableRowData>()
                    while (resultSet.next()) {
                        val row = TableRowData(
                            resultSet.getString("EscadaSucesso_NumConsultora") ?: "",  // Use Elvis operator
                            resultSet.getString("EscadaSucesso_NomeConsultora") ?: "", // Use Elvis operator
                            resultSet.getString("EscadaSucesso_NivelCarreira") ?: ""  // Use Elvis operator
                        )
                        newData.add(row)
                    }
                    resultSet.close()
                    statement.close()

                    withContext(Dispatchers.Main) {
                        tableData.clear()
                        tableData.addAll(newData)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log or display the error
            } finally {
                connection?.close()
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) { // Use fillMaxSize()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { fetchDataFromDatabase() }) { // Call fetchDataFromDatabase on button click
                Text("Atualizar")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(MKReportsConstants.TEXT_FIELDS.CONSULTANT_NUMBER)

            TextField(
                value = consultantNumber,
                onValueChange = { consultantNumber = it },
                modifier = Modifier
                    .width(174.dp)
                    .height(40.dp)
            )
        }
        BlankVerticalSpace()
        Row(
            modifier = Modifier.fillMaxWidth(),
            // .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Semestres: ")
            // Wrap each RadioButton and Text in a Row for better alignment
            SemesterRadioButton("1", selectedSemester) { selectedSemester = "1" }
            SemesterRadioButton("2", selectedSemester) { selectedSemester = "2" }
            SemesterRadioButton("3", selectedSemester) { selectedSemester = "3" }
            SemesterRadioButton("4", selectedSemester) { selectedSemester = "4" }
            SemesterRadioButton("all", selectedSemester) { selectedSemester = "all" }
        }

        BlankVerticalSpace()

        Row(
            modifier = Modifier.fillMaxWidth(),
            //.border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tipo: ")
            // Wrap each RadioButton and Text in a Row for better alignment
            TypeRadioButton("DIQ", selectedType) { selectedType = "DIQ" }
            TypeRadioButton("Equipa", selectedType) { selectedType = "Equipa" }
            TypeRadioButton("Todos", selectedType) { selectedType = "Todos" }
        }

        BlankVerticalSpace()

        // Use a Box to allow the DataTable to take up remaining space
        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                Text("Loading...")
            } else {
                DataTable(data = tableData)
            }
        }


        Row(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))){
            Button(onClick = {}){
                Text("ExportData")
            }
        }
    }
}



@Composable
fun SemesterRadioButton(semester: String, selectedSemester: String, onSemesterSelected: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = (semester == selectedSemester),
            onClick = onSemesterSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Blue, // Customize colors as needed
                unselectedColor = Color.Gray
            )
        )
        Text(text = if (semester == "all") "Todos" else "Semestre $semester")
    }
}

@Composable
fun TypeRadioButton(type: String, selectedType: String, onTypeSelected: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = (type == selectedType),
            onClick = onTypeSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Blue,
                unselectedColor = Color.Gray
            )
        )
        Text(text = type)
    }
}


@Composable
fun DataTable(data: List<TableRowData>) {
    val columnWidths = listOf(150.dp, 150.dp, 200.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        //.border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
    ) {
        TableCell(text = "Column 1", weight = columnWidths[0], isHeader = true)
        TableCell(text = "Column 2", weight = columnWidths[1], isHeader = true)
        TableCell(text = "Column 3", weight = columnWidths[2], isHeader = true)
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(data) { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                // .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                TableCell(text = rowData.column1, weight = columnWidths[0])
                TableCell(text = rowData.column2, weight = columnWidths[1])
                TableCell(text = rowData.column3, weight = columnWidths[2])
            }
        }
    }
}

@Composable
fun TableCell(text: String, weight: Dp, isHeader: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier
            .width(weight)
            .padding(8.dp),
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        fontSize = if (isHeader) 18.sp else 14.sp
    )
}