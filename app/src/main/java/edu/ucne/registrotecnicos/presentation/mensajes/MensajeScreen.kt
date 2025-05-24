package edu.ucne.registrotecnicos.presentation.mensajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MensajeScreen(
    viewModel: MensajesViewModel = hiltViewModel(),
    goBack: () -> Unit,
    ticketId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        viewModel.onEvent(MensajeEvent.TickectIdChange(ticketId))
    }

    MensajeBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@Composable
fun MensajeBodyScreen(
    uiState: MensajeUiState,
    onEvent: (MensajeEvent) -> Unit,
    goBack: () -> Unit
) {
    var selectedRemitente by remember { mutableStateOf(uiState.remitente ?: "Operator") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = goBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                }
            }

            val listState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true,
                state = listState
            ) {
                items(uiState.mensajes) { mensaje ->
                    MensajeRow(
                        mensaje = mensaje
                        )
                }
            }

            // Esto hará scroll al tope cuando cambia la lista
            LaunchedEffect(uiState.mensajes.size) {
                listState.animateScrollToItem(0)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Reply", style = MaterialTheme.typography.titleMedium)

            // Remitente radio buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRemitente == "Operator",
                    onClick = {
                        selectedRemitente = "Operator"
                        onEvent(MensajeEvent.TipoRemitenteChange("Operator"))
                    }
                )
                Text("Operator", modifier = Modifier.padding(end = 16.dp))

                RadioButton(
                    selected = selectedRemitente == "Owner",
                    onClick = {
                        selectedRemitente = "Owner"
                        onEvent(MensajeEvent.TipoRemitenteChange("Owner"))
                    }
                )
                Text("Owner")
            }

            // Nombre
            OutlinedTextField(
                value = uiState.remitente ?: "",
                onValueChange = { onEvent(MensajeEvent.RemitenteChange(it)) },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Mensaje
            OutlinedTextField(
                value = uiState.contenido ?: "",
                onValueChange = { onEvent(MensajeEvent.ContenidoChange(it)) },
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Botón enviar
            Button(
                onClick = { onEvent(MensajeEvent.Save) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun MensajeRow(
    mensaje: MensajeEntity
    ) {
    val isOperator = mensaje.tipoRemitente == "Operator"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                if (isOperator) Color(0xFFE1F5FE) else Color(0xFFC8E6C9),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = "By ${mensaje.remitente} on ${SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault()).format(mensaje.fecha)}   ${mensaje.tipoRemitente}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = mensaje.contenido,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}