package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketListScreen(
    viewModel: TicketsViewModel = hiltViewModel(),
    goToTicket: (Int) -> Unit,
    goToMensaje: (Int) -> Unit,
    createTicket: () -> Unit,
    deleteTicket: ((TicketEntity) -> Unit)? = null,

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketListBodyScreen(
        uiState = uiState,
        goToTicket = goToTicket,
        goToMensaje = goToMensaje,
        createTicket = createTicket,
        deleteTicket = { ticket ->
            viewModel.onEvent(TicketEvent.TicketChange(ticket.ticketId ?: 0))
            viewModel.onEvent(TicketEvent.Delete)
        }
    )
}

@Composable
private fun TicketRow(
    it: TicketEntity,
    goToTicket: (Int) -> Unit,
    goToMensaje: (Int) -> Unit,
    createTicket: () -> Unit,
    deleteTicket: (TicketEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = it.ticketId.toString(), color = Color.Black)
        Text(
            modifier = Modifier.weight(2f),
            text = it.fecha.toFormattedString(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Text(modifier = Modifier.weight(2f), text = it.descripcion, color = Color.Black)

        // Botón para ir al chat
        IconButton(onClick = { goToMensaje(it.ticketId ?: 0) }) {
            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = "Chat",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(onClick = createTicket) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = {deleteTicket(it)}) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }

    }
    HorizontalDivider()
}
fun Date.toFormattedString(): String {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(this)
}

@Composable
fun TicketListBodyScreen(
    uiState: TicketUiState,
    goToTicket: (Int) -> Unit,
    goToMensaje: (Int) -> Unit,
    createTicket: () -> Unit,
    deleteTicket: (TicketEntity) -> Unit
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = createTicket) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de tickets")
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(uiState.tickets) { ticket ->
                    TicketRow(
                        it = ticket,
                        goToTicket = goToTicket,
                        goToMensaje = goToMensaje,
                        createTicket = { goToTicket(ticket.ticketId ?: 0) },
                        deleteTicket = deleteTicket
                    )
                }
            }
        }
    }
}

/*@Preview
@Composable
private fun Preview() {
    val tecnicos = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Juan",
            sueldo = 100.0
        ),
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "Jose",
            sueldo = 200.0
        )
    )
    RegistroTecnicosTheme {
        TecnicoListScreen(
            tecnicoList = tecnicos,
            onEdit = {},
            onDelete = {}
        )
    }
}
*/