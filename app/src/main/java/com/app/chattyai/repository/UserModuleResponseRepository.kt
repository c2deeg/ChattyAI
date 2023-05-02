package com.app.chattyai.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.chattyai.api.RetrofitInstance
import com.app.chattyai.models.SignUp.SignupRequest
import com.app.chattyai.models.UploadUserProfileExample
import com.app.chattyai.models.getuser.GetUserExample
import com.app.chattyai.models.login.LoginExample
import com.app.chattyai.utils.Resource
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class UserModuleResponseRepository {
    val uploaduserprofilelivedata: MutableLiveData<Resource<UploadUserProfileExample>> =
        MutableLiveData()
    var useruploadprofileresponse: UploadUserProfileExample? = null


    suspend fun loginExampleResponse(jsonObject: JsonObject) =
        RetrofitInstance.api.loginExampleResponse(jsonObject)

    suspend fun getuserById(token: String, id: String) = RetrofitInstance.api.getuserById(token, id)
    suspend fun signUpMehtod(signupRequest: SignupRequest) =
        RetrofitInstance.api.signUpAPI(signupRequest)


    suspend fun uploadprofileimage(token: String, id: String, file: MultipartBody.Part) {
        uploaduserprofilelivedata.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.uploaduserprofile(token, id, file)
            handlegetUploaduserResponse(response)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> uploaduserprofilelivedata.postValue(Resource.Error("Network Failure"))
                else -> uploaduserprofilelivedata.postValue(Resource.Error("Conversion Error"))
            }

        }

    }

    private fun handlegetUploaduserResponse(response: Response<UploadUserProfileExample>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                uploaduserprofilelivedata.postValue(Resource.Success(response.body()!!))
//                return Resource.Success(loginresponse ?: resultResponse)
            }
        } else if (response.errorBody() != null) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody).getString("message")
            } catch (e: Exception) {
                "Unknown error"
            }
            uploaduserprofilelivedata.postValue(Resource.Error(errorMessage))

//            return Resource.Error(errorMessage, loginresponse)
        } else {
            uploaduserprofilelivedata.postValue(Resource.Error("errorMessage"))

        }


    }





}