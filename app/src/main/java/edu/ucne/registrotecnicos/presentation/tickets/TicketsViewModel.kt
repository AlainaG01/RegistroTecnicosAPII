package edu.ucne.registrotecnicos.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.data.repository.TicketsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val ticketsRepository: TicketsRepository,
    private val tecnicoRepository: TecnicosRepository,
    private  val prioridadesRepository: PrioridadesRepository

): ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState(
        prioridadId = 0,
        tecnicoId = 0
    ))
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: TicketEvent) {
        when (event) {
            is TicketEvent.AsuntoChange -> onAsuntoChange(event.asunto)
            is TicketEvent.ClienteChange -> onClienteChange(event.cliente)
            TicketEvent.Delete -> deleteTicket()
            is TicketEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            is TicketEvent.FechaChange -> onFechaChange(event.fecha)
            TicketEvent.New -> nuevo()
            is TicketEvent.PrioridadChange -> onPrioridadIdChange(event.prioridadId)
            TicketEvent.Save -> saveTicket()
            is TicketEvent.TecnicoChange -> onTecnicoIdChange(event.tecnicoId)
            is TicketEvent.TicketChange -> onTicketIdChange(event.ticketId)
        }
    }


    // Estado para las prioridades
    val prioridades = prioridadesRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Para Técnicos
    val tecnicos = tecnicoRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        getTicket()
        getTecnico()
        getPrioridad()
    }

    //saveTecnico
    private fun saveTicket() {
        viewModelScope.launch {
            if (_uiState.value.cliente.isNullOrBlank() && _uiState.value.prioridadId > 0
                && _uiState.value.descripcion.isNullOrBlank()
                && _uiState.value.asunto.isNullOrBlank()
                && _uiState.value.tecnicoId > 0){
                _uiState.update {
                    it.copy(errorMessage = "Campo vacios")
                }
            }
            else{
                ticketsRepository.save(_uiState.value.toEntity())
            }
        }
    }

    private fun nuevo(){
        _uiState.update {
            it.copy(
                ticketId = null,
                fecha = Date(),
                prioridadId = 0,
                cliente = "",
                asunto = "",
                descripcion =  "",
                tecnicoId = 0,
                errorMessage = null
            )
        }
    }

    fun findTicket(ticketId: Int){
        viewModelScope.launch {
            if(ticketId > 0){
                val ticket = ticketsRepository.find(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha ?: Date(),
                        prioridadId = ticket?.prioridadId ?: 0,
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: "",
                        tecnicoId = ticket?.tecnicoId ?: 0
                    )
                }
            }
        }
    }

    private fun deleteTicket() {
        viewModelScope.launch {
            ticketsRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTicket() {
        viewModelScope.launch {
            ticketsRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }

    private fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente)
        }
    }

    private fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    private fun onTecnicoIdChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    private fun getPrioridad() {
        viewModelScope.launch {
            prioridadesRepository.getAll().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    private fun getTecnico() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }
}

fun TicketUiState.toEntity() = TicketEntity(
    ticketId = ticketId,
    fecha = fecha ?: Date(),
    prioridadId = prioridadId ?: 0,
    cliente = cliente ?: "",
    asunto = asunto ?: "",
    descripcion =  descripcion ?: "",
    tecnicoId = tecnicoId ?: 0
)