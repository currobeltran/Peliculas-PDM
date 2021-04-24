package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class InicioNoAsistente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_no_asistente)
    }

    fun volverAtras(view: View){
        val intentInicio = Intent(this, MainActivity::class.java)

        startActivity(intentInicio)
    }

    fun opcionElegida(view: View){
        val intentGenero = Intent(this, SeleccionGenero::class.java)

        if(view.id == R.id.classic){
            intentGenero.putExtra("Epoca", "Clasica")
        }
        else if(view.id == R.id.colouredclassic){
            intentGenero.putExtra("Epoca", "Clasico a color")
        }
        else if(view.id == R.id.actual){
            intentGenero.putExtra("Epoca", "Actual")
        }

        startActivity(intentGenero)
    }
}