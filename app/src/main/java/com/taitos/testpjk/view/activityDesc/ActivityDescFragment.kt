package com.taitos.testpjk.view.activityDesc

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentActivityDescBinding
import com.taitos.testpjk.databinding.FragmentBillDescBinding
import com.taitos.testpjk.helper.ImageChoosBottomSheetAction
import com.taitos.testpjk.model.ActivityAct
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Employee
import com.taitos.testpjk.view.bill.BillViewModel
import kotlinx.android.synthetic.main.fragment_bill_desc.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.ByteArrayOutputStream
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivityDescFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RuntimePermissions
class ActivityDescFragment : BaseFragmentTest<FragmentActivityDescBinding>(),
    AdapterView.OnItemSelectedListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var locationStr = ""

    private var nameImg: String? = null
    private var statusWorking: Int = 2
    private var payType: Int = 0
    private val viewModel: ActivityViewModel by sharedViewModel()
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    val db = Firebase.firestore
    var sendtype = 1
    var sendtypeData = arrayOf(
        "ส่งในเส้นทาง", "ส่งในเส้นทาง-ไกล",
        "งานโดด", "งานวันเสาร์", "ส่งของ"
    )


    lateinit var activityAct: ActivityAct//: Bill
    override val res: Int
        get() = R.layout.fragment_activity_desc


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)

        setupToolbar("รายละเอียด", R.id.toolbar)
// Create a reference to "mountains.jpg"
        // Create a reference to "mountains.jpg"
// Create a reference to 'images/mountains.jpg'

// Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/mountains.jpg")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initView()
        viewModel.onActivity.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            this.activityAct = it
            viewDataBinding?.data = it


        })
        openLocationWithPermissionCheck()


        val dialog = ImageChoosBottomSheetAction()
        LogUtil.showLogError("urlsss", "1123")

        dialog.onComplete = { bitmap, uri ->
            LogUtil.showLogError("urlsss", "${bitmap}")


            val date = Calendar.getInstance().time.time

            val mountainsRef = storageRef.child("${date}_img.jpg")
//            val mountainsSignRef = storageRef.child("${date}_sign.jpg")

            imgIM.setImageBitmap(bitmap)
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data: ByteArray = baos.toByteArray()
            val uploadTask: UploadTask = mountainsRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                LogUtil.showLogError("urlsss", "${it.message.toString()}")

            }.addOnSuccessListener {

                nameImg = "${it.metadata?.path}"
//                LogUtil.showLogError("urlsss","${it.metadata?.path}")


            }
//            val baos1 = ByteArrayOutputStream()
//
//            val signatureBitmap: Bitmap = mSignaturePad.getSignatureBitmap()
//            signatureBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos1)
//            val dataSign: ByteArray = baos1.toByteArray()
//            val uploadTaskSign: UploadTask = mountainsSignRef.putBytes(dataSign)

//            uploadTaskSign.addOnFailureListener {
//                // Handle unsuccessful uploads
//                LogUtil.showLogError("urlsss","${it.message.toString()}")
//
//            }.addOnSuccessListener {
//
//
//
//            }

//            uploadTask.addOnFailureListener {
//                // Handle unsuccessful uploads
//                LogUtil.showLogError("urlsss","${it.message.toString()}")
//
//            }.addOnSuccessListener {
//
//
//
//            }


        }
        imgAction.setOnClickListener {

            dialog.show(childFragmentManager, "")


        }


    }

    fun initView() {


        spinner.setOnItemSelectedListener(this)

        // Create the instance of ArrayAdapter
        // having the list of courses

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sendtypeData
        )

        // set simple layout resource file
        // for each item of spinner

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner.setAdapter(ad)


        workingStatus.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                when (checkedId) {
                    R.id.s1 -> {
                        statusWorking = 2
                        spinner.visibility = View.VISIBLE

                    }
                    R.id.f2 -> {
                        statusWorking = 3
                        spinner.visibility = View.GONE

                    }

                }


            }


        })



        confirmAction.setOnClickListener {

            activityAct.status = statusWorking
            activityAct.send_type = sendtype


            val data = Gson().fromJson<Employee>(
                SharedPreferenceUtil.getUser(requireContext()).toString(),
                Employee::class.java
            )


            if (nameImg == null)
                showToast("เลือกรูปหลักฐานการทำงาน")
            else if (locationStr.equals(""))
                showToast("ตรวจไม่พบ Location")
            else {
                activityAct.img = nameImg
                activityAct.update_datetime =  Timestamp.now().toDate()

                db.collection("activitys")
                    .document(activityAct.id!!)
                    .set(activityAct)
                showToast("บันทึกข้อมูลเรียบร้อย")

                findNavController()
                    .popBackStack()
            }


        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sendtype = position + 1

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    @SuppressLint("MissingPermission")
    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    fun openLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    LogUtil.showLogError("location", "${location.latitude},${location.longitude}")
                    locationStr = "${location.latitude},${location.longitude}"

                }

            }
//        locationManager?.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),
//            LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener
//        );


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


}