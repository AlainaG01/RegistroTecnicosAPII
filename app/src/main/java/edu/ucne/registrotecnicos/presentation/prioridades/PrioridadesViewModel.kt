package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrioridadesViewModel @Inject constructor(
    private val prioridadesRepository: PrioridadesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: PrioridadEvent){
        when (event) {
            PrioridadEvent.Delete -> delete()
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            PrioridadEvent.New -> nuevo()
            is PrioridadEvent.PrioridadChange -> onPrioridadIdChange(event.prioridadId)
            PrioridadEvent.Save -> save()
            is PrioridadEvent.TiempoChange -> onTiempoChange(event.tiempo.toString())
        }
    }

    init {
        getPrioridad()
    }

    //savePrioridad
    private fun save() {
        viewModelScope.launch {
            if (_uiState.value.descripcion.isNullOrBlank() && _uiState.value.tiempo > 0){
                _uiState.update {
                    it.copy(errorMessage = "Campo vacios")
                }
            }
            else{
                prioridadesRepository.save(_uiState.value.toEntity())
            }
        }
    }

    private fun nuevo(){
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                tiempo = 0,
                errorMessage = null
            )
        }
    }

    //findPrioridad
    fun selectedPrioridad(prioridadId: Int){
        viewModelScope.launch {
            if(prioridadId > 0){
                val prioridad = prioridadesRepository.find(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: "",
                        tiempo = prioridad?.tiempo ?: 0
                    )
                }
            }
        }
    }

    //deletePrioridad
    private fun delete() {
        viewModelScope.launch {
            prioridadesRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getPrioridad() {
        viewModelScope.launch {
            prioridadesRepository.getAll().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }


    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onTiempoChange(tiempo: String) {
        _uiState.update {
            it.copy(tiempo = tiempo.toInt())
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    //funciones a eliminar

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

fun PrioridadUiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: "",
    tiempo = tiempo ?: 0
)