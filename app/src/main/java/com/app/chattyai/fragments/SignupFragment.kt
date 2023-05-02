package com.app.chattyai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.chattyai.R
import com.app.chattyai.databinding.FragmentSignupBinding
import com.app.chattyai.viewmodel.UserModuleviewModel

class SignupFragment : Fragment(), View.OnClickListener {
    private var binding:FragmentSignupBinding?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(layoutInflater,container,false)
        listeners()


        return binding!!.root

    }

    private fun listeners(){
        binding?.lincreateaccount?.setOnClickListener(this)
        binding?.tvlogin?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.lincreateaccount->findNavController().navigate(R.id.action_signupFragment_to_createAccountFragment)
            R.id.tvlogin->findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

        }
    }


}