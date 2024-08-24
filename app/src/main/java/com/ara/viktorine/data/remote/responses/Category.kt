package com.ara.viktorine.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("trivia_categories")
    val triviaCategories: List<TriviaCategory>
)