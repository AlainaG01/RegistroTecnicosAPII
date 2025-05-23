package edu.ucne.registrotecnicos.presentation.tecnicos

import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

data class TecnicoUiState(
    val tecnicoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double = 0.0,
    val errorMessage: String? = null,
    val tecnicos: List<TecnicoEntity> = emptyList()
)