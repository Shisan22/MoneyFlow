package com.MoneyFlow.app.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

fun View.showSnackbar(message: String) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.setBackgroundTint(Color.parseColor("#323232"))
    snackbar.setTextColor(Color.WHITE)
    snackbar.show()
}

fun Double.formatCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    return format.format(this)
}
