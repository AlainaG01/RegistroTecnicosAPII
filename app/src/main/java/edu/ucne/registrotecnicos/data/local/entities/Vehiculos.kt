package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Vehiculos")
data class VehiculoEntity (
    @PrimaryKey
    val vehiculoId: Int? = null,
    val descripcion: String = "",
    val precio: Double = 0.0
)