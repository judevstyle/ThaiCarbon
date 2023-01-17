package com.taitos.testpjk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ssoft.common.BaseActivity
import com.ssoft.common.util.LogUtil
import com.ssoft.common.util.SharedPreferenceUtil
import com.taitos.testpjk.model.Bill
import com.taitos.testpjk.model.Customer
import com.taitos.testpjk.model.Employee
import kotlinx.android.synthetic.main.activity_login.*


class Login : BaseActivity() {


    val db = Firebase.firestore


    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        super.onViewReady(savedInstanceState, intent)


        loginAction.setOnClickListener {
            LogUtil.showLogError("kdk", "ooo1")

//            Toast.makeText(this,"ระบุ Password",Toast.LENGTH_SHORT).show()

            if (usernameET.text?.length ?: 0 < 1) {
                Toast.makeText(this, "ระบุ Username", Toast.LENGTH_SHORT).show()

            } else if (passET.text?.length ?: 0 < 1) {
                Toast.makeText(this, "ระบุ Password", Toast.LENGTH_SHORT).show()

            } else {
//                Toast.makeText(this,"ระบุss Password",Toast.LENGTH_SHORT).show()

                showProgressDialog()
                LogUtil.showLogError("kdk", "ooo")
                db.collection("user_account")
                    .whereEqualTo("username", "${usernameET.text}")
                    .whereEqualTo("password", "${passET.text}")
                    .get()
                    .addOnSuccessListener { documents ->

                        LogUtil.showLogError("kdk", "ooo re ${documents.size()}")

                        hideDialog()
                        if (documents.size() > 0) {


                            for (document in documents) {
                                val doc = document.toObject(Employee::class.java)
                                doc.id = document.id

                                LogUtil.showLogError("ID","${doc.id}")



                                SharedPreferenceUtil.updateUser(this, Gson().toJson(doc))
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                            }

                        } else {
                            Toast.makeText(this, "ตรวจสอบ User และ Password", Toast.LENGTH_SHORT)
                                .show()
                        }

//                        for (document in documents) {
//                            Toast.makeText(this,"ระบุ ${document.data.toString()}",Toast.LENGTH_SHORT).show()
//
//                            Log.e("eoir", "${document.id} => ${document.data}")
//                        }
                    }
                    .addOnFailureListener { exception ->
                        hideDialog()
//                        Log.e("eoir", "${document.id} => ${document.data}")
                        showToast("เกิดข้อผิดพลาด")
                        Log.e("eoir", "Error getting documents: ", exception)
                    }

            }


        }



//        login1Action.setOnClickListener {
//
//
//            val data = Gson().fromJson<Employee>(SharedPreferenceUtil.getUser(this).toString(),Employee::class.java)
//
//
//
//            val bill = Bill(
//                id = "aaa",
//                barcode = "827368/11-10-2022/CS-029-2/900.00",
//                bill_no = "827368",
//                bill_date = "11-10-2022",
//                customer_id = "CS-029-2",
//                balance = 900.00,
//                customer = Customer("CS-029-2","AAAAA","BBBBBBBBB"),
//                create_by = data,
//                messenger = data,
//
//                )
//
//
//            db.collection("bills")
//                .document("${data.id}")
//                .collection("bill")
//                .document()
//                .set(bill)
//
//
//        }

//    override fun getContentView() =


    }

    override fun getContentView() = R.layout.activity_login
}