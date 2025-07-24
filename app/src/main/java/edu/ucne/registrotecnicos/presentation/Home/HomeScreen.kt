package edu.ucne.registrotecnicos.presentation.Home

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.ucne.registrotecnicos.common.NotificationHandler
import edu.ucne.registrotecnicos.presentation.navigation.Screen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    context: Context
) {
    //notificaciones
    // Solo necesitamos pedir permiso en Android 13 (TIRAMISU) o superior
    val postNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        // En versiones anteriores, el permiso se concede implícitamente
        null
    }

    val notificationHandler = NotificationHandler(context)

    // Solicita el permiso cuando la pantalla se carga por primera vez
    LaunchedEffect(key1 = true) {
        if (postNotificationPermission != null &&
            !postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            notificationHandler.showSimpleNotification()
        }) {
            Text(text = "Mostrar Notificación")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        // Título
        Text(
            text = "Home",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Contenedor con sombra
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón 1 - Técnicos
                OutlinedButton(
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Registro Técnico",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                // Botón 2 - Prioridades
                OutlinedButton(
                    onClick = { navController.navigate(Screen.PrioridadList) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Registro Prioridades",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                // Botón 3 - Tickets
                OutlinedButton(
                    onClick = { navController.navigate(Screen.TicketList) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Registro Tickets",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                // Botón 4 - Usuarios
                OutlinedButton(
                    onClick = { navController.navigate(Screen.UsuarioList) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Registro Usuarios",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                // Botón 5 - Vehiculos
                OutlinedButton(
                    onClick = { navController.navigate(Screen.VehiculoList) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Registro Vehiculos",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}