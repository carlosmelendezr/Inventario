package com.veramed.inventario.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDao {
    @Query("SELECT *,(SELECT count(*) FROM listaitems  WHERE lista.id = listaitems.idlista ) AS articulos from lista ORDER BY fecha ASC")
    fun getAllItems(): Flow<List<Lista>>

    @Query("SELECT * from lista WHERE id = :id")
    fun getItem(id: Int): Flow<Lista>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(lista: Lista)

    @Update
    suspend fun update(lista: Lista)

    @Delete
    suspend fun delete(lista: Lista)
}
