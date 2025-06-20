package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.CreateUsuarioDto
import edu.ucne.registrotecnicos.data.remote.dto.UsuariosDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow // ESTE es el import que falta
import retrofit2.HttpException
import javax.inject.Inject

class UsuariosRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getUsuarios(usuarioId: Int): Flow<Resource<List<UsuariosDto>>> = flow {
        try {
            emit(Resource.Loading())
            val usuario = remoteDataSource.getUsuario(usuarioId)
            emit(Resource.Success(usuario))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    suspend fun saveUsuario(usuariosDto: CreateUsuarioDto) = remoteDataSource.saveUsuario(usuariosDto)

    suspend fun deleteUsuario(id: Int) = remoteDataSource.deleteUsuario(id)

    suspend fun editUsuario(usuariosDto: UsuariosDto) = remoteDataSource.updateUsuario(usuariosDto)

    fun getUsuario(): Flow<Resource<List<UsuariosDto>>> = flow {
        try {
            emit(Resource.Loading())
            val usuario = remoteDataSource.getUsuarios()
            emit(Resource.Success(usuario))
        } catch (e: HttpException) {
            emit(Resource.Error("Error de internet: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }
}
