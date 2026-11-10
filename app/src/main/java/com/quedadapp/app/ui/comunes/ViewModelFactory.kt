package com.quedadapp.app.ui.comunes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quedadapp.app.data.AuthRepository
import com.quedadapp.app.ui.home.HomeViewModel
import com.quedadapp.app.ui.login.LoginViewModel
import com.quedadapp.app.ui.registro.RegistroViewModel

// Fábrica manual para pasar AuthRepository a los ViewModels sin montar
// Hilt/Dagger todavía: con tres pantallas basta esto para empezar.
class ViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        LoginViewModel::class.java -> LoginViewModel(authRepository) as T
        RegistroViewModel::class.java -> RegistroViewModel(authRepository) as T
        HomeViewModel::class.java -> HomeViewModel(authRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: $modelClass")
    }
}
