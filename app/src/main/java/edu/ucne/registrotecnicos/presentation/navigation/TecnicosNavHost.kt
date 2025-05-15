package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen

@Composable
fun TecnicosNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = emptyList(),
                onEdit = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
            )
        }

        composable<Screen.Tecnico>{ backStack ->
            val tecnicoId  = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(tecnicoId) {

            }
        }
    }
}