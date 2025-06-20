package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.CreateUsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.UsuariosDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioingApi {
    @GET("api/Usuarios")
    suspend fun getUsuarios(): List<UsuariosDto>

    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): List<UsuariosDto>

    @PUT("api/Usuarios/{id}")
    suspend fun updateUsuario(@Body usuariosDto: UsuariosDto): UsuariosDto

    @POST("api/Usuarios")
    suspend fun saveUsuario(@Body usuariosDto: CreateUsuarioDto): UsuariosDto

    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Unit>
}