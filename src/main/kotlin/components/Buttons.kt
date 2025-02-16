package components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeButtonOption(title: String, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = {onClick()},
        modifier = Modifier.width(150.dp).height(60.dp),//Largura de 150dp, altura de 60dp
        //elevation = ButtonDefaults.elevation(8.dp)//Sombra sutil (opcional). Removido porque outlined buttons não tem elevação por padrão
    ) {
        Text(title)
    }
}