package com.taitos.testpjk.view.bill

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.ssoft.common.BaseActivity
import com.taitos.testpjk.R
import kotlinx.android.synthetic.main.activity_bill.*
import org.koin.android.viewmodel.ext.android.viewModel

class BillActivity : BaseActivity() {
    private val viewModel: BillViewModel by viewModel()

    override fun getContentView() = R.layout.activity_bill

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        super.onViewReady(savedInstanceState, intent)

        toolbar.title = "รายการวางบิล"
        setSupportActionBar(toolbar)
        showBackArrow()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (android.R.id.home == item.itemId)
            finish()

        return super.onOptionsItemSelected(item)
    }


}