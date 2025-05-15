package com.hoanglong180903.driver.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object PopupUtils {
    fun showSnackBar(view : View, title : String){
        Snackbar.make(view, title, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    fun showToast(context : Context, title : String){
        Toast.makeText(context, title , Toast.LENGTH_LONG).show()
    }
}