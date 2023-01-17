package com.taitos.testpjk.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ssoft.common.BaseActivity
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.model.Employee
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {


    override fun getContentView() = R.layout.activity_profile

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        super.onViewReady(savedInstanceState, intent)


        toolbar.title = "ข้อมูลส่วนตัว"
        setSupportActionBar(toolbar)
        showBackArrow()



        val data = Gson().fromJson<Employee>(
            SharedPreferenceUtil.getUser(this).toString(),
            Employee::class.java
        )


        Glide.with(this).load("${data.avatar}").into(profile_image)
        code.text = "รหัสพนักงาน  : -"
        name.text = "ชื่อ-สกุล  : ${data.name}"
        job.text = "ตำแหน่ง  : ${data.jobposition}"




    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (android.R.id.home == item.itemId)
            finish()

        return super.onOptionsItemSelected(item)
    }

}