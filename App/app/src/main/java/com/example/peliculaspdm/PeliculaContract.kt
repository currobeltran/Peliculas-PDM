package com.example.peliculaspdm

import android.provider.BaseColumns

/**
 * Clase que contiene constantes relacionadas
 * con la base de datos que mantendrá la aplicación
 * en relación con las películas que no le han
 * gustado al usuario.
 */
class PeliculaContract {

    private fun PeliculaContract() {}

    object PeliculaEntry : BaseColumns {
        const val TABLE_NAME = "peliculas"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}