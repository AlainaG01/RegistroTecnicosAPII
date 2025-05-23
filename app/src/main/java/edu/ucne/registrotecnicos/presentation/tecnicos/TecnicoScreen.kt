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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.presentation.tickets.TicketEvent
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@Composable
fun TecnicoScreen(
    tecnicoId: Int?,
    viewModel: TecnicosViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(tecnicoId) {
        println("id $tecnicoId")
        tecnicoId?.let {
            if (it > 0){
                viewModel.selectedTecnico(it)
            }
        }
    }

    TecnicoBodyScreen(
        uiState = uiState,
        viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoBodyScreen(
    uiState: TecnicoUiState,
    onEvent: (TecnicoEvent) -> Unit,
    goBack: () -> Unit
){
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
                IconButton(
                    onClick = goBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "volver")
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
                    Text("Registro de tecnicos ")


                    OutlinedTextField(
                        value = uiState.tecnicoId?.toString() ?: "Nuevo",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = uiState.nombre ?: "",
                        onValueChange = { onEvent(TecnicoEvent.NombreChange(it))},
                        label = { Text("Nombre del técnico") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    OutlinedTextField(
                        value = uiState.sueldo.toString(),
                        onValueChange = { onEvent(TecnicoEvent.SueldoChange(it.toDouble()))},
                        label = { Text("Sueldo del tecnico") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Blue,
                            unfocusedTextColor = Color.Gray,
                            focusedLabelColor = Color.Blue
                        )
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    uiState.errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(TecnicoEvent.New)
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
                                onEvent(TecnicoEvent.Save)
                                goBack()
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