package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

class OfflineListaItemRepository(private val listaitemDao: ListaItemDao):ListaItemRepository {

    override fun getAllItemsStream(): Flow<List<ListaItems>> = listaitemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<ListaItems?> = listaitemDao.getItem(id)

    override fun getItemLista(idlista: Int):Flow<List<ListaItems>> = listaitemDao.getItemLista(idlista)

    override suspend fun insertItem(item: ListaItems) = listaitemDao.insert(item)

    override suspend fun deleteItem(item: ListaItems) = listaitemDao.delete(item)

    override suspend fun updateItem(item: ListaItems) = listaitemDao.update(item)
}