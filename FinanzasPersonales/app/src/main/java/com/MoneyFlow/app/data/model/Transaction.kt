package com.MoneyFlow.app.data.model

data class Transaction(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val note: String = "",
    val date: Long = System.currentTimeMillis()
)

enum class TransactionType { INCOME, EXPENSE }
