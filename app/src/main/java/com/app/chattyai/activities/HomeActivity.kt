package com.app.chattyai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.app.chattyai.R
import com.app.chattyai.repository.ChatGptRepository
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.ChatGptviewmodelFactory
import com.app.chattyai.utils.USerViewModelFactory
import com.app.chattyai.viewmodel.ChatGptresponseViewModel
import com.app.chattyai.viewmodel.UserModuleviewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var viewModel: UserModuleviewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        HomeActivity.bottomnav = findViewById(R.id.bottomnav)

//        val chatGptRepository = ChatGptRepository()
//        val chatviewmodelproviderFactory = ChatGptviewmodelFactory(application, chatGptRepository)
//        chatGptresponseViewModel = ViewModelProvider(
//            this,
//            chatviewmodelproviderFactory
//        ).get(ChatGptresponseViewModel::class.java)

        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)
    }

    companion object {
        public var bottomnav: BottomNavigationView? = null
    }
}
