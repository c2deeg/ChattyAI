package com.app.chattyai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.chattyai.R
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.USerViewModelFactory
import com.app.chattyai.viewmodel.UserModuleviewModel

class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: UserModuleviewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)
    }
}