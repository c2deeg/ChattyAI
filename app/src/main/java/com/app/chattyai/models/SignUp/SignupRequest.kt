package com.app.chattyai.models.SignUp

data class SignupRequest(
    val deviceToken: String,
    val email: String,
    val fullname: String,
    val is_activated: Boolean,
    val is_deleted: Boolean,
    val password: String,
    val phone: String
)