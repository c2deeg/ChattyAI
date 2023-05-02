package com.app.chattyai.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.chattyai.R
import com.app.chattyai.activities.HomeActivity
import com.app.chattyai.adapters.ChatAdapterRecyclerview
import com.app.chattyai.databinding.FragmentChatBinding
import com.app.chattyai.models.Messages
import com.app.chattyai.repository.ChatGptRepository
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.ChatGptviewmodelFactory
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.ChatGptresponseViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentChatBinding? = null
    private var messageList: ArrayList<Messages> = ArrayList()
    private var chatAdapterRecyclerview: ChatAdapterRecyclerview? = null
    private val client = OkHttpClient()
    var viewModel: ChatGptresponseViewModel? = null
    lateinit var userid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        listners()
        initrecyclerview()
        val repository = ChatGptRepository()

        val viewModelProviderFactory =
            ChatGptviewmodelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(
                this,
                viewModelProviderFactory
            ).get(ChatGptresponseViewModel::class.java)
        userid = CSPreferences.readString(requireActivity(), Utils.USERID)!!

        return binding!!.root

    }

    private fun listners() {
        binding?.imgsend?.setOnClickListener(this)
        binding?.imgprofile?.setOnClickListener(this)
    }

    private fun initrecyclerview() {
        chatAdapterRecyclerview = ChatAdapterRecyclerview(messageList, binding?.imgsend,binding?.chatrecyclerview)
        binding!!.chatrecyclerview!!.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.chatrecyclerview?.adapter = chatAdapterRecyclerview
    }


    private fun addMessage(time: String, message: String, type: Int) {
        messageList.add(Messages.Builder(type).time(time).message(message).build())
        chatAdapterRecyclerview!!.notifyItemInserted(messageList!!.size)
        scrollToBottom();
    }

    private fun scrollToBottom() {
        binding?.chatrecyclerview?.let { recyclerView ->
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(messageList.size - 1, 0)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgsend -> {
                val message: String = binding!!.etmsg?.getText().toString()
                val calendar = Calendar.getInstance()
                val mdformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val strDate = mdformat.format(calendar.time)
                addMessage(strDate, message, Messages.TYPE_MESSAGE)
                val question = binding!!.etmsg?.text.toString().trim()
                if (binding!!.etmsg?.text!!.isNotEmpty()) {
                    viewModel?.chatgptfunc(binding!!.etmsg.text.toString(), userid)
                    bindobservers()
//                    getResponse(question) { response ->
//                        activity?.runOnUiThread {
//                            addMessage(strDate, response, Messages.TYPE_MESSAGE_REMOTE)
//                        }
//                    }
                }

                binding!!.etmsg?.setText("")

            }

            R.id.imgprofile -> {
                HomeActivity.bottomnav?.visibility = View.GONE
                findNavController().navigate(R.id.action_welcomeChattyFragment_to_profileFragment2)
            }

        }
    }

    fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/engines/text-davinci-003/completions"

        val requestBody = """
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
                Log.e("error", "API failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("data", body)
                } else {
                    Toast.makeText(requireActivity(), "empty", Toast.LENGTH_SHORT).show()
                    Log.v("data", "empty")
                }
                val jsonObject = JSONObject(body)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                val textResult = jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }
        })
    }

    private fun bindobservers() {
        viewModel?.chatgptresponseLiveData?.value = null
        viewModel?.chatgptresponseLiveData?.removeObservers(viewLifecycleOwner)
        viewModel?.chatgptresponseLiveData?.removeObservers(viewLifecycleOwner)
        viewModel?.chatgptresponseLiveData?.observe(viewLifecycleOwner, { response ->
            if (response == null) {
            } else {
                when (response) {
                    is Resource.Success -> {
                        addMessage(
                            "strDate",
                            response.data?.data?.get(0)?.text.toString(),
                            Messages.TYPE_MESSAGE_REMOTE
                        )

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                             .show()

                    }

                    is Resource.Loading -> {
                        // handle loading state

                    }
                }
            }

        })
    }


}