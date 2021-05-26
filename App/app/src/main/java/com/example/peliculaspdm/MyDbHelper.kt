package com.example.peliculaspdm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.peliculaspdm.PeliculaContract.PeliculaEntry

/**
 * Clase que nos servirá para interactuar con la base de datos
 * local que pueden poseer todas las aplicaciones Android.
 */
class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    /**
     * Si no existe la base de datos, se crea con esta información
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + PeliculaEntry.TABLE_NAME + " (" +
                PeliculaEntry.COLUMN_ID + " VARCHAR(30) PRIMARY KEY," +
                PeliculaEntry.COLUMN_NAME + " TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PeliculaEntry.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "pym.db"
    }
}