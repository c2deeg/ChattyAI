package com.app.chattyai.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.chattyai.api.RetrofitInstance
import com.app.chattyai.models.UploadUserProfileExample
import com.app.chattyai.models.chatHistory.ChatHistoryExample
import com.app.chattyai.models.chatgptresponse.ChatGptExample
import com.app.chattyai.models.getuser.GetUserExample
import com.app.chattyai.utils.Resource
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class ChatGptRepository {

    val chatgptresponseLiveData: MutableLiveData<Resource<ChatGptExample>> = MutableLiveData()
    val chatGptHistoryLiveData: MutableLiveData<Resource<ChatHistoryExample>> = MutableLiveData()



    suspend fun chatGptresponsefun(search: String, id: String) {
        chatgptresponseLiveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.chatgptapi.chatGptAPI(search, id)
            handleChatGptapiResponse(response)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> chatgptresponseLiveData.postValue(Resource.Error("Network Failure"))
                else -> chatgptresponseLiveData.postValue(Resource.Error("Conversion Error"))
            }

        }

    }

    private fun handleChatGptapiResponse(response: Response<ChatGptExample>) {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                chatgptresponseLiveData.postValue(Resource.Success(response.body()!!))
            }
        } else if (response.errorBody() != null) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody).getString("message")
            } catch (e: Exception) {
                "Unknown error"
            }
            chatgptresponseLiveData.postValue(Resource.Error(errorMessage))
        } else {
            chatgptresponseLiveData.postValue(Resource.Error("errorMessage"))

        }
    }
    //chathistory________________________________________________________________________________________________
    suspend fun chathistoryFun(userid:String){
        chatGptHistoryLiveData.postValue(Resource.Loading())
        try {
            val response  = RetrofitInstance.chatgptapi.chatHistoryAPI(userid)
            handleHistoryResponse(response)
        }catch (t:Throwable){
            when (t) {
                is IOException -> chatGptHistoryLiveData.postValue(Resource.Error("Network Failure"))
                else -> chatGptHistoryLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun handleHistoryResponse(response: Response<ChatHistoryExample>){
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                chatGptHistoryLiveData.postValue(Resource.Success(response.body()!!))
            }
        } else if (response.errorBody() != null) {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody).getString("message")
            } catch (e: Exception) {
                "Unknown error"
            }
            chatGptHistoryLiveData.postValue(Resource.Error(errorMessage))
        } else {
            chatGptHistoryLiveData.postValue(Resource.Error("errorMessage"))

        }

    }
}