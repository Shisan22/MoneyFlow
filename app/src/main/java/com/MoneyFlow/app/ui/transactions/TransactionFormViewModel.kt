package com.MoneyFlow.app.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionFormViewModel : ViewModel() {
    private val repo = TransactionRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _transaction = MutableLiveData<Transaction?>()
    val transaction: LiveData<Transaction?> = _transaction

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    fun load(id: String) {
        viewModelScope.launch { _transaction.value = repo.getById(uid, id) }
    }

    fun save(transaction: Transaction) {
        viewModelScope.launch {
            val result = repo.save(uid, transaction)
            if (result.isSuccess) _saved.value = true
        }
    }
}
