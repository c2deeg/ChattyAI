package com.app.chattyai.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.chattyai.repository.ChatGptRepository
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.viewmodel.ChatGptresponseViewModel
import com.app.chattyai.viewmodel.UserModuleviewModel

class ChatGptviewmodelFactory (val app: Application,
                               val chatGptRepository: ChatGptRepository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatGptresponseViewModel(app, chatGptRepository) as T
    }
}