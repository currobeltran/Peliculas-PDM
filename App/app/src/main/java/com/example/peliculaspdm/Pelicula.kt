package com.example.peliculaspdm

class Pelicula {
    var imagen: String = ""
    var titulo: String = ""

    constructor(titulo: String, imagen: String){
        this.titulo = titulo
        this.imagen = imagen
    }
}