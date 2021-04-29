package com.example.peliculaspdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import okhttp3.*
import java.io.IOException

class InicioNoAsistente : AppCompatActivity() {
    var generos: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_no_asistente)
        solicitarGeneros()
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

        intentGenero.putExtra("Generos", generos)

        startActivity(intentGenero)
    }

    fun solicitarGeneros(){
        solicitaGeneros("https://api.themoviedb.org/3/genre/movie/list?api_key=ecfe4f06a0f028c3618838df92bfea77")
    }

    @Throws(IOException::class)
    fun solicitaGeneros(url: String?) {
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

                        generos = res
                    }
                })
    }
}