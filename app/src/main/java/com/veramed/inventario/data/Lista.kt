package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "lista")
data class Lista(

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    val idusuario:Int,
    val descrip:String,
    val color:Int,
    val fecha:Long,
    val feccrea:String,
    val tipo:Int,
    val centro:Int

)
