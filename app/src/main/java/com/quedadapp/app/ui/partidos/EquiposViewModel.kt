package com.quedadapp.app.ui.partidos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.DatosEquipos
import com.quedadapp.app.data.PartidosRepository
import kotlinx.coroutines.launch

// Sin id en el constructor (la fábrica de ViewModels no lo soporta
// todavía): se pide cargar() con el id del partido concreto desde la
// pantalla, igual que si fuera un parámetro de navegación.
class EquiposViewModel(private val repo: PartidosRepository) : ViewModel() {
    var datos by mutableStateOf<DatosEquipos?>(null)
        private set
    var cargando by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private var partidoIdCargado: String? = null

    fun cargar(partidoId: String) {
        if (partidoIdCargado == partidoId && datos != null) return
        partidoIdCargado = partidoId
        viewModelScope.launch {
            cargando = true
            error = null
            repo.equipos(partidoId)
                .onSuccess { datos = it }
                .onFailure { error = it.message ?: "No se pudieron cargar los equipos." }
            cargando = false
        }
    }
}
