package com.example.peliculaspdm

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun noAsistente(view: View){
        val intentNoAsistente = Intent(this, InicioNoAsistente::class.java)

        startActivity(intentNoAsistente)
    }

    fun siAsistente(view: View){


    }
}