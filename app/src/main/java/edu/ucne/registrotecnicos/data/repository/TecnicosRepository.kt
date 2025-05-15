package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

class TecnicosRepository(
    private val dao: TecnicoDao
) {
    suspend fun save(tecnico: TecnicoEntity) = dao.save(tecnico)

    suspend fun find(id: Int): TecnicoEntity? = dao.find(id)

    suspend fun delete(tecnico: TecnicoEntity) = dao.delete(tecnico)

    fun getAll(): Flow<List<TecnicoEntity>> = dao.getAll()
}