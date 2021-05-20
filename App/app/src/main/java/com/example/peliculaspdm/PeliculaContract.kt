package com.example.peliculaspdm

import android.provider.BaseColumns

class PeliculaContract {

    private fun PeliculaContract() {}

    object PeliculaEntry : BaseColumns {
        const val TABLE_NAME = "peliculas"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}