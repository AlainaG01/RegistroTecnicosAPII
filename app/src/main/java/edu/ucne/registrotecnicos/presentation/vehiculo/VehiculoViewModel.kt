package edu.ucne.registrotecnicos.presentation.vehiculo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto
import edu.ucne.registrotecnicos.data.repository.VehiculosRepository
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
class VehiculoViewModel @Inject constructor(
    private val vehiculosRepository: VehiculosRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehiculoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getVehiculo()
    }

    fun OnEvent(event: VehiculoEvent) {
        when (event) {
            is VehiculoEvent.DescripcionChange -> DescripcionChange(event.descripcion)
            VehiculoEvent.GetVehiculo -> getVehiculo()
            VehiculoEvent.LimpiarErrorMessageDescripcion -> LimpiarErrorMessageDescripcion()
            is VehiculoEvent.Nuevo -> Nuevo()
            VehiculoEvent.PostVehiculo -> addVehiculo()
            is VehiculoEvent.PrecioChange -> PrecioChange(event.precio)
            is VehiculoEvent.VehiculoChange -> VehiculoChange(event.vehiculoId)
            VehiculoEvent.LimpiarErrorMessagePrecio -> LimpiarErrorMessagePrecio()
            is VehiculoEvent.ResetSuccessMessage -> {
                _uiState.update { it.copy(showSuccessMessage = false, successMessage = "") }
            }
        }
    }

    private fun addVehiculo() {
        viewModelScope.launch {
            var error = false

            // Validaciones existentes
            if (_uiState.value.descripcion.isNullOrBlank()) {
                _uiState.update {
                    it.copy(errorDescripcion = "El campo de descripcion es obligatorio ")
                }
                error = true
            }

            if (_uiState.value.precio <= 0) {
                _uiState.update {
                    it.copy(errorPrecio = "Este campo es obligatorio y debe ser mayor que cero ")
                }
                error = true
            }

            if (error) return@launch

            try {
                vehiculosRepository.saveVehiculo(_uiState.value.toEntity())

                _uiState.update {
                    it.copy(
                        showSuccessMessage = true,
                        successMessage = "Vehículo guardado correctamente",
                        errorMessage = null
                    )
                }


                getVehiculo()
                Nuevo()

                delay(2000)
                _uiEvent.send(UiEvent.NavigateUp)

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar el vehículo: ${e.localizedMessage}",
                        showSuccessMessage = false
                    )
                }
            }
        }
    }

    fun findVehiculo(vehiculoId: Int) {
        viewModelScope.launch {
            if (vehiculoId > 0) {
                vehiculosRepository.getVehiculo(vehiculoId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val vehiculo = resource.data
                            _uiState.update {
                                it.copy(
                                    vehiculoId = vehiculo?.vehiculoId,
                                    descripcion = vehiculo?.descripcion ?: "",
                                    precio = vehiculo?.precio ?: 0.0,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    errorMessage = resource.message ?: "Error al cargar el vehículo",
                                    isLoading = false
                                )
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

    private fun LimpiarErrorMessageDescripcion() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorDescripcion = "")
            }
        }
    }

    private fun LimpiarErrorMessagePrecio() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorPrecio = "")
            }
        }
    }

    private fun PrecioChange(precio: Double) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(precio = precio)
            }
        }
    }

    private fun DescripcionChange(descripcion: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(descripcion = descripcion)
            }
        }
    }

    private fun VehiculoChange(vehiculoId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(vehiculoId = vehiculoId)
            }
        }
    }

    private fun Nuevo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    descripcion = "",
                    precio = 0.0,
                    errorDescripcion = "",
                    errorPrecio = "",
                    errorMessage = "",
                )
            }
        }
    }

    private fun getVehiculo() {
        viewModelScope.launch {
            vehiculosRepository.getVehiculos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                vehiculos = result.data ?: emptyList(),
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

fun VehiculoUiState.toEntity() = VehiculoDto(
    vehiculoId = vehiculoId,
    descripcion = descripcion,
    precio = precio,
)