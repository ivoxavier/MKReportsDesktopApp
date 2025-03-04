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
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import utils.Database
import java.io.File
import java.io.FileOutputStream
import java.sql.Connection
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

data class LosRowData(
    val EscadaSucesso_NumConsultora: String,
    val EscadaSucesso_NomeConsultora: String,
    val EscadaSucesso_NivelCarreira: String,
    val EscadaSucesso_TotalNovasConsultoras: String,
    val EscadaSucesso_TotalNovasConsultorasQualificadas: String,
    val EscadaSucesso_TotalPrograma: String,
    val EscadaSucesso_NivelConseguido: String,
    val EscadaSucesso_PrecisaParaNivelSeguinte: String,
    val EscadaSucesso_Trimestre: String
)

@Composable
fun LadderOfSucess() {
    var consultantNumber by remember { mutableStateOf("") }
    var selectedSemester by remember { mutableStateOf("all") }
    /*var selectedType by remember { mutableStateOf("Todos") }*/
    val tableData = remember { mutableStateListOf<LosRowData>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val columnWidths = listOf(150.dp, 150.dp, 200.dp, 150.dp, 150.dp, 150.dp, 150.dp, 150.dp, 150.dp)


    fun fetchDataFromDatabase() {
        isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                connection = Database.getConnection()
                if (connection != null) {
                    var query =
                        """ SELECT los.EscadaSucesso_NumConsultora,
                            los.EscadaSucesso_NomeConsultora,
                            los.EscadaSucesso_NivelCarreira,
                            los.EscadaSucesso_TotalNovasConsultoras,
                            los.EscadaSucesso_TotalNovasConsultorasQualificadas,
                            los.EscadaSucesso_TotalPrograma,
                            los.EscadaSucesso_NivelConseguido,
                            los.EscadaSucesso_PrecisaParaNivelSeguinte,
                            los.EscadaSucesso_Trimestre 
                            FROM TB_EscadaSucesso los
                            """.trimIndent()
                    val conditions = mutableListOf<String>()

                    if (consultantNumber.isNotBlank()) {
                        conditions.add("los.EscadaSucesso_NumConsultora = '$consultantNumber'")
                    }
                    if (selectedSemester != "all") {
                        conditions.add("los.EscadaSucesso_Trimestre = '$selectedSemester'")
                    }
                    /*if (selectedType != "Todos") {
                        val type = if (selectedType == "DIQ") "DIQ_TYPE" else "EQUIPA_TYPE"
                        conditions.add("type = '$type'")
                    }*/

                    if (conditions.isNotEmpty()) {
                        query += " WHERE " + conditions.joinToString(" AND ")
                    }

                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(query)

                    val newData = mutableListOf<LosRowData>()
                    while (resultSet.next()) {
                        val row = LosRowData(
                            resultSet.getString("EscadaSucesso_NumConsultora") ?: "",
                            resultSet.getString("EscadaSucesso_NomeConsultora") ?: "",
                            resultSet.getString("EscadaSucesso_NivelCarreira") ?: "",
                            resultSet.getString("EscadaSucesso_TotalNovasConsultoras") ?: "",
                            resultSet.getString("EscadaSucesso_TotalNovasConsultorasQualificadas") ?: "",
                            resultSet.getString("EscadaSucesso_TotalPrograma") ?: "",
                            resultSet.getString("EscadaSucesso_NivelConseguido") ?: "",
                            resultSet.getString("EscadaSucesso_PrecisaParaNivelSeguinte") ?: "",
                            resultSet.getString("EscadaSucesso_Trimestre") ?: "",
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
                e.printStackTrace()
            } finally {
                connection?.close()
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    fun exportDataToExcel() {
        coroutineScope.launch(Dispatchers.IO) {
            val fileChooser = JFileChooser().apply {
                dialogTitle = "Selecione onde guardar o arquivo"
                fileFilter = FileNameExtensionFilter("Arquivos Excel (*.xlsx)", "xlsx")
                isAcceptAllFileFilterUsed = false
            }

            val userSelection = fileChooser.showSaveDialog(null)

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                var fileToSave = fileChooser.selectedFile
                if (!fileToSave.name.lowercase().endsWith(".xlsx")) {
                    fileToSave = File(fileToSave.parentFile, fileToSave.name + ".xlsx")
                }


                try {
                    XSSFWorkbook().use { workbook ->
                        val sheet = workbook.createSheet("EscadaSucesso")


                        val headerRow = sheet.createRow(0)
                        val headerTitles = listOf(
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NumConsultora,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NomeConsultora,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NivelCarreira,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalNovasConsultoras,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalNovasConsultorasQualificadas,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalPrograma,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NivelConseguido,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_PrecisaParaNivelSeguinte,
                            MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_Trimestre
                        )
                        headerTitles.forEachIndexed { index, title ->
                            headerRow.createCell(index).setCellValue(title)
                        }


                        tableData.forEachIndexed { rowIndex, rowData ->
                            val row = sheet.createRow(rowIndex + 1)
                            row.createCell(0).setCellValue(rowData.EscadaSucesso_NumConsultora)
                            row.createCell(1).setCellValue(rowData.EscadaSucesso_NomeConsultora)
                            row.createCell(2).setCellValue(rowData.EscadaSucesso_NivelCarreira)
                            row.createCell(3).setCellValue(rowData.EscadaSucesso_TotalNovasConsultoras)
                            row.createCell(4).setCellValue(rowData.EscadaSucesso_TotalNovasConsultorasQualificadas)
                            row.createCell(5).setCellValue(rowData.EscadaSucesso_TotalPrograma)
                            row.createCell(6).setCellValue(rowData.EscadaSucesso_NivelConseguido)
                            row.createCell(7).setCellValue(rowData.EscadaSucesso_PrecisaParaNivelSeguinte)
                            row.createCell(8).setCellValue(rowData.EscadaSucesso_Trimestre)
                        }

                        // Write to the file *inside* the workbook's use block
                        FileOutputStream(fileToSave).use { outputStream ->
                            workbook.write(outputStream)
                        }
                    }
                    println("File saved successfully!")
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Failed to save file!")
                }
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier=Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
            ){
            Button(onClick = { fetchDataFromDatabase() }, modifier = Modifier.padding(8.dp)) {
                Text(MKReportsConstants.BUTTONS.UPDATE)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(MKReportsConstants.REPORTS.LADDER_OF_SUCCESS,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(MKReportsConstants.TEXT_FIELDS.CONSULTANT_NUMBER+":")
            TextField(
                value = consultantNumber,
                onValueChange = { consultantNumber = it },
                modifier = Modifier
                    .width(174.dp)
                    .height(70.dp)
                    .padding(8.dp)
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
            SemesterRadioButton("1_Trimestre", selectedSemester) { selectedSemester = "1_Trimestre" }
            SemesterRadioButton("2_Trimestre", selectedSemester) { selectedSemester = "2_Trimestre" }
            SemesterRadioButton("3_Trimestre", selectedSemester) { selectedSemester = "3_Trimestre" }
            SemesterRadioButton("4_Trimestre", selectedSemester) { selectedSemester = "4_Trimestre" }
            SemesterRadioButton("all", selectedSemester) { selectedSemester = "all" }
        }

        /*BlankVerticalSpace()

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
        }*/

        BlankVerticalSpace()


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            //.border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NumConsultora, weight = columnWidths[0], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NomeConsultora, weight = columnWidths[1], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NivelCarreira, weight = columnWidths[2], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalNovasConsultoras, weight = columnWidths[3], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalNovasConsultorasQualificadas, weight = columnWidths[4], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_TotalPrograma, weight = columnWidths[5], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_NivelConseguido, weight = columnWidths[6], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_PrecisaParaNivelSeguinte, weight = columnWidths[7], isHeader = true)
            TableCell(text = MKReportsConstants.TABLE.ESCADA_SUCESSO.COLUMNS.EscadaSucesso_Trimestre, weight = columnWidths[8], isHeader = true)
        }


        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                Text(MKReportsConstants.APP.LOADING_LABEL)
            } else {
                DataTable(data = tableData)
            }
        }


        Row(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically){
            Button(onClick = { exportDataToExcel() }){
                Text(MKReportsConstants.BUTTONS.EXPORT_DATA)
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
                selectedColor = Color.Blue,
                unselectedColor = Color.Gray
            )
        )
        Text(text = if (semester == "all") "Todos" else "$semester")
    }
}

/*@Composable
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
}*/


@Composable
fun DataTable(data: List<LosRowData>) {
    val columnWidths = listOf(
        150.dp, 150.dp, 200.dp, 150.dp, 150.dp, 150.dp, 150.dp, 150.dp, 150.dp
    )

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(data) { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                // .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                TableCell(text = rowData.EscadaSucesso_NumConsultora, weight = columnWidths[0])
                TableCell(text = rowData.EscadaSucesso_NomeConsultora, weight = columnWidths[1])
                TableCell(text = rowData.EscadaSucesso_NivelCarreira, weight = columnWidths[2])
                TableCell(text = rowData.EscadaSucesso_TotalNovasConsultoras, weight = columnWidths[3])
                TableCell(text = rowData.EscadaSucesso_TotalNovasConsultorasQualificadas, weight = columnWidths[4])
                TableCell(text = rowData.EscadaSucesso_TotalPrograma, weight = columnWidths[5])
                TableCell(text = rowData.EscadaSucesso_NivelConseguido, weight = columnWidths[6])
                TableCell(text = rowData.EscadaSucesso_PrecisaParaNivelSeguinte, weight = columnWidths[7])
                TableCell(text = rowData.EscadaSucesso_Trimestre, weight = columnWidths[8])

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