package edu.ucne.registrotecnicos.presentation.tickets

import java.util.Date


sealed interface TicketEvent {
    data class TicketChange(val ticketId: Int): TicketEvent
    data class FechaChange(val fecha: Date): TicketEvent
    data class PrioridadChange(val prioridadId: Int): TicketEvent
    data class ClienteChange(val cliente: String): TicketEvent
    data class AsuntoChange(val asunto: String): TicketEvent
    data class DescripcionChange(val descripcion: String): TicketEvent
    data class TecnicoChange(val tecnicoId: Int): TicketEvent
    data object Save: TicketEvent
    data object Delete: TicketEvent
    data object New: TicketEvent
}