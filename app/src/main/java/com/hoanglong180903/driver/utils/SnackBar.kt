package com.hoanglong180903.driver.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBar {
    fun showSnackBar(view : View, title : String){
        Snackbar.make(view, title, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
}