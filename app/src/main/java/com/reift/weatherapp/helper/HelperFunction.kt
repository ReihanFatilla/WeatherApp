package com.reift.weatherapp.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowManager

object HelperFunction {

    @SuppressLint("ObsoleteSdkInt")
    fun transparentStatusbar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = activity.window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

}