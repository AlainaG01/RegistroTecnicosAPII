package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.CreateUsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.UsuariosDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val usuarioingApi: UsuarioingApi
){
    suspend fun getUsuarios()= usuarioingApi.getUsuarios()

    suspend fun updateUsuario(usuariosDto: UsuariosDto)= usuarioingApi.updateUsuario(usuariosDto)

    suspend fun saveUsuario(usuariosDto: CreateUsuarioDto)= usuarioingApi.saveUsuario(usuariosDto)

    suspend fun deleteUsuario(id: Int)= usuarioingApi.deleteUsuario(id)

    suspend fun getUsuario(id: Int)= usuarioingApi.getUsuario(id)
}