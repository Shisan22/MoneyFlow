package com.MoneyFlow.app.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Goal
import com.MoneyFlow.app.data.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalsViewModel : ViewModel() {
    private val repo = GoalRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _goals = MutableLiveData<List<Goal>>()
    val goals: LiveData<List<Goal>> = _goals

    fun loadGoals() {
        viewModelScope.launch { _goals.value = repo.getAll(uid) }
    }

    fun delete(id: String) {
        viewModelScope.launch { repo.delete(uid, id); loadGoals() }
    }
}
