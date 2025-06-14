package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow // ESTE es el import que falta
import retrofit2.HttpException
import javax.inject.Inject

class UsuariosRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getUsuarios(usuarioId: Int): Flow<Resource<List<UsuarioDto>>> = flow {
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

    suspend fun saveUsuario(usuarioDto: UsuarioDto) = remoteDataSource.saveUsuario(usuarioDto)

    suspend fun deleteUsuario(id: Int) = remoteDataSource.deleteUsuario(id)

    suspend fun editUsuario(usuarioDto: UsuarioDto) = remoteDataSource.updateUsuario(usuarioDto)

    fun getUsuario(): Flow<Resource<List<UsuarioDto>>> = flow {
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
