package com.app.chattyai.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.app.chattyai.R
import com.app.chattyai.activities.HomeActivity
import com.app.chattyai.adapters.PackageViewPagerAdapter
import com.app.chattyai.databinding.FragmentLoginBinding
import com.app.chattyai.interfaces.PageAdapterListner
import com.app.chattyai.models.Pagerplanmodel
import com.app.chattyai.repository.UserModuleResponseRepository
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.USerViewModelFactory
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.UserModuleviewModel
import me.relex.circleindicator.CircleIndicator
import kotlin.collections.ArrayList


class LoginFragment : Fragment(), View.OnClickListener, PageAdapterListner {
    private var binding: FragmentLoginBinding? = null
    private var viewpager: ViewPager? = null
    lateinit var dialog: Dialog
    lateinit var viewModel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        listeners()
//        var array = arrayOf(1,2,6)
//        var n = array.size+1
//        var totalsum = n*(n+1)/2
//        val actualsum = array.sum()
//        val missing = totalsum-actualsum
//        Toast.makeText(requireActivity(), missing.toString(), Toast.LENGTH_SHORT).show()
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)
        return binding!!.root
    }

    private fun listeners() {
        binding?.tvlogin?.setOnClickListener(this)
        binding?.tvguest?.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvlogin -> {
                viewModel.loginMethod(
                    binding?.etmail!!.text.toString(),
                    binding?.etpassword!!.text.toString()
                )
//                showDialog()
                biindobservers()
            }

            R.id.tvguest -> {
                var intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun biindobservers() {
        viewModel.loginData.value = null
        viewModel.loginData.removeObservers(viewLifecycleOwner)
        viewModel.loginData.observe(viewLifecycleOwner, Observer { response ->
            Utils.hideDialog()

            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    CSPreferences.putString(requireActivity(),Utils.BOOLEANVALUE,"true")
                    CSPreferences.putString(requireActivity(),Utils.TOKEN,response.data?.data?.token.toString())
                    CSPreferences.putString(requireActivity(),Utils.USERID,response.data?.data?._id.toString())
                    showDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT).show()
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


    private fun showDialog() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialogpackageplanitem)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        viewpager = dialog.findViewById(R.id.viewpager)
        val indicator = dialog.findViewById<CircleIndicator>(R.id.indicator)
        val imgclose = dialog.findViewById<ImageView>(R.id.imgclose)
        val tryfree = dialog.findViewById<LinearLayout>(R.id.tryfree)
        tryfree.setOnClickListener {


            var intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
        }

        initViewPager(indicator)

        imgclose.setOnClickListener {
            dialog?.dismiss()
        }
        dialog.show()
    }

    private fun initViewPager(indicator: CircleIndicator) {
        val arrayList: ArrayList<Pagerplanmodel> = ArrayList()
        arrayList.add(
            Pagerplanmodel(
                "Basic plan",
                "Token Limit",
                "100k",
                "With each token being roughly equivalent to 4 letters,our system allows you to genrate an estimated 75000 words by utilizing 100k tokens",
                "Image Generations",
                "100",
                "Indulge in AI generated images with our comprehensive2 months subscription plan , offering the ability to generate up to 100 high quality images",
                "Transcription limit",
                "10Hrs",
                "Experience the convenience 0f 10 hours of accurate speech-to-text transcription service, available for a duration of six months",
                "$20",
                "Two Months subscription"
            )
        )

        arrayList.add(
            Pagerplanmodel(
                "Premium plan",
                "Token Limit",
                "500k",
                "With each token being roughly equivalent to 4 letters,our system allows you to genrate an estimated 350,000 words by utilizing 500k tokens",
                "Image Generations",
                "200",
                "Indulge in AI generated images with our comprehensive 6 months subscription plan , offering the ability to generate up to 200 high quality images",
                "Transcription limit",
                "20Hrs",
                "Experience the convenience 0f 20 hours of accurate speech-to-text transcription service, available for a duration of six months",
                "$50",
                "Two Months subscription"
            )
        )
        arrayList.add(
            Pagerplanmodel(
                "Ultra plan",
                "Token Limit",
                "1 Millon",
                "With each token being roughly equivalent to 4 letters,our system allows you to genrate an estimated 350,000 words by utilizing 500k tokens",
                "Image Generations",
                "500",
                "Indulge in AI generated images with our comprehensive 6 months subscription plan , offering the ability to generate up to 200 high quality images",
                "Transcription limit",
                "50Hrs",
                "Experience the convenience 0f 20 hours of accurate speech-to-text transcription service, available for a duration of six months",
                "$100",
                "1 year subscription"
            )
        )


        val onBoardSplashPagerAdapter = PackageViewPagerAdapter(requireActivity(), arrayList, this)
        viewpager?.adapter = onBoardSplashPagerAdapter
        indicator.setViewPager(viewpager)


    }

    override fun passclick() {
        dialog?.dismiss()
        findNavController().navigate(R.id.action_loginFragment_to_selectPaymentFragment)
    }


}