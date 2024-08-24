package com.ara.viktorine.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> ListDialog(
    title: String,
    optionsList: List<T>,
    defaultOption: T?,
    submitButtonText: String,
    onSubmitButtonClick: (T) -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable RowScope.(T) -> Unit
) {
    var selectedOption by remember {
        mutableStateOf(defaultOption ?: optionsList.firstOrNull())
    }

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(modifier = Modifier.padding(10.dp)) {

                Text(
                    fontSize = 20.sp,
                    text = title
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(modifier = Modifier.height(500.dp)) {
                    items(optionsList) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == it,
                                onClick = { selectedOption = it }
                            )

                            content(it)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        selectedOption?.let(onSubmitButtonClick) ?: onDismissRequest()
                    },
                    enabled = optionsList.isNotEmpty(),
                ) {
                    Text(
                        text = submitButtonText,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun ListDialogPreview() {
    ListDialog(
        title = "Select category",
        optionsList = listOf("Entertainment", "Sport", "IT", "Science"),
        defaultOption = null,
        submitButtonText = "Select",
        onSubmitButtonClick = {},
        onDismissRequest = {},
        content = {})
}