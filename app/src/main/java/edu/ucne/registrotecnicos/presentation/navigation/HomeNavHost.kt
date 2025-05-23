package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.Home.HomeScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketsViewModel

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    prioridadesViewModel: PrioridadesViewModel,
    tecnicosViewModel: TecnicosViewModel,
    ticketsViewModel: TicketsViewModel
){
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        //inicio
        composable <Screen.Home> {
            HomeScreen(navController = navHostController)
        }

        //pantalla lista de prioridades
        composable <Screen.PrioridadList> {

            PrioridadListScreen(
                goToPrioridad = { id ->
                    navHostController.navigate(Screen.Prioridad(id ?: 0))
                },
                createPrioridad = {
                    navHostController.navigate(Screen.Prioridad(0))
                }
            )

        }

        //pantalla formulario de prioridades
        composable <Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridadId
            PrioridadScreen(
                prioridadId = prioridadId,
                //viewModel = prioridadesViewModel,
                goBack = { navHostController.popBackStack() }
            )
        }

        //pantalla lista de tecnicos
        composable<Screen.TecnicoList>{

            TecnicoListScreen(
                goToTecnico = { id ->
                    navHostController.navigate(Screen.Tecnico(id ?: 0))
                },
                createTecnico = {
                    navHostController.navigate((Screen.Tecnico(0)))
                }
            )
        }

        //pantalla formulario de tecnico
        composable <Screen.Tecnico>{ backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId = tecnicoId,
                //viewModel = tecnicosViewModel,
                goBack = { navHostController.popBackStack() }
            )
        }

        //pantalla lista de tickets
        composable <Screen.TicketList>{

            TicketListScreen(
                goToTicket = { id ->
                    navHostController.navigate(Screen.Ticket(id ?: 0))
                },
                createTicket = {
                    navHostController.navigate((Screen.Ticket(0)))
                }
            )
        }

        //pantalla formulario tickets
        composable <Screen.Ticket>{ backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                //viewModel = ticketsViewModel,
                goBack = { navHostController.popBackStack()}
            )
        }
    }
}