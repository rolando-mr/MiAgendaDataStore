package com.example.miagendadatastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "agenda_prefs")
val CONTACTOS_KEY = stringPreferencesKey("contactos")

class Agenda(private val context: Context) {

    private val gson = Gson()
    var contactos = mutableListOf<Contacto>()

    init {
        cargarContactos()
    }

    fun agregarContacto(contacto: Contacto) {
        contactos.add(contacto)
        guardarContactos()
    }

    fun borrarContacto(nombre: String): Boolean {
        val contacto = contactos.find { it.nombre.equals(nombre, ignoreCase = true) }
        val eliminado = contacto?.let { contactos.remove(it) } ?: false
        if (eliminado) guardarContactos()
        return eliminado
    }

    fun editarContacto(nombreAntiguo: String, nuevoContacto: Contacto): Boolean {
        val index = contactos.indexOfFirst { it.nombre.equals(nombreAntiguo, ignoreCase = true) }
        if (index >= 0) {
            contactos[index] = nuevoContacto
            guardarContactos()
            return true
        }
        return false
    }

    private fun guardarContactos() {
        val json = gson.toJson(contactos)
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[CONTACTOS_KEY] = json
            }
        }
    }

    private fun cargarContactos() {
        runBlocking {
            val preferences = context.dataStore.data.first()
            val json = preferences[CONTACTOS_KEY]
            json?.let {
                val type = object : TypeToken<MutableList<Contacto>>() {}.type
                contactos = gson.fromJson(json, type)
            }
        }
    }
}
