package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

class OfflineTipoRepository(private val tipoDao: TipoDao) : TipoRepository  {

    override fun getAllTipoStream(): Flow<List<Tipo>> = tipoDao.getAllItems()

    override fun getTipoStream(id: Int): Flow<Tipo?> = tipoDao.getItem(id)

    override suspend fun insertTipo(tipo:Tipo) = tipoDao.insert(tipo)

    override suspend fun deleteTipo(tipo:Tipo) = tipoDao.delete(tipo)

    override suspend fun updateTipo(tipo:Tipo) = tipoDao.update(tipo)
}