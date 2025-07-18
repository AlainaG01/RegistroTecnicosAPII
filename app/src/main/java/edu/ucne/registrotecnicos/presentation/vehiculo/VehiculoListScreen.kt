package edu.ucne.registrotecnicos.presentation.vehiculo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnicos.data.remote.dto.VehiculoDto

@Composable
fun VehiculoListScreen(
    viewModel: VehiculoViewModel = hiltViewModel(),
    createVehiculo: () -> Unit,
    goToVehiculo: (Int) -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    VehiculoListBodyScreen(
        uiState = uiState,
        goToVehiculo = { id -> goToVehiculo(id) },
        onEvent = viewModel::OnEvent,
        createVehiculo = createVehiculo,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun VehiculoListBodyScreen(
    uiState: VehiculoUiState,
    goToVehiculo: (Int) -> Unit,
    onEvent: (VehiculoEvent) -> Unit,
    createVehiculo: () -> Unit,
    goBack: () -> Unit
) {
    val refreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { onEvent(VehiculoEvent.GetVehiculo) }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Vehículos",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF272D4D)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createVehiculo) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar nuevo")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.vehiculos.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay vehículos registrados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        items(uiState.vehiculos) { vehiculo ->
                            VehiculoRow(
                                vehiculo = vehiculo,
                                goToVehiculo = { goToVehiculo(vehiculo.vehiculoId ?: 0) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                if (!uiState.errorMessage.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun VehiculoRow(
    vehiculo: VehiculoDto,
    goToVehiculo: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = vehiculo.vehiculoId.toString(),
            modifier = Modifier.weight(1f),
            color = Color.Black
        )
        Text(
            text = vehiculo.descripcion,
            modifier = Modifier.weight(3f),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(
            text = "$${vehiculo.precio}",
            modifier = Modifier.weight(2f),
            color = Color.Black
        )
    }
    HorizontalDivider()
}
