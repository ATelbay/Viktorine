package com.ara.viktorine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.ui.theme.Cyan700
import com.ara.viktorine.ui.theme.Green100
import com.ara.viktorine.ui.theme.Grey100

@Composable
fun <T>TriStateSwitch(
    states: List<T>,
    onSelectionChange: (T) -> Unit
) {
    var selectedOption by remember {
        mutableStateOf(states.first())
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .wrapContentHeight()
                .background(Green100),
        ) {
            states.forEach { state ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(CenterVertically)
                        .weight(1f)
                        .fillMaxSize()
                        .background(if (state == selectedOption) Cyan700 else Grey100)
                        .clickable {
                            onSelectionChange(state)
                            selectedOption = state
                        }
                ) {
                    Text(
                        text = state.toString(),
                        color = if (state == selectedOption) Color.White else Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TriStateSwitchPreview() {
    val states = listOf(
        Difficulty.EASY,
        Difficulty.MEDIUM,
        Difficulty.HARD,
    )

    TriStateSwitch(
        states = states,
        onSelectionChange = {}
    )
}