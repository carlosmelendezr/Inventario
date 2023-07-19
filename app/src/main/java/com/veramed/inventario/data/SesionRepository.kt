package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

interface SesionRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getSesionActual(): Flow<Sesion>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    suspend fun inicarSesion(sesion:Sesion)

    suspend fun deleteAll()

    /**
     * Insert item in the data source
     */
    suspend fun insertSesion(sesion:Sesion)

    /**
     * Delete item from the data source
     */
    suspend fun deleteSesion(sesion:Sesion)

    /**
     * Update item in the data source
     */
    suspend fun updateSesion(sesion:Sesion)
}