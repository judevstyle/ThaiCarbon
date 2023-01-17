package com.taitos.testpjk.view.bill

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.MainActivity
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentBillBinding
import com.taitos.testpjk.helper.ImageChoosBottomSheetAction
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Employee
import kotlinx.android.synthetic.main.fragment_bill_desc.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BillFragment : BaseFragmentTest<FragmentBillBinding>() {

    private val viewModel:BillViewModel by sharedViewModel()

    val db = Firebase.firestore

    private lateinit var mLayoutManager: LinearLayoutManager
    lateinit var adapters: BillAdapter

    val listTmp = ArrayList<Bill>()

    override val res: Int
        get() = R.layout.fragment_bill


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar("รายการวางบิล",R.id.toolbar)

        adapters = BillAdapter(requireContext())

    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)
        initView()
        loadData()


    }


    fun loadData() {

        val data = Gson().fromJson<Employee>(
            SharedPreferenceUtil.getUser(requireContext()).toString(),
            Employee::class.java
        )
        listTmp.clear()

        val parser = SimpleDateFormat("dd-MM-yyyy")
        val dateStr = parser.format(Timestamp.now().toDate())

        db.collection("bill")
            .whereEqualTo("messenger.id",data.id)
            .whereEqualTo("bill_type","1")
            .whereIn("status", arrayListOf(1,3))
            .whereEqualTo("del",false)
            .addSnapshotListener { snapshots, e  ->


                val list = ArrayList<Bill>()
                LogUtil.showLogError("sii","${snapshots?.size()}")
                for (document in snapshots?.documentChanges?:ArrayList()) {


                    when (document.type) {
                        DocumentChange.Type.ADDED -> {
                            val doc = document.document.toObject(Bill::class.java)
                            doc.id = document.document.id
                            LogUtil.showLogError("sii","${document.document.id}")

                            list.add(doc)
                        }
//                        DocumentChange.Type.MODIFIED -> {
//
//
//                        }
//                        DocumentChange.Type.REMOVED -> {
//
//                        }
                    }


                }
                listTmp.addAll(list)
                adapters.setItem(listTmp)

            }
//            .addOnFailureListener { exception ->
//                hideDialog()
////                        Log.e("eoir", "${document.id} => ${document.data}")
//                showToast("เกิดข้อผิดพลาด")
//                Log.e("eoir", "Error getting documents: ", exception)
//            }


    }

    fun initView() {
        mLayoutManager = LinearLayoutManager(requireContext())
        viewDataBinding!!.cvList.apply {
            layoutManager = mLayoutManager
            isNestedScrollingEnabled = false
            adapter = adapters
        }
        adapters.onClickImageListener = {

            viewModel.setPropertyID(it)
            findNavController()
                .navigate(R.id.action_billFragment_to_billDescFragment)
//            findNavController()
//                .navigate(R.id.action_billFragment_to_billDescTFragment)
        }


    }


}