package com.taitos.testpjk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.ssoft.common.util.SharedPreferenceUtil
import java.util.*
import kotlin.concurrent.schedule

class Spash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)


        Timer("SettingUp", false).schedule(2000) {

            if (SharedPreferenceUtil.getUser(this@Spash).equals("")){
                startActivity(Intent(this@Spash,Login::class.java))
                finish()
            }else{
                startActivity(Intent(this@Spash,MainActivity::class.java))
                finish()
            }


        }


    }
}