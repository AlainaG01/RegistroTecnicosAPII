package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()
        setContent {
            RegistroTecnicosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ){
                        TecnicoScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun TecnicoScreen(
    ){

        var nombre:String by remember { mutableStateOf("") }
        var sueldo: Double by remember { mutableStateOf(0.0) }
        var errorMessage:String? by remember { mutableStateOf("") }

        Scaffold { innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ){
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ){
                        OutlinedTextField(
                            value = "0",
                            onValueChange = {},
                            label = { Text("ID Técnico") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false
                        )

                        OutlinedTextField(
                            label = { Text(text = "Nombre del tecnico") },
                            value = nombre,
                            onValueChange = {nombre = it},
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            label = { Text(text = "Sueldo") },
                            value = sueldo.toString(),
                            onValueChange = { newValue ->
                                sueldo = newValue.toDoubleOrNull() ?: 0.0
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.padding(2.dp))
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                        ){
                            OutlinedButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "new button"
                                )
                                Text(text = "Nuevo")
                            }
                            val scope = rememberCoroutineScope()
                            OutlinedButton(
                                onClick = {
                                    if(nombre.isBlank())
                                        errorMessage = "El nombre no puede estar vacio."

                                    scope.launch {
                                        saveTecnico(
                                            TecnicoEntity(
                                                nombre = nombre,
                                                sueldo = sueldo
                                            )
                                        )
                                        nombre = ""
                                        sueldo = 0.0
                                    }
                                }
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

                val lifecycleOwner = LocalLifecycleOwner.current
                val tecnicoList by tecnicoDb.TecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                TecnicoListScreen(tecnicoList)
            }
        }
    }

    @Composable
    fun TecnicoListScreen(tecnicoList: List<TecnicoEntity>) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de tecnicos")

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tecnicoList) {
                    TecnicoRow(it)
                }
            }
        }
    }
    @Composable
    private fun TecnicoRow(it: TecnicoEntity) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(1f), text = it.tecnicoId.toString())
            Text(
                modifier = Modifier.weight(2f),
                text = it.nombre,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(modifier = Modifier.weight(2f), text = it.sueldo.toString())
        }
        HorizontalDivider()
    }

    private suspend fun saveTecnico(tecnico: TecnicoEntity){
        tecnicoDb.TecnicoDao().save(tecnico)
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        RegistroTecnicosTheme {
            TecnicoScreen()
        }
    }
}

