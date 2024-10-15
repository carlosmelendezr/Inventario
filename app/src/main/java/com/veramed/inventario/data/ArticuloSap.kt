package com.veramed.inventario.data

import com.google.gson.annotations.SerializedName

data class ArticuloSap (
    @SerializedName("descrip")
    val Descripcion: String,
    @SerializedName("sap")
    val Sap: String,
    @SerializedName("docmat")
    val DocMat: String,
    @SerializedName("cantidad")
    val cantidad: Integer
    )




