package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

class ObtenerRecomendacion : AppCompatActivity() {
    var genero: String = ""
    var epoca: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obtener_recomendacion)
        val radioGroupEpocas = findViewById<RadioGroup>(R.id.epocas)
        val radioGroupGeneros = findViewById<RadioGroup>(R.id.generos)

        radioGroupEpocas.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)

            epoca = radioButton.text as String
            Log.i("EPOCA", epoca)
        }

        radioGroupGeneros.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)

            genero = radioButton.text as String
            Log.i("GENERO", genero)
        }
    }

    fun volverAtras(view: View){
        val intentInicio = Intent(this, MainActivity::class.java)

        startActivity(intentInicio)
    }

    fun verPeliculasRelacionadas(view: View){
        val intentPeliculasRelacionadas = Intent(this, PeliculasRelacionadas::class.java)

        val radioGroupEpocas = findViewById<RadioGroup>(R.id.epocas)
        val radioGroupGeneros = findViewById<RadioGroup>(R.id.generos)

        if(radioGroupEpocas.checkedRadioButtonId != -1 && radioGroupGeneros.checkedRadioButtonId != -1){
            intentPeliculasRelacionadas.putExtra("Epoca", epoca)
            intentPeliculasRelacionadas.putExtra("Genero", genero)
            startActivity(intentPeliculasRelacionadas)
        }
    }
}