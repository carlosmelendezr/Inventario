package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface ListaRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllListaStream(): Flow<List<Lista>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<Lista?>

    /**
     * Insert item in the data source
     */
    suspend fun insertLista(lista: Lista)

    /**
     * Delete item from the data source
     */
    suspend fun deleteLista(lista: Lista)

    /**
     * Update item in the data source
     */
    suspend fun updateLista(lista: Lista)
}
