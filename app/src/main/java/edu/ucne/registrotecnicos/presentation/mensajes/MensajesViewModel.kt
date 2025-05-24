package edu.ucne.registrotecnicos.presentation.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.repository.MensajesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val mensajesRepository: MensajesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MensajeUiState(
        ticketId = 0
    ))
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: MensajeEvent){
        when(event) {
            is MensajeEvent.ContenidoChange -> onContenidoChange(event.contenido)
            MensajeEvent.Delete -> deleteMensaje()
            is MensajeEvent.FechaChange -> onFechaChange(event.fecha)
            is MensajeEvent.MensajeChange -> onMensajeIdChange(event.mensajeId)
            MensajeEvent.New -> nuevo()
            is MensajeEvent.RemitenteChange -> onRemitenteChange(event.remitente)
            MensajeEvent.Save -> enviarMensaje()
            is MensajeEvent.TickectIdChange -> onTicketIdChange(event.tickectId)
            is MensajeEvent.TipoRemitenteChange -> onTipoRemitenteChange(event.tipoRemitente)
        }
    }

    //getAll
    fun cargarMensajes(ticketId: Int) {
        viewModelScope.launch {
            mensajesRepository.getAll(ticketId).collect { lista ->
                _uiState.update {
                    it.copy(mensajes = lista)
                }
            }
        }
    }
    // save
    private fun enviarMensaje() {
        viewModelScope.launch {
            if (_uiState.value.contenido.isNullOrBlank()){
                _uiState.update {
                    it.copy(errorMessage = "Campo vacio!!!")
                }
            }
            else{
                mensajesRepository.save(_uiState.value.toEntity())
                cargarMensajes(_uiState.value.ticketId ?: 0) // <-- Refresca los mensajes
                // Limpia el campo de texto
                _uiState.update {
                    it.copy(contenido = "")
                }
            }
        }
    }

    private fun deleteMensaje() {
        viewModelScope.launch {
            mensajesRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun nuevo(){
        _uiState.update {
            it.copy(
                mensajeId = null,
                fecha = Date(),
                contenido = "",
                remitente = "",
                ticketId = 0,
            )
        }
    }

    private fun onFechaChange(fecha: Date) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    private fun onMensajeIdChange(id: Int) {
        _uiState.update {
            it.copy(mensajeId = id)
        }
    }

    private fun onContenidoChange(contenido: String){
        _uiState.update {
            it.copy(contenido = contenido)
        }
    }

    private fun onRemitenteChange(remitente: String){
        _uiState.update {
            it.copy(remitente = remitente)
        }
    }

    private fun onTipoRemitenteChange(tipoRemitente: String){
        _uiState.update {
            it.copy(tipoRemitente = tipoRemitente)
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
        cargarMensajes(ticketId)
    }
}

fun MensajeUiState.toEntity() = MensajeEntity(
    mensajeId = mensajeId,
    fecha = fecha ?: Date(),
    contenido = contenido ?: "",
    remitente = remitente ?: "",
    tipoRemitente = tipoRemitente ?: "",
    ticketId = ticketId ?: 0
)
