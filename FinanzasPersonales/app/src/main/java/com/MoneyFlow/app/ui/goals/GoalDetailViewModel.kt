package com.MoneyFlow.app.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Goal
import com.MoneyFlow.app.data.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalDetailViewModel : ViewModel() {
    private val repo = GoalRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _goal = MutableLiveData<Goal?>()
    val goal: LiveData<Goal?> = _goal

    fun load(id: String) {
        viewModelScope.launch { _goal.value = repo.getAll(uid).find { it.id == id } }
    }

    fun delete(id: String) {
        viewModelScope.launch { repo.delete(uid, id) }
    }
}
