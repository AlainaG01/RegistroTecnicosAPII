package edu.ucne.registrotecnicos.presentation.mensajes

import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import java.util.Date

data class MensajeUiState(
    val mensajeId: Int? = null,
    val fecha: Date = Date(),
    val contenido: String? = null,
    val remitente: String? = null,
    val ticketId: Int? = 0,
    val errorMessage: String? = null,
    val mensajes: List<MensajeEntity> = emptyList()
)