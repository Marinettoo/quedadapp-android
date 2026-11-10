package com.quedadapp.app.ui.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.AuthRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: AuthRepository) : ViewModel() {
    var nombreUsuario by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var password2 by mutableStateOf("")
        private set
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun onNombreUsuarioChange(valor: String) {
        nombreUsuario = valor
    }

    fun onEmailChange(valor: String) {
        email = valor
    }

    fun onPasswordChange(valor: String) {
        password = valor
    }

    fun onPassword2Change(valor: String) {
        password2 = valor
    }

    fun registrar(onExito: () -> Unit) {
        if (cargando) return
        cargando = true
        error = null
        viewModelScope.launch {
            val resultado = repo.registrar(
                nombreUsuario = nombreUsuario.trim(),
                email = email.trim(),
                password = password,
                password2 = password2,
            )
            cargando = false
            resultado
                .onSuccess { onExito() }
                .onFailure { error = it.message ?: "No se pudo completar el registro." }
        }
    }
}
