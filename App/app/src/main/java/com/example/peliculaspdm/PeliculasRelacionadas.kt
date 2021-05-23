package com.example.peliculaspdm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.huxq17.swipecardsview.SwipeCardsView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class PeliculasRelacionadas : AppCompatActivity() {
    var cartas: SwipeCardsView? = null
    var listaPelis: List<Pelicula> = arrayListOf()
    var imagenesPeliSeleccionada = ""
    var idCuenta = ""
    var idSesion = ""

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_relacionadas)
        idCuenta = intent.getStringExtra("IDCUENTA")!!
        idSesion = intent.getStringExtra("IDSESION")!!

        cartas = findViewById(R.id.SwipeCardsView)
        cartas!!.retainLastCard(false)
        cartas!!.enableSwipe(true)
        cartas!!.setCardsSlideListener(object : SwipeCardsView.CardsSlideListener {
            override fun onShow(index: Int) {
                //NADA
            }

            override fun onCardVanish(index: Int, type: SwipeCardsView.SlideType?) {
                if (type == SwipeCardsView.SlideType.RIGHT) {
                    //Incluir película en peliculas que le gustan al usuario
                    var idPeli = listaPelis[index].id
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

            override fun onItemClick(cardImageView: View?, index: Int) {
                //NADA
            }

        })

        var peliculasRelacionadas = intent.getStringExtra("PELICULAS")
        var vectorPeliculasRelacionadasJSON = JSONArray(peliculasRelacionadas)
        var anioDesde = intent.getIntExtra("ANIODESDE", -1)

        var ind = 0
        while(ind < vectorPeliculasRelacionadasJSON.length()){
            val aniopeli = vectorPeliculasRelacionadasJSON.getJSONObject(ind).getString("release_date")
            val aniopelinumero = ("${aniopeli[0]}${aniopeli[1]}${aniopeli[2]}${aniopeli[3]}").toInt()
            Log.i("ANIOPELI", aniopelinumero.toString())
            if(aniopelinumero < anioDesde){
                vectorPeliculasRelacionadasJSON.remove(ind)
            }
            else{
                ind++
            }
        }

        if(vectorPeliculasRelacionadasJSON.length() > 5){
            for(i in 0 until 5){
                imagenesPeliSeleccionada = ""
                solicitarImagenes(vectorPeliculasRelacionadasJSON.getJSONObject(i).getInt("id"))
                while (imagenesPeliSeleccionada == ""){

                }

                var vectorImagenes = JSONArray(imagenesPeliSeleccionada)
                var imagenSeleccionada = ""
                if(vectorImagenes.length() > 0){
                    imagenSeleccionada = vectorImagenes.getJSONObject(0).getString("file_path")
                }
                var urlImagenSeleccionada = "https://image.tmdb.org/t/p/w500$imagenSeleccionada"

                insertarPeliculaLista(vectorPeliculasRelacionadasJSON.getJSONObject(i).getString("title"),
                        urlImagenSeleccionada,
                        vectorPeliculasRelacionadasJSON.getJSONObject(i).getString("id"))
            }

            getData()
        }
        else if(vectorPeliculasRelacionadasJSON.length() > 0){
            for(i in 0 until vectorPeliculasRelacionadasJSON.length()){
                imagenesPeliSeleccionada = ""
                solicitarImagenes(vectorPeliculasRelacionadasJSON.getJSONObject(i).getInt("id"))
                while (imagenesPeliSeleccionada == ""){

                }

                var vectorImagenes = JSONArray(imagenesPeliSeleccionada)
                var imagenSeleccionada = vectorImagenes.getJSONObject(0).getString("file_path")
                var urlImagenSeleccionada = "https://image.tmdb.org/t/p/w500$imagenSeleccionada"

                insertarPeliculaLista(vectorPeliculasRelacionadasJSON.getJSONObject(i).getString("title"),
                        urlImagenSeleccionada,
                        vectorPeliculasRelacionadasJSON.getJSONObject(i).getString("id"))
            }

            getData()
        }
        else{

        }
    }

    fun getData(){
        var adaptador = Adaptador(listaPelis,this)
        cartas!!.setAdapter(adaptador)
    }

    fun insertarPeliculaLista(titulo: String, imagen: String, idPeli: String){
        listaPelis += Pelicula(titulo, imagen, idPeli)
    }

    fun solicitarImagenes(idPeli: Int){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$idPeli/images?api_key=ecfe4f06a0f028c3618838df92bfea77")
                .build()
        val cliente = OkHttpClient()

        cliente.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        imagenesPeliSeleccionada = JSONObject(response.body()!!.string()).getJSONArray("posters").toString()
                    }

                })
    }

    fun volverInicio(v: View){
        val activityInicio = Intent(this, MainActivity::class.java)
        activityInicio.putExtra("IDCUENTA", idCuenta)
        activityInicio.putExtra("IDSESION", idSesion)
        val requestToken = intent.getStringExtra("TOKEN")
        activityInicio.putExtra("RequestToken", requestToken)

        startActivity(activityInicio)
    }
}