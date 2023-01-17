package com.taitos.testpjk.view.billDesc

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.taitos.testpjk.databinding.FragmentBillDescBinding
import com.taitos.testpjk.helper.ImageChoosBottomSheetAction
import com.taitos.testpjk.helper.ImageCropChoosBottomSheetAction
import com.taitos.testpjk.helper.ImageRatio
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
 * Use the [BillDescFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RuntimePermissions
class BillDescFragment : BaseFragmentTest<FragmentBillDescBinding>(),
    AdapterView.OnItemSelectedListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var LOCATION_REFRESH_TIME = 1 // 15 seconds to update

    var LOCATION_REFRESH_DISTANCE = 1 // 500 meters to update

    private var statusWorking: Int = 2
    private var payType: Int = 0
    private val viewModel: BillViewModel by sharedViewModel()
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    val db = Firebase.firestore
    var sendtype = 1
    var locationStr = ""

    var sendtypeData = arrayOf(
        "ส่งในเส้นทาง", "ส่งในเส้นทาง-ไกล",
        "งานโดด", "งานวันเสาร์"
    )

    var imgName: String? = null
    var signName: String? = null
    var locationManager: LocationManager? = null
    var billTmp: Bill? = null

    lateinit var bill: Bill
    override val res: Int
        get() = R.layout.fragment_bill_desc


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)
//        mLocationManager = (LocationManager)requireContext().getSystemService(LOCATION_SERVICE);
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupToolbar("รายละเอียด", R.id.toolbar)
// Create a reference to "mountains.jpg"
        // Create a reference to "mountains.jpg"

// Create a reference to 'images/mountains.jpg'

// Create a reference to 'images/mountains.jpg'
        openLocationWithPermissionCheck()

        initView()
        viewModel.onBill.observe(viewLifecycleOwner, Observer {

            this.bill = it
            viewDataBinding?.data = it
            getData(it.bill_no ?: "")


        })
        viewModel._stateView.observe(viewLifecycleOwner, Observer {

            if (it)
                boxHide.visibility = View.VISIBLE
            else
                boxHide.visibility = View.GONE


        })

        val dialog = ImageChoosBottomSheetAction()
        dialog.onComplete = { bitmap,uri  ->


            val date = Calendar.getInstance().time.time

            val mountainsRef = storageRef.child("${date}_img.jpg")


            imgIM.setImageBitmap(bitmap)
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data: ByteArray = baos.toByteArray()
            val uploadTask: UploadTask = mountainsRef.putBytes(data)


            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                LogUtil.showLogError("urlsss", "${it.message.toString()}")

            }.addOnSuccessListener {
                imgName = "${it.metadata?.path}"


            }


        }
        imgAction.setOnClickListener {

            dialog.show(childFragmentManager, "")


        }


    }


    fun getData(billNo: String) {

        db.collection("bill")
            .whereEqualTo("bill_no", billNo)
            .whereEqualTo("bill_type", "1")
            .whereEqualTo("del", false)
            .get()
            .addOnSuccessListener {

                for (document in it) {
                    billTmp = document.toObject(Bill::class.java)
                    billTmp?.id = document.id
                }

            }


    }


    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
    fun openLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private val mLocationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            showToast("${location.latitude},${location.longitude}")
            locationStr = "${location.latitude},${location.longitude}"
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

        rg.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                when (checkedId) {
                    R.id.id1 -> {
                        payType = 1
                    }
                    R.id.id2 -> {
                        payType = 2
                    }
                    R.id.id3 -> {
                        payType = 3
                    }

                }


            }


        })


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



            val date = Calendar.getInstance().time.time
            val mountainsSignRef = storageRef.child("${date}_sign.jpg")

            val baos1 = ByteArrayOutputStream()

            val signatureBitmap: Bitmap = signature_pad.getSignatureBitmap()
            signatureBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos1)
            val dataSign: ByteArray = baos1.toByteArray()
            val uploadTaskSign: UploadTask = mountainsSignRef.putBytes(dataSign)

            uploadTaskSign.addOnFailureListener {
                // Handle unsuccessful uploads
                LogUtil.showLogError("urlsss", "${it.message.toString()}")

            }.addOnSuccessListener {
                signName = "${it.metadata?.path}"

                LogUtil.showLogError("urlsss", "${it.metadata?.path}")


                bill.payType = payType
                bill.status = statusWorking
                bill.send_type = sendtype
                bill.img = imgName ?: ""
                bill.sign_img = signName ?: ""
                bill.complete_location = locationStr
                bill.update_datetime =  Timestamp.now().toDate()

                if (bill.bill_type.equals("1"))
                    bill.collect_money = "OPEN"
                else
                    bill.collect_money = "CLOSE"


                val data = Gson().fromJson<Employee>(
                    SharedPreferenceUtil.getUser(requireContext()).toString(),
                    Employee::class.java
                )

                LogUtil.showLogError("lc", "${bill.send_type}")


                if (locationStr.equals(""))
                    showToast("ตรวจไม่พบ Location")
                else{
                    db.collection("bill")
                        .document("${bill.id}")
                        .set(bill)
                        .addOnSuccessListener {

//                    showToast("บันทึกข้อมูลเรียบร้อย")
                            billTmp?.let {
                                LogUtil.showLogError("lc22", "${bill.send_type}")

                                it.collect_money = "CLOSE"
                                if (  bill.bill_type.equals("2")) {
                                    it.status = 2
                                    db.collection("bill")
                                        .document("${it.id}")
                                        .set(it)
                                        .addOnSuccessListener {
//                        findNavController()
//                            .popBackStack()

                                        }
                                }


                            }
                            showToast("บันทึกข้อมูลเรียบร้อย")

                            findNavController()
                                .popBackStack()


                        }
                }





            }


        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sendtype = position + 1
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(mLocationListener)
    }

}