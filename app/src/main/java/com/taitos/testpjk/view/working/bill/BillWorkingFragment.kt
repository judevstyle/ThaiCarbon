package com.taitos.testpjk.view.working.bill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentBillBinding
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Employee
import com.taitos.testpjk.view.bill.BillAdapter
import com.taitos.testpjk.view.bill.BillViewModel
import com.taitos.testpjk.view.working.WorkingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BillWorkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BillWorkingFragment : BaseFragmentTest<FragmentBillBinding>() {

    private val viewModel: WorkingViewModel by sharedViewModel()

    val db = Firebase.firestore

    private lateinit var mLayoutManager: LinearLayoutManager
    lateinit var adapters: BillAdapter


    override val res: Int
        get() = R.layout.fragment_bill


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapters = BillAdapter(requireContext())

    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)
        initView()
        loadData()

    }


    fun loadData() {

        viewModel._bill1.observe(viewLifecycleOwner, Observer {

            adapters.setItem(it)

        })



    }

    fun initView() {
        mLayoutManager = LinearLayoutManager(requireContext())
        viewDataBinding!!.cvList.apply {
            layoutManager = mLayoutManager
            isNestedScrollingEnabled = false
            adapter = adapters
        }


    }


}