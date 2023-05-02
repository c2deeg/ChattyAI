package com.app.chattyai.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.chattyai.R
import com.app.chattyai.activities.HomeActivity
import com.app.chattyai.databinding.FragmentProfileBinding
import com.app.chattyai.databinding.FragmentProfileinfoBinding
import com.app.chattyai.utils.CSPreferences
import com.app.chattyai.utils.Resource
import com.app.chattyai.utils.Utils
import com.app.chattyai.viewmodel.UserModuleviewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException


class ProfileinfoFragment : Fragment() {

    private var binding: FragmentProfileinfoBinding? = null
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var bitmap: Bitmap
    lateinit var viewModel: UserModuleviewModel
    lateinit var token: String
    lateinit var id: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileinfoBinding.inflate(layoutInflater, container, false)
        token = CSPreferences.readString(requireActivity(), Utils.TOKEN)!!
        id = CSPreferences.readString(requireActivity(), Utils.USERID)!!
        viewModel = (activity as HomeActivity).viewModel

        viewModel.getuserById(token!!, id!!)
        bindobservers()



        binding?.imgselectimage?.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding?.imgapplogo?.setOnClickListener {
            var intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
        }

        return binding!!.root

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
//            img_profile?.setImageURI(imageUri)
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
//                editProfilePresenter?.uploadProfileImage(bitmap!!)
                val multipart = bitmapToMultipart(bitmap, "image")
                viewModel.uploaduserimage(token, id, multipart)
                bindobserversUploadimage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding!!.imguserimage?.setImageURI(imageUri)
        }
    }

    private fun bindobservers() {
        viewModel.getuserByIdData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding?.etname?.setText(response.data?.data?.fullname.toString())
                    binding?.etemail?.setText(response.data?.data?.email.toString())
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

    private fun bindobserversUploadimage() {
        viewModel.userUploadimageLiveData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
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

    fun bitmapToMultipart(bitmap: Bitmap, name: String): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val requestBody = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            byteArrayOutputStream.toByteArray()
        )
        return MultipartBody.Part.createFormData(name, "image", requestBody)
    }




}