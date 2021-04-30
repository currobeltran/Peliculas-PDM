package com.example.peliculaspdm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var idsesion: String = ""
    var requestToken: String = ""
    var pelisRecomendadas: String = ""
    var filepath1: String = ""
    var filepath2: String = ""
    var filepath3: String = ""
    var filepath4: String = ""

    var vectorPelisRecomendadasDefinitivas = arrayListOf(JSONObject(),
        JSONObject(),
        JSONObject(),
        JSONObject())

    var imageListener: ImageListener = ImageListener { position, imageView ->
        val idPeli = vectorPelisRecomendadasDefinitivas[position].getInt("id")

        peticionImagenesPelicula(idPeli, position)

        var filepath = ""
        when (position) {
            0 -> {
                while (filepath1 == ""){

                }
                filepath = filepath1
            }
            1 -> {
                while (filepath2 == ""){

                }
                filepath = filepath2
            }
            2 -> {
                while (filepath3 == ""){

                }
                filepath = filepath3
            }
            3 -> {
                while (filepath4 == ""){

                }
                filepath = filepath4
            }
        }

        val enlaceImagen = "https://image.tmdb.org/t/p/w500$filepath"
        Log.i("ENLACE", enlaceImagen)
        Picasso.with(this).load(enlaceImagen).into(imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        peticionRecomendaciones()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestToken = intent.getStringExtra("RequestToken")!!
        peticionIdSesion()

        while (pelisRecomendadas == ""){
            //Pequeña espera para que el vector no sea vacío
        }

        var vectorPelisRecomendadas = JSONArray(pelisRecomendadas)

        for(i in 0 until 4){
            vectorPelisRecomendadasDefinitivas[i] = vectorPelisRecomendadas.getJSONObject(i)
        }

        val carouselView = findViewById<CarouselView>(R.id.carouselView);
        carouselView.pageCount = vectorPelisRecomendadasDefinitivas.size;
        carouselView.setImageListener(imageListener);
    }

    fun noAsistente(view: View){
        val intentNoAsistente = Intent(this, InicioNoAsistente::class.java)

        startActivity(intentNoAsistente)
    }

    fun siAsistente(view: View){


    }

    fun peticionIdSesion(){
        val requestBody = FormBody.Builder()
            .add("request_token", requestToken)
            .build()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/authentication/session/new?api_key=ecfe4f06a0f028c3618838df92bfea77")
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

                    if(res.getBoolean("success")){
                        idsesion = res.getString("session_id")
                        Log.i("IDSESION", idsesion)
                    }
                }
            })
    }

    fun peticionRecomendaciones(){
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/trending/movie/week?api_key=ecfe4f06a0f028c3618838df92bfea77")
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

                    pelisRecomendadas = res.getJSONArray("results").toString()
                }
            })
    }

    fun peticionImagenesPelicula(idPeli: Int, position: Int){
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
                    Log.i("FILEPATH", res.getJSONObject(0).getString("file_path"))
                    when(position){
                        0 -> filepath1 = res.getJSONObject(0).getString("file_path")

                        1 -> filepath2 = res.getJSONObject(0).getString("file_path")

                        2 -> filepath3 = res.getJSONObject(0).getString("file_path")

                        3 -> filepath4 = res.getJSONObject(0).getString("file_path")
                    }
                }
            })
    }
}