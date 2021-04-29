package com.example.peliculaspdm

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    var idsesion: String = ""
    var requestToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestToken = intent.getStringExtra("RequestToken")!!
        peticionIdSesion()
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
}