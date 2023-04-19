package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow


interface TipoRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllTipoStream(): Flow<List<Tipo>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getTipoStream(id: Int): Flow<Tipo?>

    /**
     * Insert item in the data source
     */
    suspend fun insertTipo(tipo:Tipo)

    /**
     * Delete item from the data source
     */
    suspend fun deleteTipo(tipo:Tipo)

    /**
     * Update item in the data source
     */
    suspend fun updateTipo(tipo:Tipo)
}
