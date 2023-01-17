package com.taitos.testpjk.view.payBill

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentPayBillBinding
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Employee
import com.taitos.testpjk.view.bill.BillAdapter
import com.taitos.testpjk.view.bill.BillViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PayBillFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PayBillFragment :  BaseFragmentTest<FragmentPayBillBinding>() {
    private val viewModel:BillViewModel by sharedViewModel()

    override val res: Int
    get() = R.layout.fragment_pay_bill

    val listTmp = ArrayList<Bill>()


    val db = Firebase.firestore

    private lateinit var mLayoutManager: LinearLayoutManager
    lateinit var adapters: BillAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar("รายการเก็บเงิน",R.id.toolbar)
        adapters = BillAdapter(requireContext())

    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)
        initView()
        loadData()

    }


    fun loadData() {
        listTmp.clear()
        val data = Gson().fromJson<Employee>(
            SharedPreferenceUtil.getUser(requireContext()).toString(),
            Employee::class.java
        )
        val parser = SimpleDateFormat("dd-MM-yyyy")
        val dateStr = parser.format(Timestamp.now().toDate())

        db.collection("bill")
            .whereEqualTo("messenger.id",data.id)
            .whereEqualTo("bill_type","2")
            .whereIn("status", arrayListOf(1,3))
            .whereEqualTo("del",false)
            .addSnapshotListener { snapshots, e  ->


                val list = ArrayList<Bill>()
                for (document in snapshots!!.documentChanges) {

                    when (document.type) {
                        DocumentChange.Type.ADDED -> {
                            val doc = document.document.toObject(Bill::class.java)
                            doc.id = document.document.id
                            LogUtil.showLogError("ssii","${document.document.id}")

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
                .navigate(R.id.action_payBillFragment_to_billDescFragment2)
//            findNavController()
//                .navigate(R.id.action_payBillFragment_to_billDescTFragment2)
        }

    }

}