package edu.ucne.registrotecnicos.presentation.tickets

import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import java.util.Date

data class TicketUiState(
    val ticketId: Int? = null,
    val fecha: Date = Date(),
    val prioridadId: Int,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int,
    val errorMessage: String? = null,
    val tickets: List<TicketEntity> = emptyList(),
    val tecnicos: List<TecnicoEntity> = emptyList(),
    val prioridades: List<PrioridadEntity> = emptyList()
)