package com.venkatesh.schoolmanagement.utilities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan


object Commons {
    //Showing alert dialog for validation messages , confirmation messgaes
    fun showAlertDialog(
        context: Context,
        title: String? = null,
        message: String,
        nofOptions: Int = 1,
        positiveButtonText: String = "Ok",
        negativeButtonText: String = "No",
        neutralButtonText: String = "Cancel",
        isCancelable: Boolean = true,
        dialogCallback: DialogCallback? = null
    ) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(context)

        // Set the alert dialog title
        builder.setTitle(title)

        // Display a message on alert dialog
        builder.setMessage(message)

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(positiveButtonText) { dialog, which ->
            // Do something when user press the positive button
            dialogCallback?.positiveClick()
        }


        if (nofOptions == 2)
        // Display a negative button on alert dialog
            builder.setNegativeButton(negativeButtonText) { dialog, which ->
                dialogCallback?.negativeClick()
            }

        if (nofOptions == 3)
        // Display a neutral button on alert dialog
            builder.setNeutralButton(neutralButtonText) { _, _ ->
                dialogCallback?.neutralClick()
            }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        dialog.setCancelable(isCancelable)

        // Display the alert dialog on app interface
        dialog.show()

    }

    fun spanTwoTexts(data1: String, data2: String) {
        val word = SpannableString(data1)
        word.setSpan(ForegroundColorSpan(Color.GRAY), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val wordTwo = SpannableString(data2)
        wordTwo.setSpan(ForegroundColorSpan(Color.BLACK), 0, wordTwo.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}