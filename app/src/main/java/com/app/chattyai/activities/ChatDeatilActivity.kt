package com.app.chattyai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.chattyai.R
import com.app.chattyai.adapters.ChatAdapterRecyclerview
import com.app.chattyai.databinding.ActivityChatDeatilBinding
import com.app.chattyai.models.Messages
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.USerViewModelFactory
import com.app.chattyai.viewmodel.UserModuleviewModel
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class ChatDeatilActivity : AppCompatActivity(), View.OnClickListener {
    private var messageList: ArrayList<Messages> = ArrayList()
    private var chatAdapterRecyclerview: ChatAdapterRecyclerview? = null

    lateinit var viewModel: UserModuleviewModel

    private var binding: ActivityChatDeatilBinding? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDeatilBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val repository = UserModuleResponseRepository()
        val viewModelProviderFactory = USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)

        listners()
        initrecyclerview()


    }

    private fun listners() {
        binding?.imgsend?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
    }

    private fun initrecyclerview() {
        chatAdapterRecyclerview = ChatAdapterRecyclerview(
            messageList,
            binding?.imgsend,
            binding?.chatrecyclerview
        )
        binding!!.chatrecyclerview!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.chatrecyclerview?.adapter = chatAdapterRecyclerview
    }


    private fun addMessage(time: String, message: String, type: Int) {
        messageList.add(Messages.Builder(type).time(time).message(message).build())
        chatAdapterRecyclerview!!.notifyItemInserted(messageList!!.size)
//        scrollToBottom();
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgsend -> {
                val message: String = binding!!.etmsg?.getText().toString()
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val strDate = mdformat.format(calendar.time)
                addMessage(strDate, message, Messages.TYPE_MESSAGE)
                val question=  binding!!.etmsg?.text.toString().trim()
                if( binding!!.etmsg?.text!!.isNotEmpty()){
                    getResponse(question) { response ->
                        runOnUiThread {
                            addMessage(strDate, response, Messages.TYPE_MESSAGE_REMOTE)

                        }
                    }
                }

                binding!!.etmsg?.setText("")




            }
            R.id.imgback -> finish()
        }
    }

    fun getResponse(question: String, callback: (String) -> Unit){
        val apiKey="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"
        val url="https://api.openai.com/v1/engines/text-davinci-003/completions"

        val requestBody="""
            {
            "prompt": "$question",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed",e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body=response.body?.string()
                if (body != null) {
                    Log.v("data",body)
                }
                else{
                    Log.v("data","empty")
                }
                val jsonObject= JSONObject(body)
                val jsonArray: JSONArray =jsonObject.getJSONArray("choices")
                val textResult=jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }
        })
    }


}