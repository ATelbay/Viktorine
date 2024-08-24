package com.ara.viktorine.data.remote.models

enum class GameType {
    ANY, MULTIPLE, BOOLEAN;

    override fun toString(): String {
        return name
    }
}