package com.app.chattyai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.chattyai.models.UploadUserProfileExample
import com.app.chattyai.models.chatHistory.ChatHistoryExample
import com.app.chattyai.models.chatgptresponse.ChatGptExample
import com.app.chattyai.models.getuser.GetUserExample
import com.app.chattyai.repository.ChatGptRepository
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.Resource
import kotlinx.coroutines.launch
class ChatGptresponseViewModel(
    app: Application,
    val repository: ChatGptRepository
) : AndroidViewModel(app) {

    val chatgptresponseLiveData: MutableLiveData<Resource<ChatGptExample>> = repository.chatgptresponseLiveData
    val chatHistoryLiveData:MutableLiveData<Resource<ChatHistoryExample>> = repository.chatGptHistoryLiveData


    fun chatgptfunc(search:String,id:String)= viewModelScope.launch {
        repository.chatGptresponsefun(search,id)
    }

    fun chatHistoryfunc(userid:String) = viewModelScope.launch {
        repository.chathistoryFun(userid)
    }


}
