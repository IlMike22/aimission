package com.example.michl.aimission.Utility

import android.app.AlertDialog
import android.content.Context
import com.example.michl.aimission.R

fun showSimpleDialog(
        context: Context,
        title: String,
        msg: String,
        onButtonClicked: (Boolean) -> Unit
): Boolean {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setPositiveButton(context.getString(R.string.dialog_helper_positive_button_text))
    { _, _ ->

        onButtonClicked(true)
    }
    builder.setNegativeButton(context.getString(R.string.dialog_helper_negative_button_text))
    { _, _ ->

        onButtonClicked(false)
    }
    builder.show()


    return false
}