package com.taitos.testpjk.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ssoft.common.BaseActivity
import com.taitos.testpjk.R
import com.taitos.testpjk.view.activityDesc.ActivityViewModel
import kotlinx.android.synthetic.main.activity_bill.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ActActivity : BaseActivity() {

    private val viewModel: ActivityViewModel by viewModel()

    override fun getContentView() = R.layout.activity_act


    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        super.onViewReady(savedInstanceState, intent)

        toolbar.title = "รายการงานอื่น ๆ วันนี้"
        setSupportActionBar(toolbar)
        showBackArrow()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (android.R.id.home == item.itemId)
            finish()

        return super.onOptionsItemSelected(item)
    }


}