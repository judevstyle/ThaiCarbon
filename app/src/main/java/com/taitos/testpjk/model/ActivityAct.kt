package com.taitos.testpjk.model

import com.google.firebase.Timestamp
import java.util.*

data class ActivityAct(
    var id: String? = "",
    var remark: String? = "",
    var status: Int? = 1,
    var send_type: Int? = 0,
    var del: Boolean? = false,
    var img: String? = "",
    var sign_img: String? = "",
    var complete_location: String? = "",
    var activity: Act? = null,
    var create_by: Employee? = null,
    var messenger: Employee? = null,
    var update_by: Employee? = null,
    var update_datetime: Date? = null,
    var create_datetime: String? = "",


    )

data class Act(
    var id: String? = "",
    var title: String? = "",
)