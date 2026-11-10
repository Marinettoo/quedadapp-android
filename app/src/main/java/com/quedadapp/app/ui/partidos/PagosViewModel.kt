package com.quedadapp.app.ui.partidos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quedadapp.app.data.DatosPagos
import com.quedadapp.app.data.PagoFila
import com.quedadapp.app.data.PagosRepository
import kotlinx.coroutines.launch

// Igual que EquiposViewModel: sin id en el constructor, se pide
// cargar(partidoId) desde la pantalla.
class PagosViewModel(private val repo: PagosRepository) : ViewModel() {
    var datos by mutableStateOf<DatosPagos?>(null)
        private set
    var cargando by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var idsEnProceso by mutableStateOf<Set<String>>(emptySet())
        private set

    private var partidoIdActual: String? = null

    fun cargar(partidoId: String) {
        partidoIdActual = partidoId
        viewModelScope.launch {
            cargando = true
            error = null
            repo.pagos(partidoId)
                .onSuccess { datos = it }
                .onFailure { error = it.message ?: "No se pudieron cargar los pagos." }
            cargando = false
        }
    }

    fun indicarMiPago() = ejecutar("yo") { partidoId -> repo.indicarPago(partidoId) }

    fun deshacerMiIndicacion() = ejecutar("yo") { partidoId -> repo.deshacerIndicacion(partidoId) }

    fun confirmar(fila: PagoFila) = ejecutar(fila.jugadorId) { partidoId ->
        repo.confirmarPago(partidoId, fila.jugadorId, fila.tipo)
    }

    fun corregir(fila: PagoFila) = ejecutar(fila.jugadorId) { partidoId ->
        repo.corregirConfirmacion(partidoId, fila.jugadorId, fila.tipo)
    }

    private fun ejecutar(clave: String, accion: suspend (String) -> Result<Unit>) {
        val partidoId = partidoIdActual ?: return
        if (idsEnProceso.contains(clave)) return
        idsEnProceso = idsEnProceso + clave
        viewModelScope.launch {
            val resultado = accion(partidoId)
            idsEnProceso = idsEnProceso - clave
            resultado
                .onSuccess { cargar(partidoId) }
                .onFailure { error = it.message ?: "No se pudo completar la acción." }
        }
    }
}
