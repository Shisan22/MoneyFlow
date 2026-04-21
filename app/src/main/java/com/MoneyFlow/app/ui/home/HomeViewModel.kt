package com.MoneyFlow.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.model.TransactionType
import com.MoneyFlow.app.data.repository.TransactionRepository
import kotlinx.coroutines.launch

data class FinancialSummary(
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0
)

class HomeViewModel : ViewModel() {
    private val repo = TransactionRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _summary = MutableLiveData<FinancialSummary>()
    val summary: LiveData<FinancialSummary> = _summary

    private val _recentTransactions = MutableLiveData<List<Transaction>>()
    val recentTransactions: LiveData<List<Transaction>> = _recentTransactions

    fun loadData() {
        viewModelScope.launch {
            val transactions = repo.getAll(uid)
            val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            _summary.value = FinancialSummary(income - expense, income, expense)
            _recentTransactions.value = transactions.take(5)
        }
    }
}
