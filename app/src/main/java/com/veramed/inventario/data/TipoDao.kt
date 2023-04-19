package com.veramed.inventario.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoDao {
    @Query("SELECT * from tipos ")
    fun getAllItems(): Flow<List<Tipo>>

    @Query("SELECT * from tipos WHERE id = :id")
    fun getItem(id: Int): Flow<Tipo>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tipo: Tipo)

    @Update
    suspend fun update(tipo: Tipo)

    @Delete
    suspend fun delete(tipo: Tipo)
}
