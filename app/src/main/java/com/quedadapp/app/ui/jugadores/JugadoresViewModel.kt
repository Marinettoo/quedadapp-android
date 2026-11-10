package com.quedadapp.app.ui.jugadores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.Jugador
import com.quedadapp.app.data.JugadoresRepository
import kotlinx.coroutines.launch

class JugadoresViewModel(private val repo: JugadoresRepository) : ViewModel() {
    var jugadores by mutableStateOf<List<Jugador>>(emptyList())
        private set
    var cargando by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var idsValorando by mutableStateOf<Set<String>>(emptySet())
        private set

    val token: String? get() = repo.tokenActual()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            cargando = true
            error = null
            repo.listar()
                .onSuccess { jugadores = it }
                .onFailure { error = it.message ?: "No se pudieron cargar los jugadores." }
            cargando = false
        }
    }

    fun valorar(jugador: Jugador, estrellas: Int) {
        if (idsValorando.contains(jugador.id)) return
        idsValorando = idsValorando + jugador.id
        viewModelScope.launch {
            val resultado = repo.valorar(jugador.id, estrellas)
            idsValorando = idsValorando - jugador.id
            resultado
                .onSuccess { cargar() }
                .onFailure { error = it.message ?: "No se pudo guardar la valoración." }
        }
    }
}
