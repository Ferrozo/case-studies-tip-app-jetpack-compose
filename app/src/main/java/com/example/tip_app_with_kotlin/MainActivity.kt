package com.example.tip_app_with_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tip_app_with_kotlin.components.InputField
import com.example.tip_app_with_kotlin.ui.theme.Tip_app_with_kotlinTheme
import com.example.tip_app_with_kotlin.utils.calculateTotalPerPerson
import com.example.tip_app_with_kotlin.utils.calculateTotalTip
import com.example.tip_app_with_kotlin.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tip_app_with_kotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onBackground,
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun MyApp() {
    Column{
        MainContent()
    }

}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .clip(
                shape = RoundedCornerShape(12.dp)
            ),
        color = Color(0xFFE9D7F7),
    ) {
    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
    ){
        val total = "%.2f".format(totalPerPerson)
        Text(text ="Total Per Person", style = MaterialTheme.typography.headlineMedium)
        Text(text = "$$total", style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            )

    }
    }
}

@Composable
fun MainContent(){
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val splitByState = remember {
        mutableStateOf(1)
    }
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val range = IntRange(start= 1, endInclusive =100)


    Column (modifier = Modifier.padding(16.dp)) {
        BillForm(
            totalPerPersonState= totalPerPersonState,
            splitByState=splitByState,
            tipAmountState= tipAmountState,
            range=range,

            )

    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    onValueChanged: (String) -> Unit = {},
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
){
    val totalBillState = remember{
        mutableStateOf( "")
    }

    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sliderPositionState = remember{
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    TopHeader(
        totalPerPerson = totalPerPersonState.value
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
        ,
        shape = RoundedCornerShape(corner = CornerSize((8.dp))),
        border = BorderStroke(width = 1.dp, color= Color.Gray.copy(alpha = 0.5f)),
        color= Color.White

        ){
        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter your Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions
                    onValueChanged(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if(validState) {
                Row(
                    modifier = modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Start,
                ){
                    Text(
                        modifier =
                        modifier.align(alignment = Alignment.CenterVertically),
                        text ="Split",
                    )
                    Spacer(modifier = modifier.width(120.dp))

                    Row(
                        modifier = modifier.padding(horizontal = 0.dp),
                        horizontalArrangement =  Arrangement.End,
                    ){
                        RoundedIconButton(imageVector = Icons.Filled.Remove,

                            onClick = {
                                splitByState.value = if(splitByState.value > 1) splitByState.value -1 else 1

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(
                                        totalBill= totalBillState.value.toDouble(),
                                        tipPercentage= tipPercentage,
                                        splitBy= splitByState.value,
                                    )
                            }

                        )
                        Text(
                            modifier = modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .align(Alignment.CenterVertically),

                            text ="${splitByState.value}",

                            )
                        RoundedIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if(splitByState.value < range.last){
                                    splitByState.value = splitByState.value +1

                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(
                                            totalBill= totalBillState.value.toDouble(),
                                            tipPercentage= tipPercentage,
                                            splitBy= splitByState.value,
                                        )
                                }


                            }
                        )
                    }

                }

                Row(
                    modifier = modifier.padding(horizontal = 10.dp,  vertical = 10.dp)
                ){
                    Text(
                        modifier = modifier.align(Alignment.CenterVertically),
                        text="Tip"
                    )
                    Spacer(modifier = modifier.width(200.dp))
                    Text(
                        modifier = modifier.align(Alignment.CenterVertically),
                        text="${tipAmountState.value}",
                        style = TextStyle(
                            fontSize = 20.sp,
                        )
                    )


                }
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Text(
                        modifier = modifier.align(CenterHorizontally),
                        text=" $tipPercentage%",
                        style = TextStyle(
                            fontSize = 20.sp,
                        )
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Slider(
                        modifier = modifier.padding(horizontal = 16.dp),
                        steps = 5,
                        value = sliderPositionState.value,
                        onValueChange =
                        {
                            newValue ->
                            sliderPositionState.value = newValue
                            tipAmountState.value = calculateTotalTip(totalBill = totalBillState.value.toDouble(),tipPercentage= tipPercentage)
                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBill= totalBillState.value.toDouble(),
                                    tipPercentage= tipPercentage,
                                    splitBy= splitByState.value,
                                )
                        }

                    )

                }
            }else{
                Box{}
            }
        }

    }
}


