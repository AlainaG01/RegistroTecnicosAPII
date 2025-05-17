package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import edu.ucne.registrotecnicos.data.repository.TecnicosRepository
import edu.ucne.registrotecnicos.data.repository.TicketsRepository
import edu.ucne.registrotecnicos.presentation.navigation.HomeNavHost
import edu.ucne.registrotecnicos.presentation.navigation.PrioridadesNavHost
import edu.ucne.registrotecnicos.presentation.navigation.TecnicosNavHost
import edu.ucne.registrotecnicos.presentation.navigation.TicketsNavHost
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketsViewModel
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var tecnicosRepository: TecnicosRepository
    private lateinit var tecnicosViewModel: TecnicosViewModel

    private lateinit var prioridadesRepository: PrioridadesRepository
    private lateinit var prioridadViewModel: PrioridadesViewModel

    private lateinit var ticketsRepository: TicketsRepository
    private lateinit var ticketsViewModel: TicketsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

        tecnicosRepository = TecnicosRepository(tecnicoDb.TecnicoDao())
        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)

        prioridadesRepository = PrioridadesRepository(tecnicoDb.PrioridadDao())
        prioridadViewModel = PrioridadesViewModel(prioridadesRepository)

        ticketsRepository = TicketsRepository(tecnicoDb.TicketDao())
        ticketsViewModel = TicketsViewModel(
            ticketsRepository,
            tecnicosRepository,
            prioridadesRepository
        )

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current

            val ticketList by tecnicoDb.TicketDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )

            RegistroTecnicosTheme {
                val nav = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        HomeNavHost(
                            navHostController = nav,
                            prioridadesViewModel = prioridadViewModel,
                            tecnicosViewModel = tecnicosViewModel,
                            ticketsViewModel = ticketsViewModel
                        )
                    }
                }
            }
        }
    }
}

