package com.example.peliculaspdm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huxq17.swipecardsview.SwipeCardsView


class PeliculasRelacionadas : AppCompatActivity() {
    var carta: SwipeCardsView? = null
    var listaPelis: List<Pelicula> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_relacionadas)

        carta = findViewById(R.id.SwipeCardsView)
        carta!!.retainLastCard(false)
        carta!!.enableSwipe(true)
        getData()
    }

    fun volverAtras(view: View){
        val intentSeleccionGenero = Intent(this, SeleccionGenero::class.java)

        startActivity(intentSeleccionGenero)
    }

    fun getData(){
        listaPelis = arrayListOf(Pelicula("De prueba","https://r2.abcimg.es/resizer/resizer.php?imagen=https%3A%2F%2Fstatic4.abc.es%2Fmedia%2Fpeliculas%2F000%2F000%2F344%2Fpulp-fiction-1.jpg&nuevoancho=150&medio=abc"),
            Pelicula("De prueba","https://r2.abcimg.es/resizer/resizer.php?imagen=https%3A%2F%2Fstatic4.abc.es%2Fmedia%2Fpeliculas%2F000%2F000%2F344%2Fpulp-fiction-1.jpg&nuevoancho=150&medio=abc"),
            Pelicula("De prueba","https://r2.abcimg.es/resizer/resizer.php?imagen=https%3A%2F%2Fstatic4.abc.es%2Fmedia%2Fpeliculas%2F000%2F000%2F344%2Fpulp-fiction-1.jpg&nuevoancho=150&medio=abc"),
            Pelicula("De prueba","https://r2.abcimg.es/resizer/resizer.php?imagen=https%3A%2F%2Fstatic4.abc.es%2Fmedia%2Fpeliculas%2F000%2F000%2F344%2Fpulp-fiction-1.jpg&nuevoancho=150&medio=abc"))

        var adaptador = Adaptador(listaPelis,this)
        carta!!.setAdapter(adaptador)
    }
}