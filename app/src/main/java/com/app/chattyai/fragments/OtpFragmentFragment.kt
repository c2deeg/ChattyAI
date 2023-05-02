package com.app.chattyai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.chattyai.R
import com.app.chattyai.databinding.FragmentOtpFragmentBinding


class OtpFragmentFragment : Fragment(), View.OnClickListener {
private var binding:FragmentOtpFragmentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpFragmentBinding.inflate(layoutInflater,container,false)
        listenres()
        return binding!!.root
    }

    private fun listenres(){
        binding?.tvcountinue?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvcountinue->findNavController().navigate(R.id.action_otpFragmentFragment_to_homeActivity)
        }
    }


}