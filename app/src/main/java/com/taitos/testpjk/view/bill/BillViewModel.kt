package com.taitos.testpjk.view.bill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoft.common.util.LogUtil
import com.taitos.nup.common.LiveEvent
import com.taitos.nup.common.MutableLiveEvent
import com.taitos.testpjk.model.Bill
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class BillViewModel : ViewModel() {


    private val _bill = MutableLiveData<Bill>()
    val onBill: LiveData<Bill> = _bill


     val _stateView = MutableLiveData<Boolean>(false)


    init {
    }

    fun setPropertyID(bill: Bill){
        _bill.value = bill
    }


}