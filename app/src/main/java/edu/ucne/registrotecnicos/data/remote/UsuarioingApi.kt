package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.UsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioingApi {
    @GET("api/Usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): List<UsuarioDto>

    @PUT("api/Usuarios/{id}")
    suspend fun updateUsuario(@Body usuarioDto: UsuarioDto): UsuarioDto

    @POST("api/Usuarios")
    suspend fun saveUsuario(@Body usuarioDto: UsuarioDto): UsuarioDto

    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Unit>

    //Vehiculos
    @GET("api/Vehiculos")
    suspend fun getVehiculos(): List<VehiculoDto>

    @GET("api/Vehiculos/{id}")
    suspend fun getVehiculos(@Path("id") id: Int): List<VehiculoDto>

    @PUT("api/Vehiculos/{id}")
    suspend fun updateVehiculo(@Body vehiculoDto: VehiculoDto): VehiculoDto

    @POST("api/Vehiculos")
    suspend fun saveVehiculo(@Body vehiculoDto: VehiculoDto): VehiculoDto

    @DELETE("api/Vehiculos/{id}")
    suspend fun deleteVehiculo(@Path("id") id: Int): Response<Unit>
}