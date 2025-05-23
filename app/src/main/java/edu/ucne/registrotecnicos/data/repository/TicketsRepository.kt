package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketsRepository @Inject constructor(
    private val dao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = dao.save(ticket)

    suspend fun find(id: Int): TicketEntity? = dao.find(id)

    suspend fun delete(ticket: TicketEntity) = dao.delete(ticket)

    fun getAll(): Flow<List<TicketEntity>> = dao.getAll()
}