package com.example.peliculaspdm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {
    /**
     * Variables procedentes de otros intents
     */
    var idsesion: String = ""
    var requestToken: String = ""

    /**
     * Distintas variables que manejarán información de
     * esta pantalla
     */
    var pelisRecomendadas: String = ""
    var filepath1: String = ""
    var filepath2: String = ""
    var filepath3: String = ""
    var filepath4: String = ""
    var infousuario: String = ""
    var pelisFavoritas: String = ""
    var vectorPelisRecomendadasDefinitivas = arrayListOf(JSONObject(),
            JSONObject(),
            JSONObject(),
            JSONObject())
    var ultimaPeliAleatoria = -1
    var nombrePeliRecomendada = ""

    /**
     * Variables que utilizaremos para controlar la acción con
     * el acelerómetro
     */
    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null
    var lastUpdate: Long = 0
    var last_x = 0f
    var last_y = 0f
    var last_z = 0f
    val SHAKE_THRESHOLD = 3000

    /**
     * Variable que controlará el carrusel de películas
     * Se define el Listener que tiene el proceso de carga de las imágenes
     * que aparecerán en el carrusel
     */
    var imageListener: ImageListener = ImageListener { position, imageView ->
        val idPeli = vectorPelisRecomendadasDefinitivas[position].getInt("id")

        peticionImagenesPelicula(idPeli, position)

        var filepath = ""
        when (position) {
            0 -> {
                while (filepath1 == "") {

                }
                filepath = filepath1
            }
            1 -> {
                while (filepath2 == "") {

                }
                filepath = filepath2
            }
            2 -> {
                while (filepath3 == "") {

                }
                filepath = filepath3
            }
            3 -> {
                while (filepath4 == "") {

                }
                filepath = filepath4
            }
        }

        val enlaceImagen = "https://image.tmdb.org/t/p/w500$filepath"
        Log.i("ENLACE", enlaceImagen)
        Picasso.with(this).load(enlaceImagen).into(imageView)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Se carga el acelerómetro para cambiar las películas del carrusel al agitar
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        //Obtenemos variables de otros intents
        requestToken = intent.getStringExtra("RequestToken")!!
        if(intent.getStringExtra("IDSESION") != null){
            idsesion = intent.getStringExtra("IDSESION")!!
        }

        else{
            peticionIdSesion()

            while(idsesion == ""){
                //Pequeña espera para obtener la información de la llamada anterior
            }
        }

        peticionInformacionCuenta()

        while(infousuario == ""){
            //Pequeña espera para obtener la información de la llamada anterior
        }

        peticionRecomendaciones()

        while (pelisRecomendadas == ""){
            //Pequeña espera para que el vector no sea vacío
        }

        var vectorPelisRecomendadas = JSONArray(pelisRecomendadas)

        for(i in 0 until 4){
            vectorPelisRecomendadasDefinitivas[i] = vectorPelisRecomendadas.getJSONObject(i)
        }

        //Iniciamos el carrusel
        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        carouselView.pageCount = vectorPelisRecomendadasDefinitivas.size
        carouselView.setImageListener(imageListener)
    }

    /**
     * Función de búsqueda de película sin asistente de voz
     * Se cargan las variables necesarias en el intent y se inicia
     */
    fun noAsistente(view: View){
        val intentObtenerRecomendacion = Intent(this, ObtenerRecomendacion2::class.java)
        intentObtenerRecomendacion.putExtra("IDSESION", idsesion)
        val jsonInfoUsuario = JSONObject(infousuario)
        val idUsuario = jsonInfoUsuario.getInt("id")
        intentObtenerRecomendacion.putExtra("IDCUENTA", idUsuario.toString())
        intentObtenerRecomendacion.putExtra("TOKEN", requestToken)

        startActivity(intentObtenerRecomendacion)
    }

    /**
     * Función de búsqueda de película con asistente de voz
     * Se cargan las variables necesarias en el intent y se inicia
     */
    fun siAsistente(view: View){
        val intentAsistenteDeVoz = Intent(this, AsistenteDeVoz::class.java)
        intentAsistenteDeVoz.putExtra("IDSESION", idsesion)
        val jsonInfoUsuario = JSONObject(infousuario)
        val idUsuario = jsonInfoUsuario.getInt("id")
        intentAsistenteDeVoz.putExtra("IDCUENTA", idUsuario.toString())
        intentAsistenteDeVoz.putExtra("TOKEN", requestToken)
        
        startActivity(intentAsistenteDeVoz)
    }

    /**
     * Función para obtener más información de una película y valorarla.
     * Dependiendo del elemento del carrusel, se visualizará la información de
     * una película u otra
     *
     * Se inicia una nueva actividad donde se muestra tanto el título, la imagen y
     * la sinopsis de la película
     */
    fun verInfoPelicula(view: View){
        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        val intentInfoPeliListaSeguir = Intent(this, InfoPeliculaListaSeguir::class.java)

        when(carouselView.currentItem){
            0 -> {
                val peliculaValoracion = vectorPelisRecomendadasDefinitivas[0]
                intentInfoPeliListaSeguir.putExtra("PELICULA", peliculaValoracion.toString())
                intentInfoPeliListaSeguir.putExtra("IMAGEN", filepath1)
            }
            1 -> {
                val peliculaValoracion = vectorPelisRecomendadasDefinitivas[1]
                intentInfoPeliListaSeguir.putExtra("PELICULA", peliculaValoracion.toString())
                intentInfoPeliListaSeguir.putExtra("IMAGEN", filepath2)
            }
            2 -> {
                val peliculaValoracion = vectorPelisRecomendadasDefinitivas[2]
                intentInfoPeliListaSeguir.putExtra("PELICULA", peliculaValoracion.toString())
                intentInfoPeliListaSeguir.putExtra("IMAGEN", filepath3)
            }
            3 -> {
                val peliculaValoracion = vectorPelisRecomendadasDefinitivas[3]
                intentInfoPeliListaSeguir.putExtra("PELICULA", peliculaValoracion.toString())
                intentInfoPeliListaSeguir.putExtra("IMAGEN", filepath4)
            }
        }
        intentInfoPeliListaSeguir.putExtra("IDSESION", idsesion)

        val jsonInfoUsuario = JSONObject(infousuario)
        val idUsuario = jsonInfoUsuario.getInt("id")
        intentInfoPeliListaSeguir.putExtra("IDCUENTA", idUsuario)
        intentInfoPeliListaSeguir.putExtra("TOKEN", requestToken)

        startActivity(intentInfoPeliListaSeguir)
    }

    /**
     * Función para solicitar un ID de sesión a través de un Request Token
     *
     * Este ID nos servirá para peticiones que realizaremos en un futuro.
     */
    fun peticionIdSesion(){
        val requestBody = FormBody.Builder()
            .add("request_token", requestToken)
            .build()

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/authentication/session/new?api_key=ecfe4f06a0f028c3618838df92bfea77")
            .method("POST", requestBody)
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

                    if (res.getBoolean("success")) {
                        idsesion = res.getString("session_id")
                        Log.i("IDSESION", idsesion)
                    }
                }
            })
    }

    /**
     * Función con la que se obtienen las películas recomendadas del carrusel
     * Si no existen suficientes películas valoradas positivamente por el usuario,
     * se muestran películas en tendencia actualmente dentro de la base de datos
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun peticionRecomendaciones(){
        peticionPeliculasFavoritas()

        while(pelisFavoritas == ""){

        }
        val arrayPelis = JSONArray(pelisFavoritas)

        if(arrayPelis.length() < 5){
            pelisFavoritas = ""
            peticionTendenciasPeliculas()
            while(pelisFavoritas == ""){

            }
            val mensaje = findViewById<TextView>(R.id.mensajePeliculasFavoritas)
            mensaje.text = "Valore positivamente 20 películas para obtener recomendaciones personalizadas"
        }

        peticionRecomendacionConPelicula()

        while(pelisRecomendadas == ""){

        }

        eliminaFavoritosRecomendaciones()
    }

    /**
     * Función para solicitar las películas favoritas del usuario
     */
    fun peticionPeliculasFavoritas(){
        val idCuenta = JSONObject(infousuario).getInt("id")
        Log.i("IDCUENTA", idCuenta.toString())
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/account/$idCuenta/favorite/movies?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idsesion")
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

                        pelisFavoritas = res.getJSONArray("results").toString()
                    }
                })
    }

    /**
     * Petición para obtener las películas recomendadas a partir de una película
     * concreta
     */
    fun peticionRecomendacionConPelicula(){
        val jsonArrayFavoritas = JSONArray(pelisFavoritas)
        val random = Random(System.currentTimeMillis())
        val cliente = OkHttpClient()

        ultimaPeliAleatoria = random.nextInt(0, jsonArrayFavoritas.length())

        val peliElegida = jsonArrayFavoritas.getJSONObject(ultimaPeliAleatoria)
        val idPeli = peliElegida.getInt("id")
        nombrePeliRecomendada = peliElegida.getString("title")

        val request2 = Request.Builder()
                .url("https://api.themoviedb.org/3/movie/$idPeli/recommendations?api_key=ecfe4f06a0f028c3618838df92bfea77")
                .build()

        cliente.newCall(request2)
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

    /**
     * Función para eliminar las películas que el usuario ha marcado como
     * que le gustan, de manera que no aparezcan en el carrusel de películas
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun eliminaFavoritosRecomendaciones(){
        var arrayPelisRecomendadas = JSONArray(pelisRecomendadas)
        var arrayPelisFavoritas = JSONArray(pelisFavoritas)

        for(i in 0 until arrayPelisFavoritas.length()){
            var peliculaFavorita = arrayPelisFavoritas.getJSONObject(i)

            for(j in 0 until arrayPelisRecomendadas.length()){
                if(j<arrayPelisRecomendadas.length()){
                    var peliculaRecomendada = arrayPelisRecomendadas.getJSONObject(j)

                    if (peliculaFavorita.getString("title") == peliculaRecomendada.getString("title")){
                        arrayPelisRecomendadas.remove(j)
                    }
                }
            }
        }

        pelisRecomendadas = arrayPelisRecomendadas.toString()
    }

    /**
     * Función para obtener las películas que están ahora mismo
     * siendo tendencia dentro de la base de datos
     */
    fun peticionTendenciasPeliculas(){
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

                        pelisFavoritas = res.getJSONArray("results").toString()
                    }
                })
    }

    /**
     * Función para solicitar el poster de una película concreta.
     *
     * Dicho poster será parte de una URI donde realizaremos una
     * petición posteriormente para obtener la imagen en cuestión.
     */
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
                    when (position) {
                        0 -> filepath1 = res.getJSONObject(0).getString("file_path")

                        1 -> filepath2 = res.getJSONObject(0).getString("file_path")

                        2 -> filepath3 = res.getJSONObject(0).getString("file_path")

                        3 -> filepath4 = res.getJSONObject(0).getString("file_path")
                    }
                }
            })
    }

    /**
     * Función para solicitar infomación acerca de nuestra cuenta de usuario
     *
     * Nos servirá para obtener nuestro ID de usuario dentro de la base de datos,
     * con lo que podremos realizar acciones sobre nuestra propia cuenta
     */
    fun peticionInformacionCuenta(){
        val request = Request.Builder()
                .url("https://api.themoviedb.org/3/account?api_key=ecfe4f06a0f028c3618838df92bfea77&session_id=$idsesion")
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
                        infousuario = response.body()!!.string()
                    }
                })
    }

    /**
     * Función que detectará cambios en el acelerómetro de nuestro móvil
     *
     * Si la velocidad de movimiento llega a un punto determinado, se cambian
     * las películas del carrusel.
     *
     * Para calcular la velocidad hay que realizar distintos cálculos matemáticos
     * ya que la información obtenida "en crudo" tiene también en cuenta la gravedad.
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == TYPE_ACCELEROMETER){
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val curTime = System.currentTimeMillis()

            if (curTime - lastUpdate > 100) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                val speed = abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

                if (speed > SHAKE_THRESHOLD) {
                    cambiarPeliculasCarrusel()
                }

                last_x = x
                last_y = y
                last_z = z
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    /**
     * Función que cambia las películas del carrusel, con el
     * objetivo de mostrar unas distintas
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun cambiarPeliculasCarrusel(){
        filepath1 = ""
        filepath2 = ""
        filepath3 = ""
        filepath4 = ""
        peticionRecomendaciones()

        while (pelisRecomendadas == ""){
            //Pequeña espera para que el vector no sea vacío
        }

        var vectorPelisRecomendadas = JSONArray(pelisRecomendadas)

        for(i in 0 until 4){
            vectorPelisRecomendadasDefinitivas[i] = vectorPelisRecomendadas.getJSONObject(i)
            peticionImagenesPelicula(vectorPelisRecomendadasDefinitivas[i].getInt("id"), i)
            when(i){
                0 -> {
                    while (filepath1 == ""){

                    }
                }
                1 -> {
                    while (filepath2 == ""){

                    }
                }
                2 -> {
                    while (filepath3 == ""){

                    }
                }
                3 -> {
                    while (filepath4 == ""){

                    }
                }
            }
        }

        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        carouselView.pageCount = vectorPelisRecomendadasDefinitivas.size
        carouselView.setImageListener(imageListener)
    }

    /**
     * Función para ver una lista de películas con las que se ha interactuado
     * recientemente
     */
    fun verMisPeliculas(v: View){
        val intentListaPeliculas = Intent(this, ListadoPeliculas::class.java)
        intentListaPeliculas.putExtra("IDSESION", idsesion)

        val jsonInfoUsuario = JSONObject(infousuario)
        val idUsuario = jsonInfoUsuario.getInt("id")
        intentListaPeliculas.putExtra("IDCUENTA", idUsuario.toString())
        intentListaPeliculas.putExtra("TOKEN", requestToken)

        startActivity(intentListaPeliculas)
    }
}