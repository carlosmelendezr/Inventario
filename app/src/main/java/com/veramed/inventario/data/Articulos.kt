package com.veramed.inventario.data

import com.google.gson.annotations.SerializedName

data class Articulos(
    @SerializedName("Table")
    val tables : List<Table>
)