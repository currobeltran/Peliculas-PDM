package com.example.peliculaspdm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ObtenerRecomendacion2 : AppCompatActivity(){
    var currentLayout = -1
    var generos: String? = null
    var popular: Boolean = false
    var generosAPI: String? = null
    var generosSolicitados = ""

    var pickerDesde: NumberPicker? = null
    var pickerHasta: NumberPicker? = null
    var anioDesde: Int = 1920
    var anioHasta: Int = 1920

    var pickerPuntuacion: NumberPicker? = null
    var puntuacionMinima: Int = 0

    var peliculasRecomendadas: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obtener_recomendacion2)
        currentLayout = R.layout.activity_obtener_recomendacion2
    }

    fun botonParametroPulsado(v: View){
        when(currentLayout) {
            R.layout.activity_obtener_recomendacion2 -> {
                when(v.id){
                    R.id.mostrargeneros -> {
                        setContentView(R.layout.mostrargeneros)
                        currentLayout = R.layout.mostrargeneros

                        val groupcheckgeneros = findViewById<LinearLayout>(R.id.groupcheckgeneros)
                        if(generos != null){
                            generos = generos!!.removeRange(0,1)
                            generos = generos!!.removeRange(generos!!.length-1,generos!!.length)

                            var listageneros = generos!!.split(",")

                            for(i in 0 until groupcheckgeneros.childCount){
                                val elemento = groupcheckgeneros.getChildAt(i) as CheckBox

                                for(x in listageneros){
                                    if(elemento.id == x.toInt()){
                                        elemento.isChecked = true
                                    }
                                }
                            }
                        }
                    }

                    R.id.mostrarpopularidad -> {
                        setContentView(R.layout.mostrarpopularidad)
                        currentLayout = R.layout.mostrarpopularidad

                        if(popular){
                            val botonComercial = findViewById<RadioButton>(R.id.comercial)
                            botonComercial.isChecked = true
                        }
                        else{
                            val botonIndependiente = findViewById<RadioButton>(R.id.independiente)
                            botonIndependiente.isChecked = true
                        }
                    }

                    R.id.mostrarepocas -> {
                        setContentView(R.layout.mostrarepoca)
                        currentLayout = R.layout.mostrarepoca

                        pickerDesde = findViewById(R.id.pickerDesde)
                        pickerDesde!!.maxValue = 2020
                        pickerDesde!!.minValue = 1920
                        if(anioDesde != 1920){
                            pickerDesde!!.value = anioDesde
                        }

                        pickerHasta = findViewById(R.id.pickerHasta)
                        pickerHasta!!.maxValue = 2020
                        pickerHasta!!.minValue = 1920
                        if(anioHasta != 1920){
                            pickerHasta!!.value = anioHasta
                        }

                        pickerDesde!!.setOnValueChangedListener { _, _, newVal ->
                            pickerHasta!!.minValue = newVal
                            anioDesde = newVal
                        }

                        pickerHasta!!.setOnValueChangedListener { _, _, newVal ->
                            pickerDesde!!.maxValue = newVal
                            anioHasta = newVal
                        }
                    }

                    R.id.mostrarpuntuacionminima -> {
                        setContentView(R.layout.mostrarpuntuacion)
                        currentLayout = R.layout.mostrarpuntuacion

                        pickerPuntuacion = findViewById(R.id.pickerPuntuacion)
                        pickerPuntuacion!!.maxValue = 100
                        pickerPuntuacion!!.minValue = 0
                        if(puntuacionMinima != 0){
                            pickerPuntuacion!!.value = puntuacionMinima
                        }

                        pickerPuntuacion!!.setOnValueChangedListener{ _,_, newVal ->
                            puntuacionMinima = newVal
                        }
                    }
                }
            }

            R.layout.mostrargeneros -> {
                val groupcheckgeneros = findViewById<LinearLayout>(R.id.groupcheckgeneros)

                generos = "["
                for(i in 0 until groupcheckgeneros.childCount){
                    val elemento = groupcheckgeneros.getChildAt(i) as CheckBox

                    if(elemento.isChecked){
                        if(generos == "["){
                            generos += "${elemento.id}"
                        } else{
                            generos += ",${elemento.id}"
                        }
                    }

                }
                generos += "]"

                setContentView(R.layout.activity_obtener_recomendacion2)
                currentLayout = R.layout.activity_obtener_recomendacion2
            }

            R.layout.mostrarpopularidad -> {
                val grupoRadio = findViewById<RadioGroup>(R.id.grouppopularidad)

                popular = (grupoRadio.checkedRadioButtonId == R.id.comercial)

                setContentView(R.layout.activity_obtener_recomendacion2)
                currentLayout = R.layout.activity_obtener_recomendacion2
            }

            R.layout.mostrarepoca -> {
                setContentView(R.layout.activity_obtener_recomendacion2)
                currentLayout = R.layout.activity_obtener_recomendacion2
            }

            R.layout.mostrarpuntuacion -> {
                setContentView(R.layout.activity_obtener_recomendacion2)
                currentLayout = R.layout.activity_obtener_recomendacion2
            }
        }
    }

    @SuppressLint("InflateParams")
    fun verPeliculasRelacionadas(v: View){
        //Solicitar generos y obtener IDs
        obtenerIdsGeneros()

        while (generosAPI == null){

        }

        var generosAPIJSON = JSONObject(generosAPI).getJSONArray("genres")

        generos = generos!!.removeRange(0,1)
        generos = generos!!.removeRange(generos!!.length-1,generos!!.length)
        var listageneros = generos!!.split(",")

        var inflateView = layoutInflater.inflate(R.layout.mostrargeneros, null)
        for(element2 in listageneros){
            var elementoCheck = inflateView.findViewById<CheckBox>(element2.toInt())
            when(elementoCheck.text){
                "Drama" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(6).getString("id")},"
                }
                "Acción", "Action" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(0).getString("id")},"
                }
                "Comedia", "Comedy" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(3).getString("id")},"
                }
                "Thriller" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(16).getString("id")},"
                }
                "Ciencia Ficción", "Science Fiction" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(14).getString("id")},"
                }
                "Documental", "Documentary" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(5).getString("id")},"
                }
                "Terror", "Horror" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(10).getString("id")},"
                }
                "Western" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(18).getString("id")},"
                }
                "Musical" -> {
                    generosSolicitados += "${generosAPIJSON.getJSONObject(11).getString("id")},"
                }
                else -> {
                    generosSolicitados += ""
                }
            }
        }

        generosSolicitados = generosSolicitados!!.removeRange(generosSolicitados!!.length-1,generosSolicitados!!.length)

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
                            "with_genres=$generosSolicitados&" +
                            "vote_average.gte=${puntuacionMinima / 10.0f}&" +
                            "release_date.gte=$anioDesde&" +
                            "sort_by=popularity.desc")
                    .build()
        }
        else{
            request = Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&" +
                            "with_genres=$generosSolicitados&" +
                            "vote_average.gte=${puntuacionMinima / 10.0f}&" +
                            "release_date.gte=$anioDesde&" +
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
                        Log.i("PELICULAS RELACIONADAS", "https://api.themoviedb.org/3/discover/movie?api_key=ecfe4f06a0f028c3618838df92bfea77&" +
                                "with_genres=$generosSolicitados&" +
                                "vote_average.gte=${puntuacionMinima / 10.0f}&" +
                                "release_date.gte=$anioDesde&" +
                                "sort_by=popularity.desc")
                    }

                })
    }

    fun volverAtras(v: View){
        val activityInicio = Intent(this, MainActivity::class.java)

        val idCuenta = intent.getStringExtra("IDCUENTA")
        activityInicio.putExtra("IDCUENTA", idCuenta)

        val idSesion = intent.getStringExtra("IDSESION")
        activityInicio.putExtra("IDSESION", idSesion)

        val requestToken = intent.getStringExtra("TOKEN")
        activityInicio.putExtra("RequestToken", requestToken)

        startActivity(activityInicio)
    }
}