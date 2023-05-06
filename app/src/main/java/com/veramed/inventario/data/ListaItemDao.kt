package com.veramed.inventario.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ListaItemDao {

    @Query("SELECT * from listaitems ORDER BY id DESC ")
    fun getAllItems(): Flow<List<ListaItems>>

    @Query("SELECT * from listaitems WHERE id = :id")
    fun getItem(id: Int): Flow<ListaItems>

    @Query("SELECT * from listaitems WHERE idlista = :idlista ORDER BY id DESC")
    fun getItemLista(idlista: Int): Flow<List<ListaItems>>

    @Query("SELECT * from listaitems WHERE barra = :barra")
    fun getItembyBarra(barra:String): Flow<ListaItems>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ListaItems)

    @Update
    suspend fun update(item: ListaItems)

    @Delete
    suspend fun delete(item: ListaItems)
}