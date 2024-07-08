package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*



@Entity(tableName = "listaitems")
data class Captura(
    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    val idlista:Int,
    val conteo:Int,
    val linea:String,

)
