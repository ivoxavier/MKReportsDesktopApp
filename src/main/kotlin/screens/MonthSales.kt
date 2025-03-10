package screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.BlankHorizontalSpace
import components.TableCell
import constants.MKReportsConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import utils.Database
import java.io.File
import java.io.FileOutputStream
import java.sql.Connection
import java.sql.Types
import java.text.NumberFormat
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

data class MonthSalesRowData(
    val Consultora: String,
    val Nome: String,
    val MesVenda: String,
    val ValorVenda_PVP: String,
    val ValorVenda_Liquido: String,
    val Recrutadora: String
)

@Composable
fun MonthSales() {
    var consultantNumber by remember { mutableStateOf("") }
    var mesVendas by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val tableData = remember { mutableStateListOf<MonthSalesRowData>() }
    val columnWidths = listOf(150.dp, 200.dp, 150.dp, 150.dp, 150.dp, 200.dp)
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    var totalGeralPVP by remember { mutableStateOf(0.0) }
    var totalGeralLiquido by remember { mutableStateOf(0.0) }


    fun fetchDataFromDatabase() {
        isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                connection = Database.getConnection()
                if (connection != null) {
                    val query = """
                        WITH RankedSales AS (
                            SELECT
                                t1.Consultora,
                                t3.Nome,
                                t1.MesVenda,
                                t1.ValorVenda,
                                t3.RecrutadoraNum AS Recrutadora,
                                ROW_NUMBER() OVER (PARTITION BY t1.Consultora, t1.MesVenda ORDER BY t1.ExtractionDate DESC) as rn
                            FROM
                                MARYKAY_REPORTS.TB_Month_Sales_Sales t1
                            INNER JOIN
                                MARYKAY_REPORTS.TB_Month_Sales_Consultant t3 ON t3.ConsultantNumber = t1.Consultora
                            WHERE 1=1
                                AND (t1.Consultora = ? OR ? IS NULL)
                                AND (t1.MesVenda = ? OR ? IS NULL)
                        )
                        SELECT
                            Consultora,
                            Nome,
                            CASE
                                WHEN MesVenda = '01/2024' THEN 'Janeiro 2024'
                                WHEN MesVenda = '02/2024' THEN 'Fevereiro 2024'
                                WHEN MesVenda = '03/2024' THEN 'Março 2024'
                                WHEN MesVenda = '04/2024' THEN 'Abril 2024'
                                WHEN MesVenda = '05/2024' THEN 'Maio 2024'
                                WHEN MesVenda = '06/2024' THEN 'Junho 2024'
                                WHEN MesVenda = '07/2024' THEN 'Julho 2024'
                                WHEN MesVenda = '08/2024' THEN 'Agosto 2024'
                                WHEN MesVenda = '09/2024' THEN 'Setembro 2024'
                                WHEN MesVenda = '10/2024' THEN 'Outubro 2024'
                                WHEN MesVenda = '11/2024' THEN 'Novembro 2024'
                                WHEN MesVenda = '12/2024' THEN 'Dezembro 2024'
                                WHEN MesVenda = '01/2025' THEN 'Janeiro 2025'
                                WHEN MesVenda = '02/2025' THEN 'Fevereiro 2025'
                                WHEN MesVenda = '03/2025' THEN 'Março 2025'
                                WHEN MesVenda = '04/2025' THEN 'Abril 2025'
                                WHEN MesVenda = '05/2025' THEN 'Maio 2025'
                                WHEN MesVenda = '06/2025' THEN 'Junho 2025'
                                WHEN MesVenda = '07/2025' THEN 'Julho 2025'
                                WHEN MesVenda = '08/2025' THEN 'Agosto 2025'
                                WHEN MesVenda = '09/2025' THEN 'Setembro 2025'
                                WHEN MesVenda = '10/2025' THEN 'Outubro 2025'
                                WHEN MesVenda = '11/2025' THEN 'Novembro 2025'
                                WHEN MesVenda = '12/2025' THEN 'Dezembro 2025'
                                WHEN MesVenda = '01/2026' THEN 'Janeiro 2026'
                                WHEN MesVenda = '02/2026' THEN 'Fevereiro 2026'
                                WHEN MesVenda = '03/2026' THEN 'Março 2026'
                                WHEN MesVenda = '04/2026' THEN 'Abril 2026'
                                WHEN MesVenda = '05/2026' THEN 'Maio 2026'
                                WHEN MesVenda = '06/2026' THEN 'Junho 2026'
                                WHEN MesVenda = '07/2026' THEN 'Julho 2026'
                                WHEN MesVenda = '08/2026' THEN 'Agosto 2026'
                                WHEN MesVenda = '09/2026' THEN 'Setembro 2026'
                                WHEN MesVenda = '10/2026' THEN 'Outubro 2026'
                                WHEN MesVenda = '11/2026' THEN 'Novembro 2026'
                                WHEN MesVenda = '12/2026' THEN 'Dezembro 2026'
                                WHEN MesVenda = '01/2027' THEN 'Janeiro 2027'
                                WHEN MesVenda = '02/2027' THEN 'Fevereiro 2027'
                                WHEN MesVenda = '03/2027' THEN 'Março 2027'
                                WHEN MesVenda = '04/2027' THEN 'Abril 2027'
                                WHEN MesVenda = '05/2027' THEN 'Maio 2027'
                                WHEN MesVenda = '06/2027' THEN 'Junho 2027'
                                WHEN MesVenda = '07/2027' THEN 'Julho 2027'
                                WHEN MesVenda = '08/2027' THEN 'Agosto 2027'
                                WHEN MesVenda = '09/2027' THEN 'Setembro 2027'
                                WHEN MesVenda = '10/2027' THEN 'Outubro 2027'
                                WHEN MesVenda = '11/2027' THEN 'Novembro 2027'
                                WHEN MesVenda = '12/2027' THEN 'Dezembro 2027'
                                ELSE MesVenda
                            END AS MesVenda,
                            ValorVenda AS TotalVenda_PVP,
                            CASE
                                WHEN ValorVenda >= 160 AND ValorVenda <= 579 THEN ROUND((ValorVenda - (ValorVenda * 0.40)) / 1.23, 2)
                                WHEN ValorVenda >= 580 THEN ROUND((ValorVenda - (ValorVenda * 0.50)) / 1.23, 2)
                                ELSE ROUND(ValorVenda / 1.23, 2) 
                                END AS TotalVenda_Liquido,
                            Recrutadora
                        FROM
                            RankedSales
                        WHERE
                            rn = 1
                      --  GROUP BY
                      --      Consultora,
                      --      Nome,
                      --      MesVenda,
                      --      Recrutadora
                        ORDER BY
                            MesVenda;
                    """
                    val statement = connection.prepareStatement(query)

                    if (consultantNumber.isNotBlank()) {
                        statement.setString(1, consultantNumber)
                        statement.setString(2, consultantNumber)
                    } else {
                        statement.setNull(1, Types.VARCHAR)
                        statement.setNull(2, Types.VARCHAR)
                    }

                    if (mesVendas.isNotBlank()) {
                        statement.setString(3, mesVendas)
                        statement.setString(4, mesVendas)
                    } else {
                        statement.setNull(3, Types.VARCHAR)
                        statement.setNull(4, Types.VARCHAR)
                    }


                    val resultSet = statement.executeQuery()

                    val newData = mutableListOf<MonthSalesRowData>()
                    while (resultSet.next()) {
                        //val valorVenda = resultSet.getString("ValorVenda") ?: "0"
                        val row = MonthSalesRowData(
                            resultSet.getString("Consultora") ?: "",
                            resultSet.getString("Nome") ?: "",
                            resultSet.getString("MesVenda") ?: "",
                            resultSet.getString("TotalVenda_PVP") ?: "",
                            resultSet.getString("TotalVenda_Liquido") ?: "",
                            resultSet.getString("Recrutadora") ?: ""
                        )
                        newData.add(row)
                    }
                    resultSet.close()
                    statement.close()

                    withContext(Dispatchers.Main) {
                        tableData.clear()
                        tableData.addAll(newData)


                        totalGeralPVP = tableData.sumOf {
                            numberFormat.parse(it.ValorVenda_PVP)?.toDouble() ?: 0.0
                        }
                        totalGeralLiquido = tableData.sumOf{
                            numberFormat.parse( it.ValorVenda_Liquido)?.toDouble() ?: 0.0
                        }
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
                        val sheet = workbook.createSheet("VendasMensais")

                        val headerRow = sheet.createRow(0)
                        val headerTitles = listOf(
                            MKReportsConstants.TABLE.MONTHLY_SALES.CONSULTORA,
                            MKReportsConstants.TABLE.MONTHLY_SALES.NOME,
                            MKReportsConstants.TABLE.MONTHLY_SALES.MESVENDA,
                            MKReportsConstants.TABLE.MONTHLY_SALES.TOTALVENDA_PVP,
                            MKReportsConstants.TABLE.MONTHLY_SALES.TOTALVENDA_LIQUIDO,
                            MKReportsConstants.TABLE.MONTHLY_SALES.RECRUTADORA
                        )
                        headerTitles.forEachIndexed { index, title ->
                            headerRow.createCell(index).setCellValue(title)
                        }

                        tableData.forEachIndexed { rowIndex, rowData ->
                            val row = sheet.createRow(rowIndex + 1)
                            row.createCell(0).setCellValue(rowData.Consultora)
                            row.createCell(1).setCellValue(rowData.Nome)
                            row.createCell(2).setCellValue(rowData.MesVenda)
                            row.createCell(3).setCellValue(rowData.ValorVenda_PVP)
                            row.createCell(4).setCellValue(rowData.ValorVenda_Liquido)
                            row.createCell(5).setCellValue(rowData.Recrutadora)
                        }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { fetchDataFromDatabase() }, modifier = Modifier.padding(8.dp)) {
                Text(MKReportsConstants.BUTTONS.UPDATE)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                MKReportsConstants.REPORTS.MONTH_SALES,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(MKReportsConstants.TEXT_FIELDS.CONSULTANT_NUMBER + ":")
            TextField(
                value = consultantNumber,
                onValueChange = { consultantNumber = it },
                modifier = Modifier
                    .width(174.dp)
                    .height(70.dp)
                    .padding(8.dp)
            )
            Text("Mes Vendas" + ":")
            TextField(
                value = mesVendas,
                onValueChange = { mesVendas = it },
                modifier = Modifier
                    .width(174.dp)
                    .height(70.dp)
                    .padding(8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.CONSULTORA,
                weight = columnWidths[0],
                isHeader = true
            )
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.NOME,
                weight = columnWidths[1],
                isHeader = true
            )
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.MESVENDA,
                weight = columnWidths[2],
                isHeader = true
            )
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.TOTALVENDA_PVP,
                weight = columnWidths[3],
                isHeader = true
            )
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.TOTALVENDA_LIQUIDO,
                weight = columnWidths[4],
                isHeader = true
            )
            TableCell(
                text = MKReportsConstants.TABLE.MONTHLY_SALES.RECRUTADORA,
                weight = columnWidths[5],
                isHeader = true
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (isLoading) {
                Text(MKReportsConstants.APP.LOADING_LABEL)
            } else {
                DataTable(data = tableData)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total PVP: ${numberFormat.format(totalGeralPVP)}",fontWeight = FontWeight.Bold)
            BlankHorizontalSpace()
            Text("Total Liquido: ${numberFormat.format(totalGeralLiquido)}", fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { exportDataToExcel() }) {
                Text(MKReportsConstants.BUTTONS.EXPORT_DATA)
            }
        }
    }
}

@Composable
fun DataTable(data: List<MonthSalesRowData>) {
    val columnWidths = listOf(150.dp, 200.dp, 150.dp, 200.dp,150.dp, 150.dp)

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(data) { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TableCell(text = rowData.Consultora, weight = columnWidths[0])
                TableCell(text = rowData.Nome, weight = columnWidths[1])
                TableCell(text = rowData.MesVenda, weight = columnWidths[2])
                TableCell(text = rowData.ValorVenda_PVP, weight = columnWidths[3])
                TableCell(text = rowData.ValorVenda_Liquido, weight = columnWidths[4])
                TableCell(text = rowData.Recrutadora, weight = columnWidths[5])
            }
        }
    }
}