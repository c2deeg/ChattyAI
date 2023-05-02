package com.app.chattyai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.chattyai.R
import com.app.chattyai.databinding.FragmentSelectPaymentBinding

class SelectPaymentFragment : Fragment() {
    private var binding: FragmentSelectPaymentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectPaymentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

}