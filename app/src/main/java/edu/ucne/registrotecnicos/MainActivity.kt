package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
        var editando by remember { mutableStateOf<TecnicoEntity?>(null) }
        val scope = rememberCoroutineScope()

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
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("Registro de tecnicos")

                        OutlinedTextField(
                            value = "0",
                            onValueChange = {},
                            label = { Text("ID Técnico") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false
                        )
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del técnico") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )

                        OutlinedTextField(
                            value = sueldo.toString(),
                            onValueChange = { newValue ->
                                sueldo = newValue.toDoubleOrNull() ?: 0.0
                            },
                            label = {Text("Sueldo del tecnico")},
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Blue,
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color.Blue
                            )
                        )

                        Spacer(modifier = Modifier.padding(2.dp))
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            OutlinedButton(
                                onClick = {

                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Blue
                                ),
                                border = BorderStroke(1.dp, Color.Blue),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add,
                                    contentDescription = "new button")
                                Text("Nuevo")
                            }

                            val scope = rememberCoroutineScope()

                            OutlinedButton(
                                onClick = {
                                    if(nombre.isBlank()) {
                                        errorMessage = "El nombre no puede estar vacio."
                                        return@OutlinedButton
                                    }

                                    if(sueldo <= 0.0) {
                                        errorMessage = "El sueldo no puede ser cero o menor."
                                        return@OutlinedButton
                                    }

                                    scope.launch {
                                        saveTecnico(
                                            TecnicoEntity(
                                                tecnicoId = editando?.tecnicoId,
                                                nombre = nombre,
                                                sueldo = sueldo
                                            )
                                        )
                                        nombre = ""
                                        sueldo = 0.0
                                        errorMessage = null
                                        editando = null
                                    }
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

                val lifecycleOwner = LocalLifecycleOwner.current
                val tecnicoList by tecnicoDb.TecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                //TecnicoListScreen(tecnicoList)
                TecnicoListScreen(
                    tecnicoList = tecnicoList,
                    onEdit = { tecnico ->
                        nombre = tecnico.nombre
                        sueldo = tecnico.sueldo
                        editando = tecnico
                    },
                    onDelete = { tecnico ->
                        scope.launch {
                            tecnicoDb.TecnicoDao().delete(tecnico)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun TecnicoListScreen(
        tecnicoList: List<TecnicoEntity>,
        onEdit: (TecnicoEntity) -> Unit,
        onDelete: (TecnicoEntity) -> Unit
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lista de técnicos",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico, onEdit, onDelete)
                }
            }
        }
    }

   @Composable
    private fun TecnicoRow(
       tecnico: TecnicoEntity,
       onEdit: (TecnicoEntity) -> Unit,
       onDelete: (TecnicoEntity) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(modifier = Modifier.weight(1f), text = tecnico.tecnicoId.toString())
            Text(
                modifier = Modifier.weight(2f),
                text = tecnico.nombre,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(modifier = Modifier.weight(2f), text = tecnico.sueldo.toString())
            IconButton(onClick = { onEdit(tecnico) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = { onDelete(tecnico) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
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

