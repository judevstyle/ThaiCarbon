package com.taitos.testpjk.view.working

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssoft.common.BaseActivity
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.ActivityWorkingBinding
import com.taitos.testpjk.model.Employee
import com.taitos.testpjk.view.bill.BillViewModel
import com.taitos.testpjk.view.working.actiity.ActivityWorkingFragment
import com.taitos.testpjk.view.working.bill.BillPayWorkingFragment
import com.taitos.testpjk.view.working.bill.BillWorkingFragment
import kotlinx.android.synthetic.main.activity_working.*
import org.koin.android.viewmodel.ext.android.viewModel

class WorkingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkingBinding

    private val viewModel: WorkingViewModel by viewModel()


    private lateinit var adapters: TabAdapter

    val bill1 = BillWorkingFragment()
    val bill2 = BillPayWorkingFragment()
    val act = ActivityWorkingFragment()

//    override fun getContentView() = R.layout.activity_working

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        toolbar.title = "การทำงานวันนี้"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

//        binding = viewModel


        val data = Gson().fromJson<Employee>(
            SharedPreferenceUtil.getUser(this).toString(),
            Employee::class.java
        )




        adapters = TabAdapter(supportFragmentManager)
        adapters.apply {
            addFragment(bill1, "วางบิล")
            addFragment(bill2, "เก็บเงิน")
            addFragment(act,  "งานอื่นๆ")

        }
        viewPagers.adapter = adapters
        viewPagers.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPagers)


        viewModel._sum.observe(this, Observer {

            binding.sum = it

        })

        LogUtil.showLogError("dow","lll")
        viewModel.getData(data)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (android.R.id.home == item.itemId)
            finish()

        return super.onOptionsItemSelected(item)
    }



}