package com.app.chattyai.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.chattyai.models.SignUp.SignUpExample
import com.app.chattyai.models.SignUp.SignupRequest
import com.app.chattyai.models.UploadUserProfileExample
import com.app.chattyai.models.getuser.GetUserExample
import com.app.chattyai.models.login.LoginExample
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.MyApplication
import com.app.chattyai.utils.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class UserModuleviewModel(
    app: Application,
    val repository: UserModuleResponseRepository
) : AndroidViewModel(app) { //inheriting from android view model to use application context
    //here we use application context to get the context throughout the app running,
    //so it will work even if the activity changes or destroys, the app context will still work until the app's running

    //LIVEDATA OBJECT
    val loginData: MutableLiveData<Resource<LoginExample>> = MutableLiveData()
    var loginresponse: LoginExample? = null
    //getuserByid

    val getuserByIdData: MutableLiveData<Resource<GetUserExample>> = MutableLiveData()
    var getuserResponse: GetUserExample? = null

    //signup__________________________
    val signupData: MutableLiveData<Resource<SignUpExample>> = MutableLiveData()
    var signupresponse: SignUpExample? = null

    //uploaduserprofile____________________________________________________________________
    val userUploadimageLiveData: LiveData<Resource<UploadUserProfileExample>> get() = repository.uploaduserprofilelivedata


    fun loginMethod(
        email: String, password: String
    ) = viewModelScope.launch {
        loginmethod(
            email,
            password
        )
    }

    private suspend fun loginmethod(email: String, password: String) {
        loginData.postValue(Resource.Loading())
        try {

            val jsonObject = JsonObject()
            jsonObject.addProperty("email", email)
            jsonObject.addProperty("password", password)

            if (hasInternetConnection()) {
                val response = repository.loginExampleResponse(
                    jsonObject
                )
                //handling response
                handleloginresponse(response)
            } else {
                loginData.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> loginData.postValue(Resource.Error("Network Failure"))
                else -> loginData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun handleloginresponse(response: Response<LoginExample>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                loginData.postValue(Resource.Success(response.body()!!))
//                return Resource.Success(loginresponse ?: resultResponse)
            }
        } else if (response.errorBody() != null) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody).getString("message")
            } catch (e: Exception) {
                "Unknown error"
            }
            loginData.postValue(Resource.Error(errorMessage))

//            return Resource.Error(errorMessage, loginresponse)
        } else {
            loginData.postValue(Resource.Error("errorMessage"))

        }


    }

    //getuserById_________________________________________________________________________________________________________________

    fun getuserById(token: String, id: String) = viewModelScope.launch {
        getUserByIDMethod(token, id)
    }

    private suspend fun getUserByIDMethod(token: String, id: String) {
        getuserByIdData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getuserById(token, id)
                getuserByIdData.postValue(handlegetUserByIdResponse(response))

            } else {
                getuserByIdData.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> loginData.postValue(Resource.Error("Network Failure"))
                else -> loginData.postValue(Resource.Error("Conversion Error"))
            }

        }

    }

    private fun handlegetUserByIdResponse(response: Response<GetUserExample>): Resource<GetUserExample> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getuserResponse ?: resultResponse)
            }

        }
        return Resource.Error(response.message())
    }

    //signupAPI__________________________________________________________________________________________________________________________
    fun signUpMethod(signup: SignupRequest) = viewModelScope.launch {
        signup(signup)
    }

    private suspend fun signup(signup: SignupRequest) {
        signupData.postValue(Resource.Loading())

        if (hasInternetConnection()) {
            try {
                val response = repository.signUpMehtod(signup)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        signupData.postValue(Resource.Success(resultResponse))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Unknown error"
                    } else {
                        try {
                            JSONObject(errorBody).getString("message")
                        } catch (e: JSONException) {
                            "Unknown error"
                        }
                    }
                    signupData.postValue(Resource.Error(errorMessage))
                }
            } catch (e: IOException) {
                signupData.postValue(Resource.Error("Network Failure"))
            }
        } else {
            signupData.postValue(Resource.Error("No Internet Connection"))
        }
    }


    //uploaduserimage_______________________________________________________________________________

    fun uploaduserimage(token: String, id: String, file: MultipartBody.Part) =
        viewModelScope.launch {
            repository.uploadprofileimage(token, id, file)
        }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}