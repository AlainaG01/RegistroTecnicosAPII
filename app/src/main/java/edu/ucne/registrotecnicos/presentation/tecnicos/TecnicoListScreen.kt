package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

@Composable
fun TecnicoListScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit,
    deleteTecnico: ((TecnicoEntity) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoListBodyScreen(
        uiState = uiState,
        /*goToTecnico = { id ->
            // Primero establecemos el técnico seleccionado en el ViewModel
            viewModel.onEvent(TecnicoEvent.TecnicoChange(id))
            // Luego navegamos
            goToTecnico(id)
        },*/
        goToTecnico = goToTecnico,
        createTecnico = createTecnico,
        deleteTecnico = { tecnico ->
            viewModel.onEvent(TecnicoEvent.TecnicoChange(tecnico.tecnicoId ?: 0))
            viewModel.onEvent(TecnicoEvent.Delete)
        }
    )
}

@Composable
private fun TecnicoRow(
    it: TecnicoEntity,
    goToTecnico: () -> Unit,
    deleteTecnico:(TecnicoEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = it.tecnicoId.toString(), color = Color.Black)
        Text(
            modifier = Modifier.weight(2f),
            text = it.nombre,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(modifier = Modifier.weight(2f), text = it.sueldo.toString(), color = Color.Black)
        IconButton(onClick = goToTecnico) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = {deleteTecnico(it)}) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }

    }
    HorizontalDivider()
}

@Composable
fun TecnicoListBodyScreen(
    uiState: TecnicoUiState,
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit,
    deleteTecnico: (TecnicoEntity) -> Unit
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTecnico
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create a new Ticket"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(uiState.tecnicos) { tecnico ->
                    TecnicoRow(
                        it = tecnico,
                        goToTecnico = { goToTecnico(tecnico.tecnicoId ?: 0) },
                        deleteTecnico = deleteTecnico
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