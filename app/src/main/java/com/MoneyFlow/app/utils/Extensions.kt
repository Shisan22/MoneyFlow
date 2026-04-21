package com.MoneyFlow.app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun Double.formatCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    return format.format(this)
}
