package edu.ucne.registrotecnicos.presentation.tecnicos

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@Composable
fun TecnicoScreen(
    tecnicoId: Int? = null,
    viewModel: TecnicosViewModel,
    navController: NavController,
    function: () -> Unit
) {
    var nombre: String by remember { mutableStateOf("") }
    var sueldo: Double by remember { mutableStateOf(0.0) }
    var errorMessage: String? by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<TecnicoEntity?>(null) }

    LaunchedEffect(tecnicoId) {
        if (tecnicoId != null && tecnicoId > 0) {
            val tecnico = viewModel.findTecnico(tecnicoId)
            tecnico?.let {
                editando = it
                nombre = it.nombre
                sueldo = it.sueldo
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
                    Text("Registro de tecnicos $tecnicoId")

                    OutlinedTextField(
                        value = editando?.tecnicoId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Técnico") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del técnico") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    OutlinedTextField(
                        value = sueldo.toString(),
                        onValueChange = { newValue ->
                            sueldo = newValue.toDoubleOrNull() ?: 0.0
                        },
                        label = { Text("Sueldo del tecnico") },
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
                                if (nombre.isBlank()) {
                                    errorMessage = "El nombre no puede estar vacio."
                                    return@OutlinedButton
                                }

                                if (sueldo <= 0.0) {
                                    errorMessage = "El sueldo no puede ser cero o menor."
                                    return@OutlinedButton
                                }
                                //crear
                                viewModel.saveTecnico(
                                    TecnicoEntity(
                                        tecnicoId = editando?.tecnicoId,
                                        nombre = nombre,
                                        sueldo = sueldo
                                    )
                                )
                                nombre = ""
                                sueldo = 0.0
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

/*@Preview(showBackground = true)
@Composable
private fun TecnicoPreview() {
    val navController = rememberNavController()
    val viewModel = TecnicosViewModel(
        tecnicosRepository = TODO()
    )
    RegistroTecnicosTheme {
        TecnicoScreen(
            tecnicoId = null,
            viewModel = viewModel,
            navController = navController
        ) {}
    }
}
*/