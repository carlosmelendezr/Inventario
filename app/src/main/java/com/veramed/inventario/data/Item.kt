package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items")
data class Item(

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false,deserialize = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int,
    val sap: String,
    val barra:String,
    val lote:String,
    val fecvence:String
)
