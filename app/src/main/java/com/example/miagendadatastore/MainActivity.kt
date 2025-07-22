package com.example.miagendadatastore

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaVista()
        }
    }
}

@Composable
fun AgendaVista() {
    val context = LocalContext.current
    val agenda = remember { Agenda(context) }

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contactos by remember { mutableStateOf(agenda.contactos.toList()) }

    fun actualizarLista() {
        contactos = agenda.contactos.toList()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            placeholder = { Text("Escribe tu nombre:") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            placeholder = { Text("Escribe tu teléfono:") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (nombre.isNotBlank() && telefono.isNotBlank()) {
                agenda.agregarContacto(Contacto(nombre, telefono.toInt()))
                nombre = ""
                telefono = ""
                actualizarLista()
                //Toast.makeText(context, "¡Contacto agregado correctamente!", Toast.LENGTH_SHORT).show()

            }
        }) {
            Text("Guardar")
        }


        HorizontalDivider(thickness = 2.dp, color = Color.Black)

        Text("Lista de Contactos")

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contactos) { contacto ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Text(text = contacto.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = "${contacto.telefono}")

                    Row {
                        IconButton(onClick = {
                            nombre = contacto.nombre
                            telefono = contacto.telefono.toString()
                            agenda.borrarContacto(contacto.nombre)
                            actualizarLista()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_mode_edit_24),
                                contentDescription = "Eliminar"
                            )
                        }

                        IconButton(onClick = {
                            agenda.borrarContacto(contacto.nombre)
                            actualizarLista()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Editar"
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 2.dp, color = Color.Black)
                }
            }
        }
    }
}

