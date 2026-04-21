package com.MoneyFlow.app.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Transaction
import com.MoneyFlow.app.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionDetailViewModel : ViewModel() {
    private val repo = TransactionRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _transaction = MutableLiveData<Transaction?>()
    val transaction: LiveData<Transaction?> = _transaction

    fun load(id: String) {
        viewModelScope.launch { _transaction.value = repo.getById(uid, id) }
    }

    fun delete(id: String) {
        viewModelScope.launch { repo.delete(uid, id) }
    }
}
