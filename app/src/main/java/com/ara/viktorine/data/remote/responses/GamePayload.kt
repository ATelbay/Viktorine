package com.ara.viktorine.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GamePayload(
    @SerializedName("response_code")
    val responseCode: Int,
    val results: List<Result>
)