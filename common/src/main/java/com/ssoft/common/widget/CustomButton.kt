package com.ssoft.common.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.ssoft.common.R


class CustomButton : AppCompatButton {

    private var typefaceType = 0
    private var mFontFactory: TypeFactory? = null


    constructor(
        context: Context,
        attrs: AttributeSet
    ):super(context, attrs) {

        applyCustomFont(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int
    ):super(context, attrs, defStyle) {

        applyCustomFont(context, attrs)
    }

    constructor(context: Context):super(context) {

    }

    private fun applyCustomFont(
        context: Context,
        attrs: AttributeSet
    ) {
        val array = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            0, 0
        )
        typefaceType = try {
            array.getInteger(R.styleable.CustomTextView_font_name, 0)
        } finally {
            array.recycle()
        }
        if (!isInEditMode) {
            setTypeface(getTypeFace(typefaceType))
        }
    }

    fun getTypeFace(type: Int): Typeface? {
        if (mFontFactory == null) mFontFactory = TypeFactory(context!!)
        return when (type) {
            Constants.A_LIGHT -> mFontFactory!!.light
            Constants.A_MEDIUM -> mFontFactory!!.medium
            Constants.A_Thin -> mFontFactory!!.thin

            else -> mFontFactory!!.medium
        }
    }




    interface Constants {
        companion object {
            const val A_LIGHT = 1
            const val A_Thin = 2
            const val A_MEDIUM = 3

        }
    }
}