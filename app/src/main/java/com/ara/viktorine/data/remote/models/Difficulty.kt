package com.ara.viktorine.data.remote.models

enum class Difficulty {
    EASY, MEDIUM, HARD;

    override fun toString(): String {
        return name
    }
}