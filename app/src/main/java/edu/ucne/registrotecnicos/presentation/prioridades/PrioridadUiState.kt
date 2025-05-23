package edu.ucne.registrotecnicos.presentation.prioridades

import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity

data class PrioridadUiState(
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val tiempo: Int = 0,
    val errorMessage: String? = null,
    val prioridades: List<PrioridadEntity> = emptyList()
)