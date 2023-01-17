package com.taitos.testpjk.model

import java.util.*


data class BillDocument(
    var bills: MutableList<Bill>? = null)
data class Bill(

    var id: String? = "",
    var barcode: String? = "",
    var bill_no: String? = "",
    var bill_date: String? = "",
    var customer_id: String? = "",
    var balance: Double? = 0.0,
    var send_type: Int? = 0,
    var bill_type: String? = "",
    var Activity_desc: String? = "",
    var remark: String? = "",
    var payType: Int? = 0,
    var img: String? = "",
    var sign_img: String? = "",
    var complete_location: String? = "",
    var status: Int? = 1,
    var collect_money:String? = "",
    var del: Boolean? = false,
    var create_datetime: String? = "",
    var update_datetime: Date? = null,
    var customer: Customer? = null,
    var create_by: Employee? = null,
    var messenger: Employee? = null,

    )




