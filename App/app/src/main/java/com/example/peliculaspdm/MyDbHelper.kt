package com.example.peliculaspdm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.peliculaspdm.PeliculaContract.PeliculaEntry

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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