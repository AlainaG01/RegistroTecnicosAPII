package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.VehiculoDao
import edu.ucne.registrotecnicos.data.local.entities.VehiculoEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class VehiculosRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val vehiculosDao: VehiculoDao
) {
    fun getVehiculo(id: Int): Flow<Resource<VehiculoDto>> {
        return flow {
            try {
                emit(Resource.Loading())
                val vehiculos = remoteDataSource.getVehiculo(id)
                if (vehiculos.isNotEmpty()) {
                    emit(Resource.Success(vehiculos.first()))
                } else {
                    emit(Resource.Error("No se encontró el vehículo"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.localizedMessage ?: "Error desconocido"}"))
            }
        }
    }

    fun getVehiculos(): Flow<Resource<List<VehiculoDto>>> = flow {
        var listVehiculosDto: List<VehiculoEntity> = emptyList()
        try {
            emit(Resource.Loading())
            val vehiculos = remoteDataSource.getVehiculos()
            val vehiculosEntity = vehiculos.map {
                it.toEntity()
            }
            vehiculosDao.save(vehiculosEntity)
//            emit(Resource.Success(vehiculos))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            //emit(Resource.Error("Error ${e.message}"))
        }
        listVehiculosDto = vehiculosDao.getAll()
        val listaVehiculoDto = listVehiculosDto.map {
            it.toDto()
        }

        emit(Resource.Success(listaVehiculoDto))
    }

    private fun VehiculoDto.toEntity() = VehiculoEntity(
        vehiculoId = this.vehiculoId,
        descripcion = this.descripcion ?: "",
        precio = this.precio ?: 0.0
    )

    private fun VehiculoEntity.toDto() = VehiculoDto(
        vehiculoId = this.vehiculoId,
        descripcion = this.descripcion ?: "",
        precio = this.precio ?: 0.0
    )


    suspend fun saveVehiculo(vehiculoDto: VehiculoDto) = remoteDataSource.saveVehiculo(vehiculoDto)

    suspend fun editVehiculo(vehiculoDto: VehiculoDto) = remoteDataSource.updateVehiculo(vehiculoDto)

    suspend fun deleteVehiculo(idVehiculo: Int) = remoteDataSource.deleteVehiculo(idVehiculo)
}