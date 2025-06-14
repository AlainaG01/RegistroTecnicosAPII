package edu.ucne.registrotecnicos.presentation.usuarios

import edu.ucne.registrotecnicos.data.remote.dto.UsuarioDto

data class UsuarioUiState(
    val usuarioId: Int? = null,
    val nombre: String = "",
    val balance: Double = 0.0,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorNombre: String? = null,
    val errorBalance: String? = null,
    val usuarios: List<UsuarioDto> = emptyList()
)