package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 * Esta actividad funciona básicamente igual que la de
 * valorar película, sin embargo aquí en lugar de esto, se
 * puede añadir la película a la lista de seguimiento.
 */
class InfoPeliculaListaSeguir : AppCompatActivity() {
    var pelicula: JSONObject = JSONObject()
    var imagen: String = ""
    var idSesion: String = ""
    var idCuenta: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_pelicula_lista_seguir)

        val stringPeli = intent.getStringExtra("PELICULA")!!
        pelicula = JSONObject(stringPeli)

        imagen = intent.getStringExtra("IMAGEN")!!
        val posterPelicula = findViewById<ImageView>(R.id.posterPeli3)
        val enlaceImagen = "https://image.tmdb.org/t/p/w500$imagen"
        Picasso.with(this).load(enlaceImagen).into(posterPelicula)

        val titulo = findViewById<TextView>(R.id.titulo3)
        titulo.text = pelicula.getString("title")

        val sinopsis = findViewById<TextView>(R.id.sinopsis3)
        sinopsis.text = pelicula.getString("overview")

        idSesion = intent.getStringExtra("IDSESION")!!

        idCuenta = intent.getIntExtra("IDCUENTA", -1)
    }

    fun volverAtras(view: View){
        val activityInicio = Intent(this, MainActivity::class.java)
        activityInicio.putExtra("IDCUENTA", idCuenta)
        activityInicio.putExtra("IDSESION", idSesion)
        val requestToken = intent.getStringExtra("TOKEN")
        activityInicio.putExtra("RequestToken", requestToken)

        startActivity(activityInicio)
    }

    fun anadirListaSeguimiento(v: View){
        //Incluir película en peliculas que le gustan al usuario
        var idPeli = pelicula.getString("id")
        val requestBody = FormBody.Builder()
            .add("media_type", "movie")
            .add("media_id", idPeli)
            .add("watchlist", "true")
            .build()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/account/$idCuenta/watchlist?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idSesion")
            .method("POST",requestBody)
            .build()
        val cliente = OkHttpClient()

        cliente.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    // Error
                    runOnUiThread {
                        // For the example, you can show an error dialog or a toast
                        // on the main UI thread
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    val res = JSONObject(response.body()!!.string())
                    Log.i("STATUS", res.getString("status_message"))
                }
            })
    }
}