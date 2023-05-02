package com.app.chattyai.models.chatgptresponse

data class Data(
    val finish_reason: String,
    val index: Int,
    val logprobs: Any,
    val text: String
)