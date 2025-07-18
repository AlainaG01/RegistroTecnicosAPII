package edu.ucne.registrotecnicos.presentation.vehiculo

import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto

data class VehiculoUiState(
    val vehiculoId: Int? = null,
    val descripcion: String = "",
    val errorDescripcion: String? = null,
    val errorMessage: String? = null,
    val precio: Double = 0.0,
    val errorPrecio: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val vehiculos: List<VehiculoDto> = emptyList(),
    val showSuccessMessage: Boolean = false,
    val successMessage: String = ""
)