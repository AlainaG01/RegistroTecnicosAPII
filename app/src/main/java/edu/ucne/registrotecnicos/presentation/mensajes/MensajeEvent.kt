package edu.ucne.registrotecnicos.presentation.mensajes

import java.util.Date

sealed interface MensajeEvent {
    data class MensajeChange(val mensajeId: Int): MensajeEvent
    data class FechaChange(val fecha: Date): MensajeEvent
    data class ContenidoChange(val contenido: String): MensajeEvent
    data class RemitenteChange(val remitente: String): MensajeEvent
    data class TipoRemitenteChange(val tipoRemitente: String): MensajeEvent
    data class TickectIdChange(val tickectId: Int): MensajeEvent
    data object Save: MensajeEvent
    data object Delete: MensajeEvent
    data object New: MensajeEvent
}