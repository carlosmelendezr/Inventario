package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow


interface ListaItemRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<ListaItems>>


    fun getItemLista(idlista: Int): Flow<List<ListaItems>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<ListaItems?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ListaItems)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ListaItems)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ListaItems)
}
