package com.app.chattyai.models.chatHistory

data class ChatHistoryExample(
    val `data`: List<Data>,
    val isSuccess: Boolean,
    val statusCode: Int
)