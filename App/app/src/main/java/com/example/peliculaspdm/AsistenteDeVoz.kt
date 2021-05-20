package com.example.peliculaspdm

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.*

class AsistenteDeVoz : AppCompatActivity() {
    private val SPEECH_REQUEST_CODE = 0
    var reproductor: TextToSpeech? = null
    //Estados -> 0: Inicial, 1: Confirmación genero, 2: época, 3: Confirmación época, 4: popularidad, 5: confirmación popularidad
    var estadoAsistente: Int = 0
    var anioPeli: Int = -1
    var popular: Boolean = false
    var genero: String = ""
    var generosAPI: String = ""
    var peliculasRecomendadas: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistente_de_voz)
        obtenerIdsGeneros()
    }

    override fun onStart() {
        super.onStart()
        reproductor = TextToSpeech(this, object: TextToSpeech.OnInitListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onInit(status: Int) {
                val localeSpanish = Locale("es", "ES")
                reproductor!!.language = localeSpanish
                reproductor!!.speak("Bienvenido al asistente de voz. En primer lugar, ",TextToSpeech.QUEUE_ADD, null, "")
                decirGeneros()
            }
        })
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }

            when(estadoAsistente){
                0 -> {
                    eleccionGenero(spokenText!!)
                }
                1 -> {
                    confirmarGenero(spokenText!!)
                }
                2 -> {
                    obtenerAnio(spokenText!!)
                }
                3 -> {
                    confirmarAnio(spokenText!!)
                }
                4 -> {
                    obtenerPopularidad(spokenText!!)
                }
                5 -> {
                    confirmarPopular(spokenText!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun pulsaParaHablar(v: View){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun decirGeneros(){
        reproductor!!.speak("Dígame uno de los siguientes géneros",TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.drama),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.comedia),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.accion),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.thriller),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.scifi),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.documental),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.terror),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.western),TextToSpeech.QUEUE_ADD, null, "")
        reproductor!!.speak(getString(R.string.musical),TextToSpeech.QUEUE_ADD, null, "")

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun eleccionGenero(spokenText: String) {
        var JSONGeneros = JSONObject(generosAPI).getJSONArray("genres")
        when (spokenText) {
            "drama" -> {
                reproductor!!.speak("Ha elegido drama, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(6).getString("id")
                estadoAsistente = 1
            }
            "comedia", "comedy" -> {
                reproductor!!.speak("Ha elegido comedia, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(3).getString("id")
                estadoAsistente = 1
            }
            "acción", "action" -> {
                reproductor!!.speak("Ha elegido acción, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(0).getString("id")
                estadoAsistente = 1
            }
            "thriller" -> {
                reproductor!!.speak("Ha elegido thriller, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(16).getString("id")
                estadoAsistente = 1
            }
            "ciencia ficción" -> {
                reproductor!!.speak("Ha elegido ciencia ficción, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(14).getString("id")
                estadoAsistente = 1
            }
            "documental" -> {
                reproductor!!.speak("Ha elegido documental, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(5).getString("id")
                estadoAsistente = 1
            }
            "terror" -> {
                reproductor!!.speak("Ha elegido terror, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(10).getString("id")
                estadoAsistente = 1
            }
            "western" -> {
                reproductor!!.speak("Ha elegido western, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(18).getString("id")
                estadoAsistente = 1
            }
            "musical" -> {
                reproductor!!.speak("Ha elegido musical, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                genero = JSONGeneros.getJSONObject(11).getString("id")
                estadoAsistente = 1
            }
            else -> {
                reproductor!!.speak("No le he entendido, por favor, vuelva a repetir la opción seleccionada", TextToSpeech.QUEUE_ADD, null, "")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun confirmarGenero(spokenText: String){
        when(spokenText){
            "si", "yes" -> {
                preguntarAnio()
                estadoAsistente = 2
            }
            "no" -> {
                decirGeneros()
                estadoAsistente = 0
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun preguntarAnio(){
        reproductor!!.speak("Diga ahora el año del que desea la película", TextToSpeech.QUEUE_ADD, null, "")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun obtenerAnio(spokenText: String){
        if(!comprobarValidezAnio(spokenText)){
            reproductor!!.speak("Por favor, diga un año válido", TextToSpeech.QUEUE_ADD, null, "")
        }
        else{
            val stringNumero = spokenText.toInt()
            anioPeli = stringNumero
            reproductor!!.speak("Ha dicho el año $spokenText, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
            estadoAsistente = 3
        }
    }

    fun comprobarValidezAnio(anio: String): Boolean {
        return try {
            anio.toInt()
            true
        } catch (e: Exception){
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun confirmarAnio(spokenText: String){
        when(spokenText){
            "si", "yes" -> {
                preguntarPopularidad()
                estadoAsistente = 4
            }
            "no" -> {
                preguntarAnio()
                estadoAsistente = 2
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun preguntarPopularidad(){
        reproductor!!.speak("Por último, ¿desea una película conocida?", TextToSpeech.QUEUE_ADD, null, "")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun obtenerPopularidad(spokenText: String){
        when(spokenText){
            "si", "yes" -> {
                reproductor!!.speak("Ha elegido una película popular, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                popular = true
                estadoAsistente = 5
            }
            "no" -> {
                reproductor!!.speak("Ha elegido una película no conocida, ¿es correcto?", TextToSpeech.QUEUE_ADD, null, "")
                popular = false
                estadoAsistente = 5
            }
            else -> {
                reproductor!!.speak("Por favor, responda con si o no", TextToSpeech.QUEUE_ADD, null, "")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun confirmarPopular(spokenText: String){
        when(spokenText){
            "si", "yes" -> {
                peticionPeliculas()
                while (peliculasRecomendadas == ""){

                }

                val intentPeliculasRelacionadas = Intent(this, PeliculasRelacionadas::class.java)
                intentPeliculasRelacionadas.putExtra("PELICULAS", peliculasRecomendadas)

                val idCuenta = intent.getStringExtra("IDCUENTA")
                intentPeliculasRelacionadas.putExtra("IDCUENTA", idCuenta)

                val idSesion = intent.getStringExtra("IDSESION")
                intentPeliculasRelacionadas.putExtra("IDSESION", idSesion)

                val requestToken = intent.getStringExtra("TOKEN")
                intentPeliculasRelacionadas.putExtra("TOKEN", requestToken)

                startActivity(intentPeliculasRelacionadas)
            }
            "no" -> {
                preguntarPopularidad()
                estadoAsistente = 4
            }
            else -> {
                reproductor!!.speak("Perdone, no le he entendido", TextToSpeech.QUEUE_ADD, null, "")
            }
        }
    }

    fun obtenerIdsGeneros(){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/genre/movie/list?api_key=ecfe4f06a0f028c3618838df92bfea77")
                .build()
        val cliente = OkHttpClient()

        cliente.newCall(request)
                .enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        //Nada
                    }

                    override fun onResponse(call: Call, response: Response) {
                        generosAPI = response.body()!!.string()
                    }

                })
    }

    fun peticionPeliculas(){
        var request: Request? = null

        if(popular){
            request = Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&" +
                            "with_genres=$genero&" +
                            "year=$anioPeli&" +
                            "sort_by=popularity.desc")
                    .build()
        }
        else{
            request = Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&" +
                            "with_genres=$genero&" +
                            "release_date.gte=$anioPeli&" +
                            "sort_by=popularity.asc&vote_count.gte=25")
                    .build()
        }
        val cliente = OkHttpClient()

        cliente.newCall(request)
                .enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        //Nada
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var res = JSONObject(response.body()!!.string())
                        peliculasRecomendadas = res.getJSONArray("results").toString()
                    }

                })
    }
}