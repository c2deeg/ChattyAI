package com.app.chattyai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.chattyai.adapters.ChatHistoryRecyclerAdapter
import com.app.chattyai.databinding.FragmentHistoryBinding
import com.app.chattyai.models.chatHistory.Data
import com.app.chattyai.repository.ChatGptRepository
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.ChatGptviewmodelFactory
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.ChatGptresponseViewModel


class HistoryFragment : Fragment() {
    private var binding: FragmentHistoryBinding? = null
    var viewModel: ChatGptresponseViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        initviewModel()

        return binding!!.root
    }

    private fun initviewModel() {
        val repository = ChatGptRepository()
        val viewModelProviderFactory =
            ChatGptviewmodelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(
                this,
                viewModelProviderFactory
            ).get(ChatGptresponseViewModel::class.java)
        var userid = CSPreferences.readString(requireActivity(), Utils.USERID)!!
        viewModel?.chatHistoryfunc(userid)
        bindobservers()
    }

    private fun initrecyclerview(data: List<Data>?) {
        var chatHistoryRecyclerAdapter = ChatHistoryRecyclerAdapter(requireActivity(),data)
        binding!!.chathistoryrecyclerview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = chatHistoryRecyclerAdapter
        }
    }

    private fun bindobservers() {
        viewModel?.chatHistoryLiveData?.value = null
        viewModel?.chatHistoryLiveData?.removeObservers(viewLifecycleOwner)
        viewModel?.chatHistoryLiveData?.removeObservers(viewLifecycleOwner)
        viewModel?.chatHistoryLiveData?.observe(viewLifecycleOwner, Observer{ response ->
            if (response == null) {
            } else {
                when (response) {
                    is Resource.Success -> {
                        Utils.hideDialog()
                        Toast.makeText(requireActivity(), response?.data?.statusCode.toString(), Toast.LENGTH_SHORT).show()
                        initrecyclerview(response?.data?.data)
                    }
                    is Resource.Error -> {
                        Utils.hideDialog()
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Loading -> {
                    Utils.showDialogMethod(requireActivity(),activity?.fragmentManager)
                    }
                }
            }

        })
    }


}