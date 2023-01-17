package com.taitos.testpjk.view.billDesc

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.UploadTask
import com.ssoft.common.BaseFragmentTest
import com.ssoft.common.util.LogUtil
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.FragmentBillDescTBinding
import com.taitos.testpjk.helper.ImageChoosBottomSheetAction
import kotlinx.android.synthetic.main.fragment_bill_desc.*
import java.io.ByteArrayOutputStream
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BillDescTFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BillDescTFragment : BaseFragmentTest<FragmentBillDescTBinding>() {
    override val res: Int
        get() = R.layout.fragment_bill_desc_t


    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        super.onViewReady(view, savedInstanceState)

        val dialog = ImageChoosBottomSheetAction()
        dialog.onComplete = { bitmap,uri  ->




        }
//        imgAction.setOnClickListener {

            dialog.show(childFragmentManager, "")

//        }

    }

}