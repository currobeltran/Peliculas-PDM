package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SeleccionGenero : AppCompatActivity() {
    var epoca: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_genero)
        epoca = intent.getStringExtra("Epoca")!!
    }

    fun volverAtras(view: View){
        val intentInicioNoAsistente = Intent(this, InicioNoAsistente::class.java)

        startActivity(intentInicioNoAsistente)
    }
}