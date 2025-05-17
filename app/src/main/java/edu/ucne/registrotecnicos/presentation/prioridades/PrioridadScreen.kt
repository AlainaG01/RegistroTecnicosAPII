package edu.ucne.registrotecnicos.presentation.prioridades


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity


@Composable
fun PrioridadScreen(
    prioridadId: Int? = null,
    viewModelPrioridad: PrioridadesViewModel,
    navController: NavController,
    function: () -> Unit
){
    var descripcion: String by remember { mutableStateOf("") }
    var tiempo: Int by remember { mutableStateOf(0) }
    var errorMessage: String? by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<PrioridadEntity?>(null) }

    LaunchedEffect(prioridadId) {
        if (prioridadId != null && prioridadId > 0){
            val prioridad = viewModelPrioridad.findPrioridad(prioridadId)
            prioridad?.let {
                editando = it
                descripcion = it.descripcion
                tiempo = it.tiempo
            }
        }
    }
    Scaffold { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                if (navController != null){
                    IconButton(
                        onClick = {navController.popBackStack()},
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                    }
                }
            }
            ElevatedCard (
                modifier = Modifier.fillMaxWidth()
            ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ){
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Registro de prioridades $prioridadId")

                    OutlinedTextField(
                        value = editando?.prioridadId?.toString() ?: "0",
                        onValueChange = {},
                        label = {Text("ID Prioridad")},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {descripcion = it},
                        label = {Text("Descripcion de la prioridad")},
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedTextColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )
                    OutlinedTextField(
                        value = tiempo.toString(),
                        onValueChange = {newValue ->
                            tiempo = newValue.toIntOrNull() ?: 0
                        },
                        label = {Text("Tiempo de la prioridad")},
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedTextColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        OutlinedButton(
                            onClick = {
                                if (descripcion.isBlank()){
                                    errorMessage = "La descripcion no puede estar vacia."
                                    return@OutlinedButton
                                }
                                if (tiempo <= 0){
                                    errorMessage = "Debe asignar un tiempo a la prioridad."
                                    return@OutlinedButton
                                }

                                viewModelPrioridad.savePrioridad(
                                    PrioridadEntity(
                                        prioridadId = editando?.prioridadId,
                                        descripcion = descripcion,
                                        tiempo = tiempo
                                    )
                                )
                                descripcion = ""
                                tiempo = 0
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