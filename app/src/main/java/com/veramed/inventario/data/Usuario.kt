package com.veramed.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String,
    val nivel: Int,
    val password:String

    )