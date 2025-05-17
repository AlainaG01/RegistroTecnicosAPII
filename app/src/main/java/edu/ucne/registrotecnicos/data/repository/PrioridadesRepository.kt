package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow

class PrioridadesRepository(
    private val dao: PrioridadDao
) {
    suspend fun save(prioridad: PrioridadEntity) = dao.save(prioridad)

    suspend fun find(id: Int) = dao.find(id)

    suspend fun delete(prioridad: PrioridadEntity) = dao.delete(prioridad)

    fun getAll(): Flow<List<PrioridadEntity>> = dao.getAll()
}