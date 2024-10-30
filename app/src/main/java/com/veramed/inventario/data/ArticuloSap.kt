package com.veramed.inventario.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticuloSap (
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    @SerializedName("descrip")
    val Descripcion: String,
    @SerializedName("sap")
    val Sap: String,
    @SerializedName("docmat")
    val DocMat: String,
    @SerializedName("cantidad")
    val cantidad: Int,
    @Expose(serialize = false,deserialize = false)
    var estatus:Int = 0

    )




