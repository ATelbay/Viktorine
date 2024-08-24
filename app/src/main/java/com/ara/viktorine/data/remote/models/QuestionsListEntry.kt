package com.ara.viktorine.data.remote.models

data class QuestionsListEntry(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)
