package com.app.chattyai.models.login

data class Data(
    val _id: String,
    val createdAt: String,
    val deviceToken: String,
    val email: String,
    val fullname: String,
    val image: String,
    val is_activated: Boolean,
    val phone: String,
    val role: String,
    val token: String,
    val updatedAt: String
)