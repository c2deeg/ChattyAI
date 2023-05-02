package com.app.chattyai.models.chatgptresponse

data class ChatGptExample(
    val `data`: List<Data>,
    val isSuccess: Boolean,
    val statusCode: Int
)