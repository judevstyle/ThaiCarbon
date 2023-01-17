package com.taitos.testpjk.view.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentActBinding
import com.taitos.testpjk.model.ActivityAct
import com.taitos.testpjk.model.Employee
import com.taitos.testpjk.view.activityDesc.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_act.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActFragment :  BaseFragmentTest<FragmentActBinding>() {

    private val viewModel: ActivityViewModel by sharedViewModel()

    val db = Firebase.firestore

    private lateinit var mLayoutManager: LinearLayoutManager
    lateinit var adapters: ActivityAdapter


    override val res: Int
    get() = R.layout.fragment_act


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar("รายการงานอื่น ๆ วันนี้",R.id.toolbar)

        adapters = ActivityAdapter(requireContext())

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

        LogUtil.showLogError("id","${data.id}")


        val parser = SimpleDateFormat("dd-MM-yyyy")
        val dateStr = parser.format(Timestamp.now().toDate())

        db.collection("activitys")
            .whereEqualTo("status", 1)
            .whereIn("status", arrayListOf(1,3))
            .whereEqualTo("del", false)
            .whereEqualTo("messenger.id", data.id)
//            .whereEqualTo("create_datetime", dateStr)
            .addSnapshotListener { snapshots, e  ->


                val list = ArrayList<ActivityAct>()
                LogUtil.showLogError("id","${snapshots?.size()}")

                for (document in snapshots!!.documentChanges) {

//                    when (document.type) {
//                        DocumentChange.Type.ADDED -> {
                    val doc = document.document.toObject(ActivityAct::class.java)
                    doc.id = document.document.id
                    list.add(doc)
//                        }
//                        DocumentChange.Type.MODIFIED -> {
//
//
//                        }
//                        DocumentChange.Type.REMOVED -> {
//
//                        }
//                    }


                }
                adapters.setItem(list)

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

            viewModel.setActivity(it)
            findNavController()
                .navigate(R.id.action_actFragment_to_activityDescFragment)

        }

        addAction.setOnClickListener {
            findNavController()
                .navigate(R.id.action_actFragment_to_addActivityFragment)
        }


    }


}