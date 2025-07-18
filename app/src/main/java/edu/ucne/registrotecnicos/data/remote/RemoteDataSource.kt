package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.UsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val usuarioingApi: UsuarioingApi
){
    suspend fun getUsuarios()= usuarioingApi.getUsuarios()

    suspend fun updateUsuario(usuarioDto: UsuarioDto)= usuarioingApi.updateUsuario(usuarioDto)

    suspend fun saveUsuario(usuarioDto: UsuarioDto)= usuarioingApi.saveUsuario(usuarioDto)

    suspend fun deleteUsuario(id: Int)= usuarioingApi.deleteUsuario(id)

    suspend fun getUsuario(id: Int)= usuarioingApi.getUsuario(id)

    //Vehiculos
    suspend fun getVehiculos() = usuarioingApi.getVehiculos()

    suspend fun getVehiculo(id: Int) = usuarioingApi.getVehiculos(id)

    suspend fun updateVehiculo(vehiculoDto: VehiculoDto) = usuarioingApi.updateVehiculo(vehiculoDto)

    suspend fun saveVehiculo(vehiculoDto: VehiculoDto) = usuarioingApi.saveVehiculo(vehiculoDto)

    suspend fun deleteVehiculo(idVehiculo: Int) = usuarioingApi.deleteVehiculo(idVehiculo)
}