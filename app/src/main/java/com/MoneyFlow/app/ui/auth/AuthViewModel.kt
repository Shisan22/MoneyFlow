package com.MoneyFlow.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.MoneyFlow.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repo.login(email, password)
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it.uid) },
                onFailure = { AuthState.Error(it.message ?: "Error al iniciar sesión") }
            )
        }
    }

    fun register(name: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = repo.register(name, email, password)
            _authState.value = result.fold(
                onSuccess = { AuthState.Success(it.uid) },
                onFailure = { AuthState.Error(it.message ?: "Error al registrarse") }
            )
        }
    }
}
