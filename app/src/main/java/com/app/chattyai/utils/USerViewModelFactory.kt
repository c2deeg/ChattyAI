package com.app.chattyai.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.viewmodel.UserModuleviewModel

class USerViewModelFactory (val app: Application,
                            val newsRepository: UserModuleResponseRepository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserModuleviewModel(app, newsRepository) as T
    }
}