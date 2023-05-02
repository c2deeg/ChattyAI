package com.app.chattyai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.chattyai.R
import com.app.chattyai.activities.LoginActivity
import com.app.chattyai.activities.SplashActivity
import com.app.chattyai.databinding.FragmentCreateAccountBinding
import com.app.chattyai.models.SignUp.SignupRequest
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.UserModuleviewModel

class CreateAccountFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentCreateAccountBinding? = null
    lateinit var viewModel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as LoginActivity).viewModel
        listeners()
        return binding?.root
    }

    private fun listeners() {
        binding?.tvsignup?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvsignup -> {
                viewModel.signUpMethod(signupReq())
                bindobservers()
            }
        }
    }

    private fun signupReq(): SignupRequest {
        var name = binding?.etname?.text.toString()
        var email = binding?.etmail?.text.toString()
        var password = binding?.etpassword?.text.toString()
        var number = Math.random()
        return SignupRequest("sss", email, name, true, false, password, number.toString())
    }

    private fun bindobservers() {
        viewModel.signupData.value = null
        viewModel.signupData.removeObservers(viewLifecycleOwner)
        viewModel.signupData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }


}