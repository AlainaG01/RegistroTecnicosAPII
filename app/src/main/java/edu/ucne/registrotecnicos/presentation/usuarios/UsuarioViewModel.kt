package edu.ucne.registrotecnicos.presentation.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.UsuarioDto
import edu.ucne.registrotecnicos.data.repository.UsuariosRepository
import edu.ucne.registrotecnicos.presentation.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val usuarioRepository: UsuariosRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getUsuarios()
    }

    fun onEvent(event: UsuarioEvent) {
        when (event) {
            is UsuarioEvent.BalanceChange -> balanceChange(event.balance)
            UsuarioEvent.GetUsuarios -> getUsuarios()
            UsuarioEvent.LimpiarErrorMessageBalance -> limpiarErrorMessageBalance()
            UsuarioEvent.LimpiarErrorMessageNombre -> limpiarErrorMessageNombre()
            is UsuarioEvent.NombreChange -> nombreChange(event.nombre)
            UsuarioEvent.Nuevo -> nuevo()
            UsuarioEvent.PostUsuario -> addUsuario()
            is UsuarioEvent.UsuarioIdChange -> usuarioIdChange(event.usuarioId)
        }
    }

    private fun limpiarErrorMessageNombre() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorNombre = "")
            }
        }
    }

    private fun limpiarErrorMessageBalance() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorBalance = "")
            }
        }
    }

    private fun nombreChange(nombre: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nombre = nombre)
            }
        }
    }

    private fun usuarioIdChange(id: Int){
        viewModelScope.launch {
            _uiState.update {
                it.copy(usuarioId = id)
            }
        }
    }

    private fun balanceChange(balance: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(balance = balance)
            }
        }
    }

    private fun nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    nombre = "",
                    balance = 0.0,
                    errorNombre = "",
                    errorBalance = "",
                    errorMessage = "",
                )
            }
        }
    }

    private fun addUsuario() {
        viewModelScope.launch {
            var error = false
            if (_uiState.value.nombre.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorNombre = "Este campo es obligatorio *")
                }
                error = true
            }
            if (_uiState.value.balance <= 0) {
                _uiState.update {
                    it.copy(errorBalance = "Este campo es obligatorio y debe ser mayor que cero *")
                }
                error = true
            }
            if (error) return@launch

            usuarioRepository.saveUsuario(_uiState.value.toEntity())
            getUsuarios()
            nuevo()
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    private fun getUsuarios() {
        viewModelScope.launch {
            usuarioRepository.getUsuario().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                usuarios = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

fun UsuarioUiState.toEntity() = UsuarioDto(
    usuarioId = usuarioId,
    nombre = nombre ?: "",
    balance = balance ?: 0.0,
)