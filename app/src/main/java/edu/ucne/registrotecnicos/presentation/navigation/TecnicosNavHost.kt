package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicosViewModel

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    tecnicoList: List<TecnicoEntity>,
    viewModel: TecnicosViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
            )
        }

        composable<Screen.Tecnico>{ backStack ->
            val tecnicoId  = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId,
                viewModel
            ) {
            }
        }
    }
}