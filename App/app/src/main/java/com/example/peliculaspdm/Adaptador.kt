package com.example.peliculaspdm

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.huxq17.swipecardsview.BaseCardAdapter
import com.squareup.picasso.Picasso

/**
 * Adaptador para las cartas deslizables en la búsqueda de
 * películas.
 *
 * Este adaptador se usará para mostrar una lista determinada
 * de objetos Pelicula en el momento que se realice una búsqueda
 */
class Adaptador : BaseCardAdapter<CardView> {
    var listaPelis: List<Pelicula> = listOf()
    var contexto: Context? = null

    constructor(listaPelis: List<Pelicula>, contexto: Context){
        this.listaPelis = listaPelis
        this.contexto = contexto
    }

    /**
     * Obtiene el numero de peliculas que maneja el adaptador
     */
    override fun getCount(): Int{
        return listaPelis.size
    }

    /**
     * Obtiene el ID del layout donde se mostrará la información
     * correspondiente a una película (el diseño de la carta)
     */
    override fun getCardLayoutId(): Int{
        return R.layout.carta_pelicula
    }

    /**
     * En esta función se carga la información de una película
     * en el view correspondiente a la carta.
     */
    override fun onBindData(position: Int, cardview: View){
        if(listaPelis.isEmpty()){
            return
        }

        val imagen = cardview.findViewById<ImageView>(R.id.imagencarta)
        val texto = cardview.findViewById<TextView>(R.id.titulopelicula)
        var pelicula = listaPelis[position]
        texto.text = pelicula.titulo
        Picasso.with(contexto).load(pelicula.imagen).into(imagen)
    }
}