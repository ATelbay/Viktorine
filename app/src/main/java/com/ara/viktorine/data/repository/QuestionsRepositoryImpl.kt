package com.ara.viktorine.data.repository

import com.ara.viktorine.data.remote.OpenTdbApi
import com.ara.viktorine.data.remote.models.Difficulty
import com.ara.viktorine.data.remote.models.GameType
import com.ara.viktorine.data.remote.responses.Category
import com.ara.viktorine.data.remote.responses.GamePayload
import com.ara.viktorine.domain.repository.QuestionsRepository
import com.ara.viktorine.util.Resource

class QuestionsRepositoryImpl(
    private val api: OpenTdbApi
) : QuestionsRepository {

    override suspend fun getQuestions(
        amount: Int,
        categoryId: Int?,
        gameType: String?,
        difficulty: String,
    ): Resource<GamePayload> {
        val response = try {
            api.getQuestions(amount, categoryId, difficulty, gameType)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    override suspend fun getCategories(): Resource<Category> {
        val response = try {
            api.getCategories()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.\n${e}")
        }
        return Resource.Success(response)
    }
}