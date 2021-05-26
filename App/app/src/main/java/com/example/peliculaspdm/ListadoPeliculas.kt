package com.example.peliculaspdm

import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ListadoPeliculas : AppCompatActivity() {
    var peliculasMeGusta: String? = null
    var cursorPeliculasNoMeGusta: Cursor? = null
    var peliculasPorVer: String? = null
    var idSesion: String? = null
    var idCuenta: String? = null
    var imagenPeli: String? = null
    var detallePeli: String? = null

    /**
     * En la creación de la actividad se obtiene la información
     * de todas las listas que se podrían mostrar.
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_peliculas)
        idSesion = intent.getStringExtra("IDSESION")
        idCuenta = intent.getStringExtra("IDCUENTA")
        peticionPeliculasMeGusta()
        while(peliculasMeGusta == null){

        }
        peticionPeliculasNoMeGusta()
        peticionWatchlist()
        while(peliculasPorVer == null){

        }

        var vectorPeliculasMeGusta = JSONArray(peliculasMeGusta)
        var vectorPeliculasPorVer = JSONObject(peliculasPorVer).getJSONArray("results")

        for(i in 0 until vectorPeliculasMeGusta.length()){
            var j = 0
            do{
                if(vectorPeliculasMeGusta.getJSONObject(i).getString("title") == vectorPeliculasPorVer.getJSONObject(j).getString("title")){
                    vectorPeliculasPorVer.remove(j)
                }
                else{
                    j++
                }
            }while(j < vectorPeliculasPorVer.length())
        }

        if(cursorPeliculasNoMeGusta!!.moveToNext()){
            do{
                var j = 0

                do {
                    if(cursorPeliculasNoMeGusta!!.getString(1) == vectorPeliculasPorVer.getJSONObject(j).getString("title")){
                        vectorPeliculasPorVer.remove(j)
                    }
                    else{
                        j++
                    }
                } while (j < vectorPeliculasPorVer.length())

            }while (cursorPeliculasNoMeGusta!!.moveToNext())
        }

        peliculasPorVer = vectorPeliculasPorVer.toString()
    }

    /**
     * Función para cambiar la lista de películas mostrada en la actividad
     *
     * Al pulsar un determinado botón, dicha información cambia correspondiendo
     * con la lista que se desea visualizar. La lista se limpia y se construye
     * de nuevo al pulsar ese botón.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun cambioDeLista(v: View){
        var scrollPelis = findViewById<LinearLayout>(R.id.scrollpeliculas)
        scrollPelis.removeAllViews()

        when (v.id) {
            R.id.listaseguimientoboton -> {
                var vectorPeliculasPorVer = JSONArray(peliculasPorVer)

                for(i in 0 until vectorPeliculasPorVer.length()){
                    var layoutPeliculaUnidad = LinearLayout(this)
                    layoutPeliculaUnidad.orientation = LinearLayout.HORIZONTAL
                    val parametros_layout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    parametros_layout.setMargins(20,20,20,0)

                    var botonValorarPeli = Button(this)
                    botonValorarPeli.setOnClickListener {
                        imagenPeli = null
                        detallePeli = null
                        val idPeli = vectorPeliculasPorVer.getJSONObject(i).getInt("id")
                        //Obtener imagen
                        peticionImagen(idPeli)
                        while (imagenPeli == null){

                        }

                        //Obtener info de peli
                        peticionDetallesPelicula(idPeli)
                        while(detallePeli == null){

                        }

                        //Iniciar valorar peli
                        val intentValorarPeli = Intent(this, ValorarPelicula::class.java)

                        intentValorarPeli.putExtra("PELICULA", detallePeli)
                        intentValorarPeli.putExtra("IMAGEN", imagenPeli)

                        intentValorarPeli.putExtra("IDSESION", idSesion)
                        intentValorarPeli.putExtra("IDCUENTA", idCuenta)
                        intentValorarPeli.putExtra("TOKEN", intent.getStringExtra("TOKEN"))

                        startActivity(intentValorarPeli)
                    }
                    botonValorarPeli.setText(R.string.valorarpeli)
                    botonValorarPeli.textSize = 12.0f
                    botonValorarPeli.layoutParams = parametros_layout
                    layoutPeliculaUnidad.addView(botonValorarPeli)

                    var tituloPeli = TextView(this)
                    tituloPeli.text = vectorPeliculasPorVer.getJSONObject(i).getString("title")
                    tituloPeli.textSize = 16.0f
                    tituloPeli.setTextColor(getColor(R.color.black))
                    tituloPeli.layoutParams = parametros_layout
                    layoutPeliculaUnidad.addView(tituloPeli)

                    scrollPelis.addView(layoutPeliculaUnidad)
                }
            }
            R.id.listamegustaboton -> {
                val vectorPeliculasMeGusta = JSONArray(peliculasMeGusta)
                for(i in 0 until vectorPeliculasMeGusta.length()){
                    var layoutPeliculaUnidad = LinearLayout(this)
                    layoutPeliculaUnidad.orientation = LinearLayout.HORIZONTAL
                    val parametros_layout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    parametros_layout.setMargins(20,20,20,0)

                    var botonValorarPeli = Button(this)
                    botonValorarPeli.setOnClickListener {
                        imagenPeli = null
                        detallePeli = null
                        val idPeli = vectorPeliculasMeGusta.getJSONObject(i).getInt("id")
                        //Obtener imagen
                        peticionImagen(idPeli)
                        while (imagenPeli == null){

                        }

                        //Obtener info de peli
                        peticionDetallesPelicula(idPeli)
                        while(detallePeli == null){

                        }

                        //Iniciar valorar peli
                        val intentInfoPelicula = Intent(this, InfoPelicula::class.java)

                        intentInfoPelicula.putExtra("PELICULA", detallePeli)
                        intentInfoPelicula.putExtra("IMAGEN", imagenPeli)
                        Log.i("IMAGEN", imagenPeli!!)

                        intentInfoPelicula.putExtra("IDSESION", idSesion)
                        intentInfoPelicula.putExtra("IDCUENTA", idCuenta)
                        intentInfoPelicula.putExtra("TOKEN", intent.getStringExtra("TOKEN"))

                        startActivity(intentInfoPelicula)
                    }
                    botonValorarPeli.setText(R.string.valorarpeli)
                    botonValorarPeli.textSize = 12.0f
                    botonValorarPeli.layoutParams = parametros_layout
                    layoutPeliculaUnidad.addView(botonValorarPeli)

                    var tituloPeli = TextView(this)
                    Log.i("PELICULA", vectorPeliculasMeGusta.getJSONObject(i).toString())
                    tituloPeli.text = vectorPeliculasMeGusta.getJSONObject(i).getString("title")
                    tituloPeli.textSize = 16.0f
                    tituloPeli.setTextColor(getColor(R.color.black))
                    tituloPeli.layoutParams = parametros_layout
                    layoutPeliculaUnidad.addView(tituloPeli)

                    scrollPelis.addView(layoutPeliculaUnidad)
                }
            }
            R.id.listanomegustaboton -> {
                if(cursorPeliculasNoMeGusta!!.moveToFirst()){
                    do{
                        var layoutPeliculaUnidad = LinearLayout(this)
                        layoutPeliculaUnidad.orientation = LinearLayout.HORIZONTAL
                        val parametros_layout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        parametros_layout.setMargins(20,20,20,0)

                        var botonValorarPeli = Button(this)
                        botonValorarPeli.setOnClickListener {
                            imagenPeli = null
                            detallePeli = null
                            val idPeli = cursorPeliculasNoMeGusta!!.getInt(0)
                            //Obtener imagen
                            peticionImagen(idPeli)
                            while (imagenPeli == null){

                            }

                            //Obtener info de peli
                            peticionDetallesPelicula(idPeli)
                            while(detallePeli == null){

                            }

                            //Iniciar valorar peli
                            val intentInfoPelicula = Intent(this, InfoPelicula::class.java)

                            intentInfoPelicula.putExtra("PELICULA", detallePeli)
                            intentInfoPelicula.putExtra("IMAGEN", imagenPeli)
                            Log.i("IMAGEN", imagenPeli!!)

                            intentInfoPelicula.putExtra("IDSESION", idSesion)
                            intentInfoPelicula.putExtra("IDCUENTA", idCuenta)
                            intentInfoPelicula.putExtra("TOKEN", intent.getStringExtra("TOKEN"))

                            startActivity(intentInfoPelicula)
                        }
                        botonValorarPeli.setText(R.string.valorarpeli)
                        botonValorarPeli.textSize = 12.0f
                        botonValorarPeli.layoutParams = parametros_layout
                        layoutPeliculaUnidad.addView(botonValorarPeli)

                        var tituloPeli = TextView(this)
                        tituloPeli.text = cursorPeliculasNoMeGusta!!.getString(1)
                        tituloPeli.textSize = 16.0f
                        tituloPeli.setTextColor(getColor(R.color.black))
                        tituloPeli.layoutParams = parametros_layout
                        layoutPeliculaUnidad.addView(tituloPeli)

                        scrollPelis.addView(layoutPeliculaUnidad)
                    }while (cursorPeliculasNoMeGusta!!.moveToNext())
                }
            }
        }
    }

    /**
     * Función para obtener la lista de seguimiento del usuario
     */
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
                    }
                })
    }

    /**
     * Función para obtener las películas que le gustan al usuario
     */
    fun peticionPeliculasMeGusta(){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/account/$idCuenta/favorite/movies?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idSesion")
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

                        peliculasMeGusta = res.getJSONArray("results").toString()
                        Log.i("PELICULAS", peliculasMeGusta!!)
                    }
                })
    }

    /**
     * Función que nos proporciona la lista de películas que
     * no le gustan al usuario
     */
    fun peticionPeliculasNoMeGusta(){
        var dbHelper = MyDbHelper(this)
        var sqliteBD = dbHelper.readableDatabase

        var cursor = sqliteBD.rawQuery("SELECT * FROM ${PeliculaContract.PeliculaEntry.TABLE_NAME}", null)
        cursorPeliculasNoMeGusta = cursor
    }

    /**
     * Función para solicitar el poster de una película concreta.
     *
     * Dicho poster será parte de una URI donde realizaremos una
     * petición posteriormente para obtener la imagen en cuestión.
     */
    fun peticionImagen(idPeli: Int){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$idPeli/images?api_key=ecfe4f06a0f028c3618838df92bfea77")
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
                        val res = JSONObject(response.body()!!.string()).getJSONArray("posters")
                        imagenPeli = res.getJSONObject(0).getString("file_path")
                    }
                })
    }

    /**
     * Función para mostrar los detalles de una película en concreto
     * (póster, título y sinopsis)
     */
    fun peticionDetallesPelicula(idPeli: Int){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$idPeli?api_key=ecfe4f06a0f028c3618838df92bfea77")
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
                        detallePeli = response.body()!!.string()
                    }
                })
    }
}