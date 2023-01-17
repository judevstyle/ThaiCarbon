package com.taitos.testpjk.view.payBill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ssoft.common.BaseActivity
import com.taitos.testpjk.R
import com.taitos.testpjk.view.bill.BillViewModel
import kotlinx.android.synthetic.main.activity_bill.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PayBillActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_pay_bill
    private val viewModel: BillViewModel by viewModel()

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        super.onViewReady(savedInstanceState, intent)

        viewModel._stateView.value = true
        toolbar.title = "รายการเก็บเงิน"
        setSupportActionBar(toolbar)
        showBackArrow()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (android.R.id.home == item.itemId)
            finish()

        return super.onOptionsItemSelected(item)
    }
}