package com.example.peliculaspdm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.collections.ArrayList

class SeleccionGenero : AppCompatActivity() {
    var epoca: String = ""
    var generos: JSONArray = JSONArray()
    var pelis1: JSONObject = JSONObject()
    var pelis2: JSONObject = JSONObject()
    var pelis3: JSONObject = JSONObject()
    var pelis4: JSONObject = JSONObject()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_genero)
        epoca = intent.getStringExtra("Epoca")!!
        generos = JSONObject(intent.getStringExtra("Generos")!!)
                .getJSONArray("genres")

        var vectorGenerosAleatorios = arrayOf("","","","")
        var vectorIDsAleatorios = arrayOf("","","","")

        val numerosAleatorios: List<Int> = IntStream
                .range(0, 18)
                .boxed()
                .collect(Collectors.toCollection { ArrayList() })

        Collections.shuffle(numerosAleatorios)

        for(i in 0 until 4){
            val generoRandom = generos.getJSONObject(numerosAleatorios[i]).getString("name")
            val idgenero = generos.getJSONObject(numerosAleatorios[i]).getString("id")

            vectorGenerosAleatorios[i] = generoRandom
            vectorIDsAleatorios[i] = idgenero
        }

        var añosAleatorios: List<Int> = listOf()

        if (epoca == "Clasica"){
            añosAleatorios = IntStream
                    .range(1920, 1959)
                    .boxed()
                    .collect(Collectors.toCollection { ArrayList() })
        }
        else if (epoca == "Clasico a color"){
            añosAleatorios = IntStream
                    .range(1960, 1989)
                    .boxed()
                    .collect(Collectors.toCollection { ArrayList() })
        }
        else if (epoca == "Actual"){
            añosAleatorios = IntStream
                    .range(1990, 2020)
                    .boxed()
                    .collect(Collectors.toCollection { ArrayList() })
        }

        Collections.shuffle(añosAleatorios)

        val boton1: Button = findViewById(R.id.opcion1)
        boton1.text = vectorGenerosAleatorios[0]
        for(i in 0 until 3){
            val urlPeticion = "https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&with_genres=${vectorIDsAleatorios[0]}&year=${añosAleatorios[i]}&sort_by=popularity.desc"
            solicitaPeliculas(urlPeticion, 1)
        }

        val boton2: Button = findViewById(R.id.opcion2)
        boton2.text = vectorGenerosAleatorios[1]
        for(i in 0 until 3){
            val urlPeticion = "https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&with_genres=${vectorIDsAleatorios[1]}&year=${añosAleatorios[i]}&sort_by=popularity.desc"
            solicitaPeliculas(urlPeticion, 2)
        }

        val boton3: Button = findViewById(R.id.opcion3)
        boton3.text = vectorGenerosAleatorios[2]
        for(i in 0 until 3){
            val urlPeticion = "https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&with_genres=${vectorIDsAleatorios[2]}&year=${añosAleatorios[i]}&sort_by=popularity.desc"
            solicitaPeliculas(urlPeticion, 3)
        }

        val boton4: Button = findViewById(R.id.opcion4)
        boton4.text = vectorGenerosAleatorios[3]
        for(i in 0 until 3){
            val urlPeticion = "https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&with_genres=${vectorIDsAleatorios[3]}&year=${añosAleatorios[i]}&sort_by=popularity.desc"
            solicitaPeliculas(urlPeticion, 4)
        }
    }

    fun mostrarPeliculasRelacionadas(view: View){
        val intentPeliculasRelacionadas = Intent(this, PeliculasRelacionadas::class.java)

        startActivity(intentPeliculasRelacionadas)
    }

    fun volverAtras(view: View){
        val intentInicioNoAsistente = Intent(this, InicioNoAsistente::class.java)

        startActivity(intentInicioNoAsistente)
    }

    @Throws(IOException::class)
    fun solicitaPeliculas(url: String?, opcion: Int) {
        val request = Request.Builder()
                .url(url)
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
                        val res: String = response.body()!!.string()

                        when (opcion) {
                            1 -> {
                                pelis1 = JSONObject(res)
                            }
                            2 -> {
                                pelis2 = JSONObject(res)
                            }
                            3 -> {
                                pelis3 = JSONObject(res)
                            }
                            4 -> {
                                pelis4 = JSONObject(res)
                            }
                        }
                    }
                })
    }
}