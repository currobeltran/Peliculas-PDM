package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONObject

/**
 * Esta actividad funciona básicamente igual que la de
 * valorar película, sin embargo aquí no existe la posibilidad
 * de valorarla, ya que no aparecen los botones destinados a
 * dicha funcionalidad.
 */
class InfoPelicula : AppCompatActivity() {
    var pelicula: JSONObject = JSONObject()
    var imagen: String = ""
    var idSesion: String = ""
    var idCuenta: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_pelicula)

        val stringPeli = intent.getStringExtra("PELICULA")!!
        pelicula = JSONObject(stringPeli)

        imagen = intent.getStringExtra("IMAGEN")!!
        val posterPelicula = findViewById<ImageView>(R.id.posterPeli2)
        val enlaceImagen = "https://image.tmdb.org/t/p/w500$imagen"
        Picasso.with(this).load(enlaceImagen).into(posterPelicula)

        val titulo = findViewById<TextView>(R.id.titulo2)
        titulo.text = pelicula.getString("title")

        val sinopsis = findViewById<TextView>(R.id.sinopsis2)
        sinopsis.text = pelicula.getString("overview")

        idSesion = intent.getStringExtra("IDSESION")!!

        idCuenta = intent.getIntExtra("IDCUENTA", -1)
    }

    fun volverAtras(view: View){
        val activityInicio = Intent(this, ListadoPeliculas::class.java)
        activityInicio.putExtra("IDCUENTA", idCuenta)
        activityInicio.putExtra("IDSESION", idSesion)
        val requestToken = intent.getStringExtra("TOKEN")
        activityInicio.putExtra("RequestToken", requestToken)

        startActivity(activityInicio)
    }
}