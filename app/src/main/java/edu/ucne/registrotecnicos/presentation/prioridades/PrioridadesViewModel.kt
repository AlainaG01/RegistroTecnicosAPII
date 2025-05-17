package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PrioridadesViewModel(
    private val prioridadesRepository: PrioridadesRepository
): ViewModel() {
    fun savePrioridad(prioridad: PrioridadEntity){
        viewModelScope.launch {
            prioridadesRepository.save(prioridad)
        }
    }

    suspend fun findPrioridad(id: Int): PrioridadEntity? {
        return prioridadesRepository.find(id)
    }

    fun deletePrioridad(prioridad: PrioridadEntity){
        viewModelScope.launch {
            prioridadesRepository.delete(prioridad)
        }
    }

    // Exponer las prioridades como StateFlow
    val prioridades: StateFlow<List<PrioridadEntity>> = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

}
