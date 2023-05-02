package com.app.chattyai.models.chatHistory

data class Answer(
    val __v: Int,
    val _id: String,
    val answer: String,
    val chatgptLog: String,
    val created_at: String,
    val is_deleted: Boolean,
    val updated_at: String,
    val user: String
)