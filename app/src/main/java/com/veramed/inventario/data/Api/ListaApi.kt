package com.veramed.inventario.data.Api


import com.google.gson.annotations.Expose
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import com.veramed.util.convertLongToTime
import java.util.*


data class ListaApi(

    @Expose(serialize = false,deserialize = true)
    val id:Int,
    val idusuario:Int,
    val descrip:String,
    val color:Int,
    val fecha: String,
    val tipo:Int,
    val centro:Int
)

fun Lista.toListaApi(): ListaApi = ListaApi(
    id=id,
    idusuario = id,
    descrip= descrip,
    color = color,
    fecha = convertLongToTime(fecha),
    tipo=tipo,
    centro=centro
)