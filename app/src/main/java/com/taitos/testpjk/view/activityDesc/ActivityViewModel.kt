package com.taitos.testpjk.view.activityDesc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssoft.common.util.LogUtil
import com.taitos.nup.common.LiveEvent
import com.taitos.nup.common.MutableLiveEvent
import com.taitos.testpjk.model.ActivityAct
import com.taitos.testpjk.model.Bill
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {


    private val _activity = MutableLiveData<ActivityAct>()
    val onActivity: LiveData<ActivityAct> = _activity


     val _stateView = MutableLiveData<Boolean>(false)

    init {

    }

    fun setActivity(activityAct: ActivityAct){
        _activity.value = activityAct
    }


}