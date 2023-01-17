package com.ssoft.common.widget

import android.content.Context
import android.graphics.Typeface

class TypeFactory {
    private val A_THIN = "fonts/Sarabun-Thin.ttf"
    private val A_LIGHT = "fonts/Sarabun-Light.ttf"
    private val A_MEDIUM = "fonts/Sarabun-Medium.ttf"


    var light: Typeface? = null
    var medium: Typeface? = null
    var thin: Typeface? = null

    constructor(context: Context) {
        light = Typeface.createFromAsset(context.assets, A_LIGHT)
        medium = Typeface.createFromAsset(context.assets, A_MEDIUM)
        thin = Typeface.createFromAsset(context.assets, A_THIN)

    }

}