package com.MoneyFlow.app.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.MoneyFlow.app.data.model.Goal
import com.MoneyFlow.app.data.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalFormViewModel : ViewModel() {
    private val repo = GoalRepository()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val _goal = MutableLiveData<Goal?>()
    val goal: LiveData<Goal?> = _goal

    private val _saved = MutableLiveData(false)
    val saved: LiveData<Boolean> = _saved

    fun load(id: String) {
        viewModelScope.launch { _goal.value = repo.getAll(uid).find { it.id == id } }
    }

    fun save(goal: Goal) {
        viewModelScope.launch {
            if (repo.save(uid, goal).isSuccess) _saved.value = true
        }
    }
}
