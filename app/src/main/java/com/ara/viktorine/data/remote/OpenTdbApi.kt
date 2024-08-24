package com.ara.viktorine.data.remote

import com.ara.viktorine.data.remote.models.GameType
import com.ara.viktorine.data.remote.responses.Category
import com.ara.viktorine.data.remote.responses.GamePayload
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTdbApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") categoryId: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") gameType: String?,
    ): GamePayload

    @GET("api_category.php")
    suspend fun getCategories(): Category
}