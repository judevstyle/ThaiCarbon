package com.taitos.testpjk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.view.activity.ActActivity
import com.taitos.testpjk.view.bill.BillActivity
import com.taitos.testpjk.view.payBill.PayBillActivity
import com.taitos.testpjk.view.profile.ProfileActivity
import com.taitos.testpjk.view.working.WorkingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        menu1.setOnClickListener {

            startActivity(Intent(this, BillActivity::class.java))


        }
        menu2.setOnClickListener {

            startActivity(Intent(this, PayBillActivity::class.java))


        }

        menu3.setOnClickListener {

            startActivity(Intent(this, ActActivity::class.java))


        }
        menu4.setOnClickListener {

            startActivity(Intent(this, WorkingActivity::class.java))


        }
        menu5.setOnClickListener {

            startActivity(Intent(this, ProfileActivity::class.java))


        }

        logoutAction.setOnClickListener {
            SharedPreferenceUtil.updateUser(this,"")
            startActivity(Intent(this, Login::class.java))
            finish()

        }
    }




}