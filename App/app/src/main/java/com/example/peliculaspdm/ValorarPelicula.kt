package com.example.peliculaspdm

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ValorarPelicula : AppCompatActivity() {
    var pelicula: JSONObject = JSONObject()
    var imagen: String = ""
    var idSesion: String = ""
    var idLista: Int = -1
    var idCuenta: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valorar_pelicula)

        val stringPeli = intent.getStringExtra("PELICULA")!!
        pelicula = JSONObject(stringPeli)

        imagen = intent.getStringExtra("IMAGEN")!!
        val posterPelicula = findViewById<ImageView>(R.id.posterPeli)
        val enlaceImagen = "https://image.tmdb.org/t/p/w500$imagen"
        Picasso.with(this).load(enlaceImagen).into(posterPelicula)

        val titulo = findViewById<TextView>(R.id.titulo)
        titulo.text = pelicula.getString("title")

        val sinopsis = findViewById<TextView>(R.id.sinopsis)
        sinopsis.text = pelicula.getString("overview")

        idSesion = intent.getStringExtra("IDSESION")!!

        idLista = intent.getIntExtra("IDLISTA", -1)

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

    fun valoracionPelicula(view: View){
        val idBoton = view.id

        if(idBoton == R.id.botonMG){
            anadePeliculaListaMeGusta()
        }
        else{
            anadePeliculaListaNoMeGusta()
        }
    }

    fun anadePeliculaListaMeGusta(){
        val idPeli = pelicula.getString("id")
        val requestBody = FormBody.Builder()
                .add("media_type", "movie")
                .add("media_id", idPeli)
                .add("favorite", "true")
                .build()

        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/account/$idCuenta/favorite?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idSesion")
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

    fun anadePeliculaListaNoMeGusta(){
        var dbHelper = MyDbHelper(this)
        var sqliteBD = dbHelper.writableDatabase

        var contenido = ContentValues().apply {
            put(PeliculaContract.PeliculaEntry.COLUMN_ID, pelicula.getString("id"))
            put(PeliculaContract.PeliculaEntry.COLUMN_NAME, pelicula.getString("title"))
        }

        var nuevaFila = sqliteBD.insert(PeliculaContract.PeliculaEntry.TABLE_NAME, null, contenido)
        Log.i("NO ME GUSTA", nuevaFila.toString())
    }
}