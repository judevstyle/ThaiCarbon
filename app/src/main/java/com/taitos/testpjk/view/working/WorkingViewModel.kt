package com.taitos.testpjk.view.working

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ssoft.common.util.LogUtil
import com.taitos.nup.common.LiveEvent
import com.taitos.nup.common.MutableLiveEvent
import com.taitos.testpjk.model.ActivityAct
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Employee
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class WorkingViewModel : ViewModel() {

    val db = Firebase.firestore
     val _sum = MutableLiveData<Int>()

    val _bill1 = MutableLiveData<ArrayList<Bill>>()
    val _bill2 = MutableLiveData<ArrayList<Bill>>()
    val _activity = MutableLiveData<ArrayList<ActivityAct>>()

    val parser = SimpleDateFormat("dd-MM-yyyy")
    val dateStr = parser.format(Timestamp.now().toDate())

    private val _bill = MutableLiveData<Bill>()
    val onBill: LiveData<Bill> = _bill


    val _stateView = MutableLiveData<Boolean>(false)


    init {
    }

    fun setPropertyID(bill: Bill) {
        _bill.value = bill
    }


    fun getData(data: Employee) {


        var inCase = 0
        var outCase = 0
        var jumpCase = 0
        var satCase = 0
        var delieryCase = 0


        if (data.jobposition.equals("RIDER1")) {
            db.collection("bill")
                .whereEqualTo("messenger.id", data.id)
                .whereEqualTo("status", 2)
                .whereEqualTo("create_datetime", dateStr)
                .whereEqualTo("del", false)
                .get()
                .addOnSuccessListener { documents ->


//                    LogUtil.showLogError("deok", "dlep =${dateStr}")

                    val list1 = ArrayList<Bill>()
                    val list2 = ArrayList<Bill>()
                    val listTmp = ArrayList<Bill>()
                    val cusListTmp = ArrayList<String>()

                    var Sumsat = 0
                    for (document in documents) {
                        val doc = document.toObject(Bill::class.java)
                        doc.id = document.id
                        if (doc.bill_type.equals("1"))
                            list1.add(doc)
                        else
                            list2.add(doc)

//                    if (doc.con)
                        val countList: List<Bill> = listTmp.filter { s -> s.customer_id == doc.customer_id  }

                        if (countList.size == 0 ) {
                            listTmp.add(doc)
                            when (doc.send_type) {
                                1 -> inCase++
                                2 -> outCase++
                                3 -> jumpCase++
                                4 -> satCase++
                                5 -> delieryCase++
                            }
                        }


                    }

                    _bill1.value = list1
                    _bill2.value = list2


                    db.collection("activitys")
                        .whereEqualTo("status", 2)
                        .whereEqualTo("create_datetime", dateStr)
                        .whereEqualTo("messenger.id", data.id)
                        .whereEqualTo("del", false)
                        .get()
                        .addOnSuccessListener { documents ->
                            val listAct = ArrayList<ActivityAct>()

                            for (document in documents) {
                                val doc = document.toObject(ActivityAct::class.java)
                                doc.id = document.id

                                if (document.id.equals("W5sHOq9mgNjxOhSJMWie"))
                                    Sumsat = 50 //Sumsat += 50

                                listAct.add(doc)
                                when (doc.send_type) {
                                    1 -> inCase++
                                    2 -> outCase++
                                    3 -> jumpCase++
                                    4 -> {
                                        satCase++
                                    }
                                    5 -> delieryCase++
                                }
                            }

                            _activity.value = listAct

                            val sumCase = (inCase + outCase)
                            var sum = 0
                            if (sumCase >= 6)
                                sum = 60 + ((sumCase - 6) * 20)
                            LogUtil.showLogError(
                                "sumหห",
                                "${sum}-${sumCase}"
                            )
                            if (jumpCase >= 1) {
                                sum += 60 + ((jumpCase - 1) * 20)
                            }
                            if (satCase > 0)
                                sum += 100
//                            sum += (satCase * 50)
                        //    sum += (delieryCase * 50)
                            sum += (outCase * 20)




                            _sum.value = sum+Sumsat

                            LogUtil.showLogError(
                                "sum",
                                "${inCase}-${outCase}-${jumpCase}-${satCase}-${delieryCase} = ${sum}"
                            )

                        }


                }
                .addOnFailureListener { exception ->

                }
        }else{
            _sum.value = 0
        }

    }


}