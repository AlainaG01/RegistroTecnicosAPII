package edu.ucne.registrotecnicos.presentation.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.CreateUsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.UsuariosDto
import edu.ucne.registrotecnicos.data.repository.UsuariosRepository
import edu.ucne.registrotecnicos.presentation.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
            UsuarioEvent.ResetSuccessMessage -> _uiState.update { it.copy(isSuccess = false, successMessage = null) }
            is UsuarioEvent.GetUsuario -> findUsuario(event.id)
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
            try {
                usuarioRepository.saveUsuario(_uiState.value.toEntity())

                // Actualizar estado con mensaje de éxito
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        successMessage = "Usuario guardado correctamente",
                        errorMessage = null
                    )
                }

                getUsuarios()
                nuevo()

                // Navegar de regreso después de un breve retraso para que se vea el mensaje
                delay(2000) // Espera 2 segundos para mostrar el mensaje
                _uiEvent.send(UiEvent.NavigateUp)
            }catch (e: retrofit2.HttpException) {
                if (e.code() == 500) {
                    // Si es un error 500, usa los datos locales y notifica
                    _uiState.update {
                        it.copy(
                            isSuccess = true,
                            successMessage = "Usuario guardado. Falló sincronización con el servidor (500).",
                            errorMessage = null
                        )
                    }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error en la API: ${e.code()} - ${e.message}",
                        isSuccess = false
                    )
                }
                return@launch // Salir si es otro error de API

            }
        }catch (e: Exception){
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar el usuario: ${e.localizedMessage}",
                        isSuccess = false
                    )
                }
            }

            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    fun findUsuario(usuarioId: Int) {
        viewModelScope.launch {
            if (usuarioId > 0) {
                usuarioRepository.getUsuarios(usuarioId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val usuario = resource.data?.firstOrNull()
                            _uiState.update {
                                it.copy(
                                    usuarioId = usuario?.usuarioId,
                                    nombre = usuario?.nombre ?: "",
                                    balance = usuario?.balance ?: 0.0
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(errorMessage = resource.message)
                            }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            }
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

fun UsuarioUiState.toEntity() = CreateUsuarioDto(
    nombre = nombre ?: "",
    balance = balance ?: 0.0,
)