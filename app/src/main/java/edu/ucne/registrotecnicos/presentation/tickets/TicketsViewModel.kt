package edu.ucne.registrotecnicos.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.data.repository.TicketsRepository
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoEvent
import edu.ucne.registrotecnicos.presentation.tecnicos.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.Int

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
        when(event){
            is TicketEvent.AsuntoChange -> TicketEvent.AsuntoChange(event.asunto)
            is TicketEvent.ClienteChange -> TicketEvent.ClienteChange(event.cliente)
            TicketEvent.Delete -> deleteTicket()
            is TicketEvent.DescripcionChange -> TicketEvent.DescripcionChange(event.descripcion)
            is TicketEvent.FechaChange -> TicketEvent.FechaChange(event.fecha)
            TicketEvent.New -> nuevo()
            is TicketEvent.PrioridadChange -> TicketEvent.PrioridadChange(event.prioridadId)
            TicketEvent.Save -> saveTicket()
            is TicketEvent.TecnicoChange -> TicketEvent.TecnicoChange(event.tecnicoId)
            is TicketEvent.TicketChange -> TicketEvent.TicketChange(event.ticketId)
        }
    }

    init {
        getTicket()
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

    //cosas a eliminar


    //prioridad
   /*
   private val ticketList = MutableStateFlow<List<TicketEntity>>(emptyList())
    val tickets: StateFlow<List<TicketEntity>> = ticketList.asStateFlow()
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
        )*/
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