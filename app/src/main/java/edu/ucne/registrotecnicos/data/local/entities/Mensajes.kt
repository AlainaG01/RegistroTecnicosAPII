package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Mensajes",
    foreignKeys = [
        ForeignKey(
            entity = TicketEntity::class,
            parentColumns = ["ticketId"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MensajeEntity(
    @PrimaryKey
    val mensajeId: Int? = null,
    val fecha: Date = Date(),
    val contenido: String = "",
    val remitente: String = "",
    val tipoRemitente: String = "",
    val ticketId: Int = 0
)