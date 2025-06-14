package edu.ucne.registrotecnicos.presentation.usuarios

sealed interface UsuarioEvent {
    data class UsuarioIdChange(val usuarioId: Int): UsuarioEvent
    data class NombreChange(val nombre: String): UsuarioEvent
    data class BalanceChange(val balance: Double): UsuarioEvent

    data object PostUsuario: UsuarioEvent
    data object GetUsuarios: UsuarioEvent
    data object Nuevo: UsuarioEvent
    data object LimpiarErrorMessageNombre: UsuarioEvent
    data object LimpiarErrorMessageBalance: UsuarioEvent
}