package com.app.chattyai.api

import com.app.chattyai.models.SignUp.SignUpExample
import com.app.chattyai.models.SignUp.SignupRequest
import com.app.chattyai.models.UploadUserProfileExample
import com.app.chattyai.models.getuser.GetUserExample
import com.app.chattyai.models.login.LoginExample
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface API {
    @POST("users/login")
    suspend fun loginExampleResponse(
     @Body jsonObject: JsonObject
    ): Response<LoginExample>

    @GET("users/getById/{id}")
    suspend fun getuserById(@Header("x-access-token")token:String,@Path("id")id:String):Response<GetUserExample>
    @POST("users/createUser")
    suspend fun signUpAPI(@Body signupRequest: SignupRequest):Response<SignUpExample>
    @Multipart
    @PUT("users/uploadProfile/{id}")
    suspend fun uploaduserprofile(@Header("x-access-token")token:String,@Path("id")id:String,@Part images: MultipartBody.Part):Response<UploadUserProfileExample>
}

