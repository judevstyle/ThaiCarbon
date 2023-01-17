package com.taitos.testpjk.helper

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssoft.common.util.FileUtil
import com.ssoft.common.util.LogUtil
import com.taitos.testpjk.R
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.bottom_sheet_layout_photo.*
import kotlinx.coroutines.launch
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


@RuntimePermissions
class ImageChoosBottomSheetAction() : BottomSheetDialogFragment(){


    private var img_base64: String? = null
    private var providerFile: Uri? = null
    val PICK_IMAGE_REQUEST = 1002
    private var actualImage: File? = null
    private var compressedImage: File? = null
    //    private lateinit var adapters: ImageB64Adapter
    val REQUEST_CODE = 1001

    var onComplete: ((Bitmap?,Uri?) -> Unit)? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout_photo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomsheet()

    }



    fun bottomsheet() {



        menu_bottom_sheet_camera.setOnClickListener {
            openCameraWithPermissionCheck()

        }


        menu_bottom_sheet_img.setOnClickListener {
            openGalleryWithPermissionCheck()

        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun openCamera(){

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val filePhoto = getPhotoFile("photo.jpg")

        providerFile = FileProvider.getUriForFile(
            requireContext(),
            "com.ssoft.thaicarbons.provider",
            filePhoto
        )
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        if (takePhotoIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_CODE)
        } else {
            Toast.makeText(
                requireContext(),
                "Camera could not open",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @NeedsPermission(READ_EXTERNAL_STORAGE)
    fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

        LogUtil.showLogError("ldwo","oooo")
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.type = "image/*"
////        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
////        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST)

    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)

            if (resultCode === Activity.RESULT_OK) {

                if (requestCode == REQUEST_CODE) {



                    try {
                        val intent = data?.data


                        Log.e("ss", "${intent}")
                        actualImage =
                            FileUtil.from(requireContext(), providerFile!!)?.also { imageFile ->

                                lifecycleScope.launch {
                                    // Default compression

                                    compressedImage =
                                        Compressor.compress(requireContext(), imageFile)

                                    compressedImage?.let {
                                        img_base64 =
                                            convertImage(BitmapFactory.decodeFile(it.absolutePath))
//                                        Glide.with(requireContext()).load(providerFile).into(avatarIM)
                                        onComplete?.invoke(BitmapFactory.decodeFile(it.absolutePath),providerFile)
                                        LogUtil.showLogError("edd2","${img_base64}")
                                        dismiss()


                                    }
                                }


                            }


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (requestCode == PICK_IMAGE_REQUEST) {

                    try {
                        val intent = data?.data

                        actualImage =
                            FileUtil.from(requireContext(), data?.data!!)?.also { imageFile ->

                                lifecycleScope.launch {
                                    // Default compression

                                    compressedImage =
                                        Compressor.compress(requireContext(), imageFile)

                                    compressedImage?.let {
                                        img_base64 =
                                            convertImage(BitmapFactory.decodeFile(it.absolutePath))
                                        onComplete?.invoke(BitmapFactory.decodeFile(it.absolutePath),data?.data)

                                        dismiss()

//                                        LogUtil.showLogError("edd1","${img_base64}")
                                        //val img = ImageAdd("", img_base64, data?.data, 0)
//                                arrImg.add(img)
                                     //   Glide.with(requireContext()).load(data?.data).into(avatarIM)
//                                        adapters.setItem(img)
//                                avatarIM.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
//                                        showToast("sl;w3")

                                    }
                                }


                            }


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                else if (requestCode == UCrop.REQUEST_CROP) {

                    handleCropResult(data!!)

                }
            }
        } catch (ex: Exception) {

        }



    }

    private fun handleCropResult(result: Intent) {

        val resultUri = UCrop.getOutput(result)
//        avatarIM.setImageResource(0)
//        avatarIM.setImageURI(resultUri!!)

        try {
            actualImage = FileUtil.from(requireContext()!!, resultUri!!)?.also {
            }
        } catch (e: IOException) {
//                    showError("Failed to read picture data!")
            e.printStackTrace()
        }

        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(requireContext(), imageFile)

            }
        }


    }
    private val SAMPLE_CROPPED_IMAGE_NAME = "CropImage"
    private val REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02
    private val requestMode: Int = 1001

    private fun startCrop(uri: Uri) {
//        showToast("dlw")
        var destinationFileName: String =
            SAMPLE_CROPPED_IMAGE_NAME

        var uCrop = UCrop.of(
            uri,
            Uri.fromFile(File(activity?.cacheDir, destinationFileName))
        )
        uCrop = basisConfig(uCrop)
//        uCrop = advancedConfig(uCrop)
        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
//            setupFragment(uCrop)
        } else {                                                        // else start uCrop Activity
            uCrop.start(requireContext(),this)
        }
    }

    fun basisConfig(@NonNull uCrop: UCrop): UCrop {
        var data = uCrop
        data = uCrop.withAspectRatio(512.toFloat(), 512.toFloat())
        return data

    }


    fun convertImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val encodedImage: String = android.util.Base64.encodeToString(
            byteArrayOutputStream.toByteArray(),
            android.util.Base64.DEFAULT
        )
        //  Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
        return encodedImage


    }



}