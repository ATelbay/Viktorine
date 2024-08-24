package com.ara.viktorine.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ara.viktorine.data.remote.models.QuestionsListEntry
import com.ara.viktorine.util.Constants.QUESTIONS_NUMBER

/**
 *  Экран отображает вопрос и ответы (от 2 до 4) из которых 1 правильный
 *  и кнопку для следующего ответа
 * */
@Composable
fun QuestionEntry(
    entry: QuestionsListEntry,
    questionNumber: Int,
    nextQuestion: () -> Unit,
    checkUserAnswer: (String) -> Unit,
    goToResultScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var guessed by rememberSaveable { mutableStateOf(false) }
    var selectedAnswer by rememberSaveable { mutableStateOf("") }
    var answered by rememberSaveable { mutableStateOf(selectedAnswer.isNotEmpty()) }
    val initialTextStyle = MaterialTheme.typography.h6
    var textStyle by remember { mutableStateOf(initialTextStyle) }
    var readyToDraw by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(8.dp)
    ) {
        Card(
            elevation = 4.dp,
            modifier = modifier.weight(1.5f)
        ) {
            Text(
                text = entry.question,
                fontSize = 20.sp,
                style = textStyle,
                overflow = TextOverflow.Clip,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.didOverflowHeight) {
                        textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                    } else {
                        readyToDraw = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .drawWithContent {
                        if (readyToDraw) drawContent()
                    }
            )
        }

        Column(
            modifier = modifier.weight(2.5f)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            val lastAnswer = entry.answers.last()
            for (answer in entry.answers) {
                AnswerEntry(
                    answer = answer,
                    enabled = !answered,
                    isCorrect = entry.correctAnswer == answer,
                    isSelected = selectedAnswer == answer,
                    onClick = {
                        answered = true
                        if (entry.correctAnswer == answer) {
                            guessed = true
                        }
                        selectedAnswer = answer
                        checkUserAnswer(selectedAnswer)
                    }
                )
                if (answer != lastAnswer) Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (answered) {
            Text(
                text = if (guessed) "Correct answer!" else "Wrong answer!",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
                    .weight(.5f),
                textAlign = TextAlign.Center
            )
        } else {
            answered = false
            guessed = false
            Text(
                text = "",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(.5f),
                textAlign = TextAlign.Center
            )
        }
        if (questionNumber < QUESTIONS_NUMBER) {
            Button(
                modifier = modifier.weight(.5f),
                enabled = answered,
                onClick = {
                    nextQuestion()
                    answered = false
                }
            ) {
                Text(
                    text = "NEXT QUESTION",
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Button(
                modifier = modifier.weight(.5f),
                onClick = {
                    goToResultScreen()
                }
            ) {
                Text(
                    text = "RESULT",
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun AnswerEntry(
    answer: String,
    enabled: Boolean,
    isCorrect: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialTextStyle = MaterialTheme.typography.body1
    var textStyle by remember { mutableStateOf(initialTextStyle) }
    var readyToDraw by remember { mutableStateOf(false) }
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor =
            if (isCorrect)
                Color.Green
            else if (isSelected && !isCorrect)
                Color.Red
            else
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Text(
            text = answer,
            color = MaterialTheme.colors.onPrimary,
            style = textStyle,
            maxLines = 1,
            softWrap = false,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowWidth) {
                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                } else {
                    readyToDraw = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .drawWithContent {
                    if (readyToDraw) {
                        drawContent()
                    }
                },
            textAlign = TextAlign.Center,
        )
    }
}