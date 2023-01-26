package com.example.chatapplication.model

data class UserRequest (
    val prompt: String = " A white tiger in jungle",
    val n: Int = 1,
    val size: String = "256x256"
)

