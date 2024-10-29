package com.veramed.inventario.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Moveventos (
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    @SerializedName("idingreso")
    val idingreso: Integer,
    @SerializedName("cant")
    val cant: Integer,
    @SerializedName("idcausa")
    val idcausa: Integer,
    @SerializedName("fecha")
    val fecha: Date
)


