package com.example.peliculaspdm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class InicioSesion : AppCompatActivity() {
    var requestToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)
        peticionRequestToken()
    }

    fun inicioSesion(view: View){
        val nombreDeUsuario = findViewById<EditText>(R.id.editTextTextPersonName)
        val contraseña = findViewById<EditText>(R.id.editTextTextPassword)

        peticionInicioSesion(nombreDeUsuario.text.toString(), contraseña.text.toString(), this)
    }

    fun peticionRequestToken(){
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/authentication/token/new?api_key=ecfe4f06a0f028c3618838df92bfea77")
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

                    requestToken = res.getString("request_token")
                }
            })
    }

    fun peticionInicioSesion(usuario: String, contraseña: String, contexto: Context){
        val requestBody = FormBody.Builder()
            .add("username", usuario)
            .add("password", contraseña)
            .add("request_token", requestToken)
            .build()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=ecfe4f06a0f028c3618838df92bfea77")
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
                        val intentPantallaPrincipal = Intent(contexto,MainActivity::class.java)
                        intentPantallaPrincipal.putExtra("RequestToken", requestToken)
                        startActivity(intentPantallaPrincipal)
                    }
                    else{
                        var mensajeError = findViewById<TextView>(R.id.mensajeError)
                        if(Locale.getDefault().language == "es"){
                            mensajeError.text = "Credenciales incorrectas, vuelva a intentarlo"
                        }
                        else{
                            mensajeError.text = "Log in incorrect, please try again"
                        }
                    }
                }
            })
    }
}