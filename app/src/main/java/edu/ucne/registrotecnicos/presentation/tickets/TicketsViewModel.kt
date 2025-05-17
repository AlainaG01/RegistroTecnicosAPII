package edu.ucne.registrotecnicos.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.data.repository.TicketsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketsViewModel(
    private val ticketsRepository: TicketsRepository,
    private val tecnicoRepository: TecnicosRepository,
    private  val prioridadesRepository: PrioridadesRepository

): ViewModel() {
    private val ticketList = MutableStateFlow<List<TicketEntity>>(emptyList())
    val tickets: StateFlow<List<TicketEntity>> = ticketList.asStateFlow()

    //prioridad
    val ListaPrioridades = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //tecnico
    val ListaTecnicos = tecnicoRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getAllTickets()
    }

    fun getAllTickets() {
        ticketsRepository.getAll()
            .onEach { tickets ->
                ticketList.value = tickets
            }
            .launchIn(viewModelScope)
    }

    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketsRepository.save(ticket)
        }
    }

    suspend fun findTicket(id: Int): TicketEntity? {
        return ticketsRepository.find(id)
    }

    fun deleteTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketsRepository.delete(ticket)
        }
    }
    val ticketsS: StateFlow<List<TicketEntity>> = ticketsRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}