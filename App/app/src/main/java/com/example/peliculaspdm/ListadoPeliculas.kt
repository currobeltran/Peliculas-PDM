package com.example.peliculaspdm

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ListadoPeliculas : AppCompatActivity() {
    var peliculasMeGusta: String? = null
    var peliculasNoMeGusta: String? = null
    var peliculasPorVer: String? = null
    var idSesion: String? = null
    var idCuenta: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_peliculas)
        idSesion = intent.getStringExtra("IDSESION")
        idCuenta = intent.getStringExtra("IDCUENTA")

        peticionWatchlist()
        while (peliculasPorVer == null){

        }

        var vectorPeliculasPorVer = JSONObject(peliculasPorVer!!).getJSONArray("results")
        var scrollPelis = findViewById<LinearLayout>(R.id.scrollpeliculas)

        for(i in 0 until vectorPeliculasPorVer.length()){
            var layoutPeliculaUnidad = LinearLayout(this)
            layoutPeliculaUnidad.orientation = LinearLayout.HORIZONTAL

            var tituloPeli = TextView(this)
            tituloPeli.text = vectorPeliculasPorVer.getJSONObject(i).getString("title")
            tituloPeli.textSize = 16.0f
            tituloPeli.setTextColor(getColor(R.color.black))
            layoutPeliculaUnidad.addView(tituloPeli)

            var accionRealizada = TextView(this)
            accionRealizada.text = getString(R.string.listadeseguimiento)
            accionRealizada.textSize = 16.0f
            accionRealizada.setTextColor(getColor(R.color.black))
            layoutPeliculaUnidad.addView(accionRealizada)

            var botonValorarPeli = Button(this)
            botonValorarPeli.setText(R.string.valorarpeli)
            botonValorarPeli.setBackgroundColor(getColor(R.color.teal_700))
            layoutPeliculaUnidad.addView(botonValorarPeli)

            scrollPelis.addView(layoutPeliculaUnidad)
        }
    }

    fun peticionWatchlist(){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/account/$idCuenta/watchlist/movies?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idSesion")
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
                        peliculasPorVer = response.body()!!.string()
                        Log.i("PELIS", peliculasPorVer!!)
                    }
                })
    }

    fun peticionPeliculasMeGusta(){

    }

    fun peticionPeliculasNoMeGusta(){

    }
}