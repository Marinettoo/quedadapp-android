package com.quedadapp.app.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.AuthRepository
import com.quedadapp.app.data.Usuario
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    var nombreUsuario by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun onNombreUsuarioChange(valor: String) {
        nombreUsuario = valor
    }

    fun onPasswordChange(valor: String) {
        password = valor
    }

    fun iniciarSesion(onExito: (Usuario) -> Unit) {
        if (cargando || nombreUsuario.isBlank() || password.isBlank()) return
        cargando = true
        error = null
        viewModelScope.launch {
            val resultado = repo.iniciarSesion(nombreUsuario.trim(), password)
            cargando = false
            resultado
                .onSuccess { onExito(it) }
                .onFailure { error = it.message ?: "No se pudo iniciar sesión." }
        }
    }
}
