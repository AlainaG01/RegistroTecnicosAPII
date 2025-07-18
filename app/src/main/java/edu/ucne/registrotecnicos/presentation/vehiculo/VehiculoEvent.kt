package edu.ucne.registrotecnicos.presentation.vehiculo

sealed interface VehiculoEvent {
    data class VehiculoChange(val vehiculoId: Int):VehiculoEvent
    data class DescripcionChange(val descripcion: String): VehiculoEvent
    data class PrecioChange(val precio: Double): VehiculoEvent

    data object PostVehiculo: VehiculoEvent
    data object GetVehiculo: VehiculoEvent
    data object Nuevo: VehiculoEvent
    data object LimpiarErrorMessageDescripcion: VehiculoEvent
    data object LimpiarErrorMessagePrecio: VehiculoEvent
    object ResetSuccessMessage : VehiculoEvent
}