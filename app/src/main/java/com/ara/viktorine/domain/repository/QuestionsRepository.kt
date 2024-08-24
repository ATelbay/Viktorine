package com.ara.viktorine.domain.repository

import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.data.remote.responses.Category
import com.ara.viktorine.data.remote.responses.GamePayload
import com.ara.viktorine.data.remote.responses.TriviaCategory
import com.ara.viktorine.util.Resource

interface QuestionsRepository {
    suspend fun getQuestions(
        amount: Int,
        categoryId: Int?,
        gameType: String?,
        difficulty: String,
    ): Resource<GamePayload>

    suspend fun getCategories(): Resource<Category>
}