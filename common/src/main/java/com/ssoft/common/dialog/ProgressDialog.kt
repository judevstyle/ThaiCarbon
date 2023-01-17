package com.ssoft.common.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.animation.Animation
import android.widget.ImageView
import com.ssoft.common.R


class ProgressDialog(context: Context?) {

    private val animation: Animation? = null
    var alertDialog: Dialog? = null
    var img: ImageView? = null
    var context: Context? = null

    init {

        this.context = context
        alertDialog = Dialog(context!!)
        alertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog!!.setContentView(R.layout.dialog_loading_process)
        alertDialog!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent);

    }


    fun isShowing(): Boolean {
        return alertDialog!!.isShowing
    }

    fun show() {
        alertDialog!!.show()
    }

    fun dismiss() {
        alertDialog!!.dismiss()
    }


    fun onAnimationStart(animation: Animation?) {}

    fun onAnimationEnd(animation: Animation?) {}

    fun onAnimationRepeat(animation: Animation?) {}
}