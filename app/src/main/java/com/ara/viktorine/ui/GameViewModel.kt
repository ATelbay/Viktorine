package com.ara.viktorine.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.data.remote.models.GameType
import com.ara.viktorine.data.remote.models.QuestionsListEntry
import com.ara.viktorine.data.remote.responses.TriviaCategory
import com.ara.viktorine.domain.repository.QuestionsRepository
import com.ara.viktorine.util.Constants.QUESTIONS_NUMBER
import com.ara.viktorine.util.Constants.SCORE_INCREASE
import com.ara.viktorine.util.Resource
import com.ara.viktorine.util.htmlToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: QuestionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var errorMessage by mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var currentAnswer by mutableStateOf("")
    private var categoryList: MutableList<TriviaCategory> = mutableListOf()

    init {
        resetGame()
        loadCategories()
    }

    fun resetGame(delay: Long = 0L) {
        viewModelScope.launch {
            delay(delay)
            _uiState.value = GameUiState(
                score = 0,
                isGameOver = false,
                currentQuestionCount = 1,
                questionsList = emptyList(),
                selectedCategory = uiState.value.selectedCategory,
                categoryList = categoryList
            )
        }
    }

    fun selectCategory(triviaCategory: TriviaCategory) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(selectedCategory = triviaCategory)
            }
        }
    }

    fun selectDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(difficulty = difficulty)
            }
        }
    }

    fun selectGameType(gameType: GameType) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(gameType = gameType)
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage = ""

            when (val result = repository.getCategories()) {
                is Resource.Success -> {
                    categoryList.clear()
                    result.data?.triviaCategories?.let(categoryList::addAll)

                    _uiState.update { currentState ->
                        currentState.copy(categoryList = categoryList)
                    }

                    Log.d("ARA", "Category: $categoryList")


                    isLoading.value = false
                    errorMessage = ""
                }

                is Resource.Error -> {
                    Log.d("ARA", result.message.toString())
                    errorMessage = result.message.toString()
                    isLoading.value = false
                }

            }
        }
    }

    fun loadQuestions(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage = ""

            val categoryId = uiState.value.selectedCategory?.id
            val difficulty = uiState.value.difficulty.name
            val gameType = if (uiState.value.gameType == GameType.ANY) null
            else uiState.value.gameType.name

            val result = repository.getQuestions(
                amount = QUESTIONS_NUMBER,
                categoryId = categoryId,
                gameType = gameType?.lowercase(),
                difficulty = difficulty.lowercase(),
            )

            when (result) {
                is Resource.Success -> {
                    if (result.data?.responseCode != 0) {
                        Log.d("ARA", "API error! Code — ${result.data?.responseCode}")
                    } else {
                        Log.d("ARA", result.data.results.toString())
                        val randomQuestions = result.data.results.mapIndexed { _, entry ->
                            val answers = mutableListOf<String>()
                            answers.addAll(entry.incorrectAnswers)
                            answers.add(entry.correctAnswer)
                            answers.shuffle()
                            QuestionsListEntry(
                                question = htmlToString(entry.question),
                                answers = answers.map { htmlToString(it) },
                                correctAnswer = htmlToString(entry.correctAnswer)
                            )
                        }
                        Log.d("ARA", randomQuestions.toString())
                        _uiState.update { currentState ->
                            onSuccess()
                            currentState.copy(questionsList = randomQuestions)
                        }
                    }
                    errorMessage = ""
                    isLoading.value = false
                }

                is Resource.Error -> {
                    Log.d("ARA", result.message.toString())
                    errorMessage = result.message.toString()
                    isLoading.value = false
                }
            }
        }
    }

    private fun updateCurrentAnswer(newAnswer: String) {
        currentAnswer = newAnswer
    }

    fun checkUserAnswer(newAnswer: String) {
        updateCurrentAnswer(newAnswer)
        val questionList = _uiState.value.questionsList
        val questionIndex = _uiState.value.currentQuestionCount - 1

        val correctAnswer = questionList.getOrNull(questionIndex )?.correctAnswer ?: return

        if (currentAnswer == correctAnswer) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameScore(updatedScore)
        }
    }

    fun updateGameScore(updatedScore: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                score = updatedScore
            )
        }
    }

    fun nextQuestion() {
        if (_uiState.value.currentQuestionCount < QUESTIONS_NUMBER) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentQuestionCount = _uiState.value.currentQuestionCount.plus(1)
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true
                )
            }
        }
        updateCurrentAnswer("")
    }
}