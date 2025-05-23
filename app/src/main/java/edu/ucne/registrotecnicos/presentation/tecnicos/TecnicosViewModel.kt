package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicosViewModel @Inject constructor(
    private val tecnicosRepository: TecnicosRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: TecnicoEvent){
        when (event) {
            TecnicoEvent.Delete -> delete()
            TecnicoEvent.New -> nuevo()
            is TecnicoEvent.NombreChange -> onNombreChange(event.nombre)
            TecnicoEvent.Save -> save()
            is TecnicoEvent.SueldoChange -> onSueldoChange(event.sueldo)
            is TecnicoEvent.TecnicoChange -> onTecnicoIdChange(event.tecnicoId)
        }
    }


    init {
        getTecnico()
    }

    //saveTecnico
     private fun save() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isNullOrBlank() || _uiState.value.sueldo < 0.0){
                _uiState.update {
                    it.copy(errorMessage = "Campo vacios")
                }
            }
            else{
                tecnicosRepository.save(_uiState.value.toEntity())
            }
        }
    }

    private fun nuevo(){
        _uiState.update {
            it.copy(
                tecnicoId = null,
                nombre = "",
                sueldo = 0.0,
                errorMessage = null
            )
        }
    }

    //findTecnico
    fun selectedTecnico(tecnicoId: Int){
        viewModelScope.launch {
            if(tecnicoId > 0){
                val tecnico = tecnicosRepository.find(tecnicoId)
                _uiState.update {
                    it.copy(
                        tecnicoId = tecnico?.tecnicoId,
                        nombre = tecnico?.nombre ?: "",
                        sueldo = tecnico?.sueldo ?: 0.0
                    )
                }
            }
        }
    }

    //deleteTecnico
    private fun delete() {
        viewModelScope.launch {
            tecnicosRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTecnico() {
        viewModelScope.launch {
            tecnicosRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    private fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(nombre = nombre)
        }
    }

    private fun onSueldoChange(sueldo: Double) {
        _uiState.update {
            it.copy(sueldo = sueldo)
        }
    }

    private fun onTecnicoIdChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }

}


fun TecnicoUiState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombre = nombre ?: "",
    sueldo = sueldo ?: 0.0
)