package com.veramed.inventario.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * from usuario ")
    fun getAllItems(): Flow<List<Usuario>>

    @Query("SELECT * from usuario WHERE id = :id")
    fun getItem(id: Int): Flow<Usuario>

    @Query("SELECT * from usuario WHERE id = :id AND password = :pass")
    fun getUsuario(id: Int, pass:String): Flow<Usuario>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}
