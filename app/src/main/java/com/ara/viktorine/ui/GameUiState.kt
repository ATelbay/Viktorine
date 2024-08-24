package com.ara.viktorine.ui

import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.data.remote.models.GameType
import com.ara.viktorine.data.remote.models.QuestionsListEntry
import com.ara.viktorine.data.remote.responses.TriviaCategory

data class GameUiState(
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val currentQuestionCount: Int = 1,
    val categoryList: List<TriviaCategory> = emptyList(),
    val selectedCategory: TriviaCategory? = null,
    val difficulty: Difficulty = Difficulty.EASY,
    val gameType: GameType = GameType.ANY,
    val questionsList: List<QuestionsListEntry> = emptyList()
)