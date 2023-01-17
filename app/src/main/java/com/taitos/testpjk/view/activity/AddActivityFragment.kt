package com.taitos.testpjk.view.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentAddActivityBinding
import com.taitos.testpjk.model.Act
import com.taitos.testpjk.model.ActivityAct
import com.taitos.testpjk.model.Employee
import kotlinx.android.synthetic.main.fragment_add_activity.*
import java.text.SimpleDateFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddActivityFragment : BaseFragmentTest<FragmentAddActivityBinding>() {
    override val res: Int
        get() = R.layout.fragment_add_activity
    val db = Firebase.firestore

    var act: Act? = null
    val map: ArrayList<String> = ArrayList()


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)

        initView()

    }


    fun initView() {
        val list: ArrayList<String> = ArrayList()
        list.add("---เลือกประเภทงาน---")
        list.add("ไปรษณีย์")
        list.add("ไปธนาคาร")
        list.add("ส่งของ")
        list.add("อื่น ๆ")

        map.add("TEm1C9w6CwvCK3bRi9QG")
        map.add("wx13aWFhN2vFvqCzHiZR")
        map.add("W5sHOq9mgNjxOhSJMWie")
        map.add("Zcicm43dN8qOqoMzjvPd")


        val adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, list)
        sp.setAdapter(adapter)
        sp.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                val value: String = adapterView.getItemAtPosition(i).toString()
                if (i > 0)
                    act = Act(map.get(i - 1), value)
//                Toast.makeText(this@MainActivity, value, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        confirmAction.setOnClickListener {
            val data = Gson().fromJson<Employee>(
                SharedPreferenceUtil.getUser(requireContext()).toString(),
                Employee::class.java
            )
            val parser = SimpleDateFormat("dd-MM-yyyy")
            val dateStr = parser.format(Timestamp.now().toDate())

            if (act == null)
                showToast("เลือกงาน")
            else {
                val actData = ActivityAct(
                    remark = "${descET.text.toString()}",
                    create_by = data,
                    messenger = data,
                    update_by = data,
                    activity = act,
                    update_datetime = Timestamp.now().toDate(), create_datetime =  dateStr
                )

                db.collection("activitys")
                    .document()
                    .set(actData)

                findNavController()
                    .popBackStack()

            }


        }

    }

}