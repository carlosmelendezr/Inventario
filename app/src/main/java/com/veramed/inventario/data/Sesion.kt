package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "sesion")
data class Sesion(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val nivel: Int,

)
