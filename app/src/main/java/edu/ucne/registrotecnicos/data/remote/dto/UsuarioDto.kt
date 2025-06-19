package edu.ucne.registrotecnicos.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    val usuarioId: Int?,
    val nombre: String,
    val balance: Double
)

/*
* En caso de que los nombres de los campos en el JSON no coinciden con los nombres de tus propiedades
* utilizamos @SerialName ejemplo
*
* @Serializable
data class UsuarioDto(
    @SerialName("id_usuario") val usuarioId: Int?,
    @SerialName("nombre_completo") val nombre: String,
    val balance: Double
)
* */