package com.veramed.inventario.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {
    @Query("SELECT * from sesion ")
    fun getSesionActual(): Flow<List<Sesion>>

    @Query("DELETE FROM sesion ")
    suspend fun iniciarSesion(sesion:Sesion) {
        insert(sesion)
    }

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sesion:Sesion)

    @Update
    suspend fun update(sesion:Sesion)

    @Delete
    suspend fun delete(sesion:Sesion)
}
