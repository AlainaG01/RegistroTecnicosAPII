package edu.ucne.registrotecnicos.presentation.prioridades

sealed interface PrioridadEvent {
    data class PrioridadChange(val prioridadId: Int): PrioridadEvent
    data class DescripcionChange(val descripcion: String): PrioridadEvent
    data class TiempoChange(val tiempo: Int): PrioridadEvent
    data object Save: PrioridadEvent
    data object Delete: PrioridadEvent
    data object New: PrioridadEvent
}