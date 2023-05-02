package com.app.chattyai.models.chatHistory

data class Data(
    val __v: Int,
    val _id: String,
    val answers: List<Answer>,
    val created_at: String,
    val is_deleted: Boolean,
    val question: String,
    val updated_at: String,
    val user: String
)