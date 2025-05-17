package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadesViewModel
import java.util.Date

@Composable
fun TicketScreen(
    ticketId: Int? = null,
    viewModel: TicketsViewModel,
    navController: NavController,
    function: () -> Unit
) {
    var fecha: Date by remember { mutableStateOf(Date()) }
    var prioridadId: Int by remember { mutableStateOf(0) }
    var cliente: String by remember { mutableStateOf("") }
    var asunto: String by remember { mutableStateOf("") }
    var descripcion: String by remember { mutableStateOf("") }
    var tecnicoId: Int by remember { mutableStateOf(0) }
    var errorMessage: String? by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<TicketEntity?>(null) }

    // Estados para los dropdowns
    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    val prioridades by viewModel.ListaPrioridades.collectAsState()
    val tecnicos by viewModel.ListaTecnicos.collectAsState()


    LaunchedEffect(ticketId) {
        if (ticketId != null && ticketId > 0) {
            val ticket = viewModel.findTicket(ticketId)
            ticket?.let {
                editando = it
                fecha = it.fecha
                prioridadId = it.prioridadId
                cliente = it.cliente
                asunto = it.asunto
                descripcion = it.descripcion
                tecnicoId = it.tecnicoId
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null){
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                    }
                }
            }
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Registro de tickets $ticketId")

                    OutlinedTextField(
                        value = editando?.ticketId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Ticket") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    //prioridad
                    Spacer(modifier = Modifier.height(8.dp))

                    // Selector de Prioridad
                   /* Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = prioridades.find { it.prioridadId == prioridadId }?.descripcion ?: "Seleccione técnico",
                            onValueChange = {},
                            label = { Text("Prioridad") },
                            modifier = Modifier.fillMaxWidth(),
                            //readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )
                        DropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.descripcion) },
                                    onClick = {
                                        prioridadId = prioridad.prioridadId ?: 0
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }*/
                    //prueba
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = prioridades.find { it.prioridadId == prioridadId }?.descripcion ?: "Seleccione una prioridad",
                            onValueChange = {},
                            label = { Text("Prioridad") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedPrioridad = true }, // <- Habilitar el dropdown
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )
                        DropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.descripcion) },
                                    onClick = {
                                        prioridadId = prioridad.prioridadId!!
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }


                    OutlinedTextField(
                        value = cliente,
                        onValueChange = { cliente = it },
                        label = { Text("Nombre del cliente") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    OutlinedTextField(
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripcion") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    //tecnico
                    Spacer(modifier = Modifier.height(8.dp))

                   /* Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = tecnicos.find { it.tecnicoId == tecnicoId }?.nombre ?: "Seleccione técnico",
                            onValueChange = {},
                            label = { Text("Técnico Asignado") },
                            modifier = Modifier.fillMaxWidth(),
                            //readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )
                        DropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = {
                                        Text(tecnico.nombre)
                                    },
                                    onClick = {
                                        tecnicoId = tecnico.tecnicoId ?: 0
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }*/

                    //prueba
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = tecnicos.find { it.tecnicoId == tecnicoId }?.nombre ?: "Seleccione un técnico",
                            onValueChange = {},
                            label = { Text("Técnico Asignado") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedTecnico = true }, // <- Habilitar el dropdown
                            readOnly = true,
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )
                        DropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = { Text(tecnico.nombre) },
                                    onClick = {
                                        tecnicoId = tecnico.tecnicoId!!
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {

                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Blue
                            ),
                            border = BorderStroke(1.dp, Color.Blue),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text("Nuevo")
                        }
                        OutlinedButton(
                            onClick = {
                                if (prioridadId <= 0) {
                                    errorMessage = "Seleccione una prioridad"
                                    return@OutlinedButton
                                }
                                if (cliente.isBlank()) {
                                    errorMessage = "El nombre del cliente no puede estar vacio."
                                    return@OutlinedButton
                                }
                                if (asunto.isBlank()) {
                                    errorMessage = "Debe tener un asunto."
                                    return@OutlinedButton
                                }
                                if (descripcion.isBlank()) {
                                    errorMessage = "Debe tener una descripcion."
                                    return@OutlinedButton
                                }
                                if (tecnicoId <= 0) {
                                    errorMessage = "Seleccione un tecnico"
                                    return@OutlinedButton
                                }
                                //crear
                                viewModel.saveTicket(
                                    TicketEntity(
                                        ticketId = editando?.ticketId,
                                        fecha = fecha,
                                        prioridadId = prioridadId,
                                        cliente = cliente,
                                        asunto = asunto,
                                        descripcion = descripcion,
                                        tecnicoId = tecnicoId
                                    )
                                )
                                errorMessage = null
                                editando = null

                                navController.navigateUp()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Blue
                            ),
                            border = BorderStroke(1.dp, Color.Blue),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}