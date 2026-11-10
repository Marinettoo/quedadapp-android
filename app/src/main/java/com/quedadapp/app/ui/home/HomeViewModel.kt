package com.quedadapp.app.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.AuthRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: AuthRepository) : ViewModel() {
    var cerrandoSesion by mutableStateOf(false)
        private set

    fun cerrarSesion(onSesionCerrada: () -> Unit) {
        if (cerrandoSesion) return
        cerrandoSesion = true
        viewModelScope.launch {
            repo.cerrarSesion()
            cerrandoSesion = false
            onSesionCerrada()
        }
    }
}
