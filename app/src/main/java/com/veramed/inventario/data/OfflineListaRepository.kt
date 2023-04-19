package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

class OfflineListaRepository(private val listaDao: ListaDao) : ListaRepository  {

    override fun getAllListaStream(): Flow<List<Lista>> = listaDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Lista?> = listaDao.getItem(id)

    override suspend fun insertLista(lista: Lista) = listaDao.insert(lista)

    override suspend fun deleteLista(lista: Lista) = listaDao.delete(lista)

    override suspend fun updateLista(lista: Lista) = listaDao.update(lista)
}