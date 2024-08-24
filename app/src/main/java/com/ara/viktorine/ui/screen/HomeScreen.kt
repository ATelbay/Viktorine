package com.ara.viktorine.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ara.viktorine.R
import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.data.remote.models.GameType
import com.ara.viktorine.data.remote.responses.TriviaCategory
import com.ara.viktorine.ui.ViktorineTopBar
import com.ara.viktorine.ui.components.ListDialog
import com.ara.viktorine.ui.components.TriStateSwitch
import com.ara.viktorine.ui.theme.ViktorineTheme

/**
 * Экран выбора опций для викторины.
 * Содержит выбор уровня сложности, выбор режима игры
 * (несколько вариантов или правда/лож), выбор категории
 * и начало игры
 * */
@Composable
fun HomeScreen(
    onStartButtonClick: () -> Unit,
    onSubmitButtonClick: (TriviaCategory) -> Unit,
    onDifficultyChanged: (Difficulty) -> Unit,
    onGameTypeChanged: (GameType) -> Unit,
    categoryList: List<TriviaCategory>,
    selectedOption: TriviaCategory?,
    modifier: Modifier = Modifier
) {
    var dialogState by remember {
        mutableStateOf(false)
    }

    if (dialogState) {
        ListDialog(
            title = "Select Category",
            optionsList = categoryList,
            defaultOption = selectedOption,
            submitButtonText = "Continue",
            onSubmitButtonClick = {
                onSubmitButtonClick(it)
                dialogState = false
            },
            onDismissRequest = {
                dialogState = false
            },
            content = {
                Text(text = it.name)
            }
        )
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "Quiz icons created by Flaticon"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quiz App",
            style = MaterialTheme.typography.h2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Learn - Take Quiz - Repeat",
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { onStartButtonClick() }
        ) {
            Text(
                text = "Play",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                dialogState = true
            }
        ) {
            Text(
                text = "Select Category",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val difficultyState by remember {
            mutableStateOf(Difficulty.entries)
        }

        TriStateSwitch(difficultyState) { difficulty ->
            onDifficultyChanged(difficulty)
        }

        Spacer(modifier = Modifier.height(8.dp))

        val gameTypes by remember {
            mutableStateOf(GameType.entries)
        }

        TriStateSwitch(gameTypes) { gameType ->
            onGameTypeChanged(gameType)
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ViktorineTheme {
        Scaffold(
            topBar = { ViktorineTopBar() },
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            HomeScreen(
                modifier = Modifier.padding(it),
                onStartButtonClick = {},
                categoryList = listOf(),
                onSubmitButtonClick = {},
                selectedOption = null,
                onDifficultyChanged = {},
                onGameTypeChanged = {}
            )
        }
    }
}