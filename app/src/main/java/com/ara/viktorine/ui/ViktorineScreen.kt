package com.ara.viktorine.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ara.viktorine.R
import com.ara.viktorine.ui.screen.HomeScreen
import com.ara.viktorine.ui.screen.QuestionEntry
import com.ara.viktorine.ui.screen.ResultScreen
import com.ara.viktorine.util.Constants
import org.koin.androidx.compose.koinViewModel

// Navigation destinations
enum class ViktorineApp {
    Start,
    Game,
    Result
}

@Composable
fun ViktorineScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(),
) {
    // Game UI
    val isLoading = viewModel.isLoading.value
    val gameUiState by viewModel.uiState.collectAsState()

    // Navigation parameters
    val navController = rememberNavController()

    Scaffold(
        topBar = { ViktorineTopBar() },
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        NavHost(
            navController = navController,
            startDestination = ViktorineApp.Start.name,
            modifier = modifier.padding(it)
        ) {
            composable(ViktorineApp.Start.name) {
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (viewModel.errorMessage.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error happened",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                        )

                        Button(
                            onClick = {
                                viewModel.loadCategories()
                            }
                        ) {
                            Text(
                                text = "Retry",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                } else HomeScreen(
                    onStartButtonClick = {
                        viewModel.loadQuestions {
                            navController.navigate(ViktorineApp.Game.name) {
                                popUpTo(ViktorineApp.Start.name) { inclusive = true }
                            }
                        }
                    },
                    onDifficultyChanged = { difficulty ->
                        viewModel.selectDifficulty(difficulty)
                    },
                    onGameTypeChanged = { gameType ->
                        viewModel.selectGameType(gameType)
                    },
                    onSubmitButtonClick = viewModel::selectCategory,
                    categoryList = gameUiState.categoryList,
                    selectedOption = gameUiState.selectedCategory
                )

            }
            composable(ViktorineApp.Game.name) {
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        GameStatus(
                            questionCount = gameUiState.currentQuestionCount,
                            questionsNumber = Constants.QUESTIONS_NUMBER,
                            score = gameUiState.score
                        )

                        QuestionEntry(
                            entry = gameUiState.questionsList[gameUiState.currentQuestionCount - 1],
                            questionNumber = gameUiState.currentQuestionCount,
                            nextQuestion = { viewModel.nextQuestion() },
                            checkUserAnswer = { viewModel.checkUserAnswer(it) },
                            goToResultScreen = {
                                navController.navigate(ViktorineApp.Result.name) {
                                    popUpTo(ViktorineApp.Game.name) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
            composable(ViktorineApp.Result.name) {
                ResultScreen(
                    finalScore = gameUiState.score,
                    goToStartScreen = {
                        navController.navigate(ViktorineApp.Start.name) {
                            popUpTo(ViktorineApp.Result.name) { inclusive = true }
                            viewModel.resetGame(500)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ViktorineTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = "Quiz icons created by Flaticon"
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h1
        )
    }
}

@Composable
fun GameStatus(
    questionCount: Int,
    questionsNumber: Int,
    score: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(24.dp),
    ) {
        Text(
            text = stringResource(R.string.question_count, questionCount, questionsNumber),
            fontSize = 18.sp,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            text = stringResource(R.string.score, score),
            fontSize = 18.sp,
        )
    }
}