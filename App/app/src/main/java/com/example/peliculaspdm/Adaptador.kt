package com.example.peliculaspdm

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.huxq17.swipecardsview.BaseCardAdapter
import com.squareup.picasso.Picasso

class Adaptador : BaseCardAdapter<CardView> {
    var listaPelis: List<Pelicula> = listOf()
    var contexto: Context? = null

    constructor(listaPelis: List<Pelicula>, contexto: Context){
        this.listaPelis = listaPelis
        this.contexto = contexto
    }

    override fun getCount(): Int{
        return listaPelis.size
    }

    override fun getCardLayoutId(): Int{
        return R.layout.carta_pelicula
    }

    override fun onBindData(position: Int, cardview: View){
        if(listaPelis == null || listaPelis.isEmpty()){
            return
        }

        val imagen = cardview.findViewById<ImageView>(R.id.imagencarta)
        val texto = cardview.findViewById<TextView>(R.id.titulopelicula)
        var pelicula = listaPelis[position]
        texto.text = pelicula.titulo
        Picasso.with(contexto).load(pelicula.imagen).into(imagen)
    }
}