package com.example.tip_app_with_kotlin.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val IconButtonSize = Modifier.size(40.dp)

@Composable
fun RoundedIconButton(
    modifier : Modifier = Modifier,
    imageVector: ImageVector,
    onClick: ()-> Unit,
    tint: Color = Color.Black.copy(0.8f),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp,
){

    Card(
        modifier= modifier
            .padding(all = 4.dp)
            .clickable { onClick.invoke() }
            .then((IconButtonSize)),
        shape = CircleShape,
        colors =   CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation=
        CardDefaults.cardElevation(
            defaultElevation=elevation ,
        )
    ){
        Icon(
            modifier = modifier.fillMaxSize()
                .padding(10.dp)
            ,

            imageVector = imageVector, contentDescription ="less or plus",
            tint = tint,
            )

    }
}