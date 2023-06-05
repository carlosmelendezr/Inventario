package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow

class OfflineSesionRepository(private val sesionDao: SesionDao) : SesionRepository  {

    override fun getSesionActual(): Flow<Sesion> = sesionDao.getSesionActual()

    override suspend fun inicarSesion(sesion:Sesion)  = sesionDao.iniciarSesion(sesion)

    override suspend fun insertSesion(sesion:Sesion) = sesionDao.insert(sesion)

    override suspend fun deleteSesion(sesion:Sesion) = sesionDao.delete(sesion)

    override suspend fun updateSesion(sesion:Sesion) = sesionDao.update(sesion)
}