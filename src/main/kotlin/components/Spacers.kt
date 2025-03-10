package components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun BlankVerticalSpace(){
    Spacer(modifier = Modifier.height(15.dp) )
}

@Composable
fun BlankHorizontalSpace(){
    Spacer(modifier = Modifier.width(15.dp) )
}