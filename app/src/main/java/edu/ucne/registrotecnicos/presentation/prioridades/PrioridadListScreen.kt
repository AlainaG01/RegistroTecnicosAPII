package edu.ucne.registrotecnicos.presentation.prioridades

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
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity

@Composable
fun PrioridadListScreen(
    viewModel: PrioridadesViewModel = hiltViewModel(),
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: ((PrioridadEntity) -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadListBodyScreen(
        uiState = uiState,
        goToPrioridad = goToPrioridad,
        createPrioridad = createPrioridad,
        deletePrioridad = { prioridad ->
            viewModel.onEvent(PrioridadEvent.PrioridadChange(prioridad.prioridadId ?: 0))
            viewModel.onEvent(PrioridadEvent.Delete)
        }
    )
}

@Composable
private fun PrioridadRow(
    it: PrioridadEntity,
    goToPrioridad: () -> Unit,
    deletePrioridad: (PrioridadEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = it.prioridadId.toString(), color = Color.Black)
        Text(
            modifier = Modifier.weight(2f),
            text = it.descripcion,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(modifier = Modifier.weight(2f), text = it.tiempo.toString(), color = Color.Black)
        IconButton(onClick = goToPrioridad) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = {deletePrioridad(it)}) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }

    }
    HorizontalDivider()
}

@Composable
fun PrioridadListBodyScreen(
    uiState: PrioridadUiState,
    goToPrioridad: (Int) -> Unit,
    createPrioridad: () -> Unit,
    deletePrioridad: (PrioridadEntity) -> Unit
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                title = { Text("Lista de Prioridades") })
//        },
        floatingActionButton = {
            FloatingActionButton(onClick = createPrioridad) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(uiState.prioridades){ prioridad ->
                    PrioridadRow(
                        it = prioridad,
                        goToPrioridad = {
                            goToPrioridad(prioridad.prioridadId ?: 0)
                        },
                        deletePrioridad = deletePrioridad
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    val prioridad = listOf(
//        PrioridadEntity(
//            prioridadId = 1,
//            descripcion = "Baja",
//            tiempo = 1
//        ),
//        PrioridadEntity(
//            prioridadId = 2,
//            descripcion = "Media",
//            tiempo = 2
//        )
//    )
//    RegistroTecnicosTheme {
//        PrioridadListScreen(
//            prioridadList = prioridad,
//            onEdit = {},
//            onDelete = {}
//        )
//    }
//}