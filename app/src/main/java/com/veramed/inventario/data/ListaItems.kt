package com.veramed.inventario.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*



@Entity(tableName = "listaitems")
data class ListaItems(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idlista:Int,
    val iditem:Int,
    val sap:String,
    val barra:String,
    val descrip:String,
    val cant:Int

)
