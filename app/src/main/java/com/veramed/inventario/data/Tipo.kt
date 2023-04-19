package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tipos")
data class Tipo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String

)