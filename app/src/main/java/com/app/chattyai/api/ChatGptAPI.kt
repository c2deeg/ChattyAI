package com.app.chattyai.api

import com.app.chattyai.models.chatHistory.ChatHistoryExample
import com.app.chattyai.models.chatgptresponse.ChatGptExample
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatGptAPI {
    @GET("chatgpt/search")
    suspend fun chatGptAPI(@Query("search")search:String, @Query("userId")userid:String):Response<ChatGptExample>
    @GET("chatgpt/getHistory/{userId}")
    suspend fun chatHistoryAPI(@Path("userId")userid: String):Response<ChatHistoryExample>
}