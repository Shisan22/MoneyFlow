package com.MoneyFlow.app.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {
    private val repo = TransactionRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var allTransactions = listOf<Transaction>()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    fun loadTransactions() {
        viewModelScope.launch {
            allTransactions = repo.getAll(uid)
            _transactions.value = allTransactions
        }
    }

    fun filter(type: String?) {
        _transactions.value = if (type == null) allTransactions
        else allTransactions.filter { it.type.name == type }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repo.delete(uid, id)
            loadTransactions()
        }
    }
}
