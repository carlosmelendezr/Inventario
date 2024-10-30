package com.veramed.inventario.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Moveventos (
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    @SerializedName("idingreso")
    val idingreso: Int,
    @SerializedName("cant")
    val cant: Int,
    @SerializedName("idcausa")
    val idcausa: Int,
    @SerializedName("fecha")
    val fecha: String
)


