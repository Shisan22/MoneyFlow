package com.MoneyFlow.app.data.model

data class Goal(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val targetAmount: Double = 0.0,
    val currentAmount: Double = 0.0,
    val deadline: Long = 0L,
    val category: String = "",
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val progress: Int get() = if (targetAmount > 0)
        ((currentAmount / targetAmount) * 100).toInt().coerceAtMost(100) else 0
}
