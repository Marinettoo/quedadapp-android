package com.quedadapp.app.ui.partidos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.Partido
import com.quedadapp.app.data.PartidosRepository
import kotlinx.coroutines.launch

class PartidosViewModel(private val repo: PartidosRepository) : ViewModel() {
    var partidos by mutableStateOf<List<Partido>>(emptyList())
        private set
    var cargando by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    // Ids de partidos con un apuntarse/cancelar en curso: para deshabilitar
    // solo el botón de esa fila, no la lista entera.
    var idsEnProceso by mutableStateOf<Set<String>>(emptySet())
        private set

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            cargando = true
            error = null
            repo.listar()
                .onSuccess { partidos = it }
                .onFailure { error = it.message ?: "No se pudieron cargar los partidos." }
            cargando = false
        }
    }

    fun alternarAsistencia(partido: Partido) {
        if (idsEnProceso.contains(partido.id)) return
        idsEnProceso = idsEnProceso + partido.id
        viewModelScope.launch {
            val resultado = if (partido.yaApuntado) repo.cancelar(partido.id) else repo.apuntarse(partido.id)
            idsEnProceso = idsEnProceso - partido.id
            resultado
                .onSuccess { cargar() }
                .onFailure { error = it.message ?: "No se pudo completar la acción." }
        }
    }
}
