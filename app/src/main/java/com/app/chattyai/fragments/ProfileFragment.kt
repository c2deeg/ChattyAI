package com.app.chattyai.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.chattyai.R
import com.app.chattyai.activities.HomeActivity
import com.app.chattyai.activities.LoginActivity
import com.app.chattyai.activities.SplashActivity
import com.app.chattyai.databinding.FragmentProfileBinding
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.UserModuleviewModel


class ProfileFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentProfileBinding? = null
    lateinit var viewModel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        viewModel= (activity as HomeActivity).viewModel
        var token = CSPreferences.readString(requireActivity(),Utils.TOKEN)
        var id = CSPreferences.readString(requireActivity(),Utils.USERID)
        viewModel.getuserById(token!!,id!!)
        bindobservers()
        listners()
        return binding!!.root
    }

    private fun listners() {
        binding?.linprofileinfo?.setOnClickListener(this)
        binding?.linprivacypolicy?.setOnClickListener(this)
        binding?.linaboutopenai?.setOnClickListener(this)
        binding?.tvlogout?.setOnClickListener(this)
        binding?.imgapplogo?.setOnClickListener(this)
        binding?.tvhistory?.setOnClickListener(this)
    }


    private fun bindobservers() {
        viewModel.getuserByIdData.value = null
        viewModel.getuserByIdData.removeObservers(viewLifecycleOwner)
        viewModel.getuserByIdData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding?.tvname?.text = response.data?.data?.fullname
                    binding?.tvgmail?.text = response.data?.data?.email
                    Utils.hideDialog()

                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
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


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.linprofileinfo -> findNavController().navigate(R.id.action_profileFragment2_to_profileinfoFragment)
            R.id.linprivacypolicy -> findNavController().navigate(R.id.action_profileFragment2_to_privacyPolicyFragment)
            R.id.linaboutopenai -> findNavController().navigate(R.id.action_profileFragment2_to_aboutopenchatAIFragment)
            R.id.tvlogout ->{
                CSPreferences.putString(requireActivity(),Utils.BOOLEANVALUE,"false")
              var intent = Intent(requireActivity(),LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.imgapplogo->{
                var intent = Intent(requireActivity(),HomeActivity::class.java)
                startActivity(intent)
            }
            R.id.tvhistory->findNavController().navigate(R.id.action_profileFragment2_to_historyFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.getuserByIdData.removeObservers(viewLifecycleOwner)
        binding = null
    }


}