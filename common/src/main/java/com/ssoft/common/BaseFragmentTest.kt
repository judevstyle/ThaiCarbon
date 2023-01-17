package com.ssoft.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ssoft.common.dialog.ProgressDialog

abstract class BaseFragmentTest<T : ViewDataBinding> : Fragment() {

    var mProgressDialog: ProgressDialog? = null
     var viewDataBinding: T? = null

    abstract val res: Int
    var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (viewDataBinding == null)
            viewDataBinding = DataBindingUtil.inflate<T>(inflater, res, container, false)
        return viewDataBinding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(view, savedInstanceState)
        onSetingToolbar()
//        initView()

    }

//    protected open fun initView() {
//    }

    @CallSuper
    protected open fun onViewReady(
        view: View,
        savedInstanceState: Bundle?
    ) { //To be used by child activities
    }


    fun setupToolbar(title: String, view: Int) {

        toolbar =
            activity?.findViewById<Toolbar>(view)
        toolbar?.title = title
        toolbar?.setNavigationOnClickListener(View.OnClickListener { activity?.onBackPressed() })

    }


    protected open fun onSetingToolbar() {

    }

    open fun noInternetConnectionAvailable() {
//        showToast(getString(R.string.noNetworkFound))
    }


    open fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(context)
        }
        if (!mProgressDialog!!.isShowing()) { //            mProgressDialog.setMessage(message);
            mProgressDialog!!.show()
        }
    }


    open fun showProgressDialogTitle(title: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(context)
        }
//        mProgressDialog?.setTitle(title)
        if (!mProgressDialog!!.isShowing()) { //            mProgressDialog.setMessage(message);
            mProgressDialog!!.show()
        }
    }


    open fun hideDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    protected open fun showAlertDialog(msg: String?) {
//        val dialogBuilder =
//            AlertDialog.Builder(context!)
//        dialogBuilder.setTitle(null)
//        dialogBuilder.setIcon(R.mipmap.ic_launcher)
//        dialogBuilder.setMessage(msg)
//        dialogBuilder.setPositiveButton(""
////            getString(R.string.dialog_ok_btn)
//        ) { dialog, which -> dialog.cancel() }
//        dialogBuilder.setCancelable(false)
//        dialogBuilder.show()
    }


    protected open fun showToast(mToastMsg: String?) {
        Toast.makeText(context, mToastMsg, Toast.LENGTH_LONG).show()
    }

}