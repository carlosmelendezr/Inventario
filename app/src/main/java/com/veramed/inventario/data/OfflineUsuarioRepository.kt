package com.veramed.inventario.data
import kotlinx.coroutines.flow.Flow

class OfflineUsuarioRepository(private val usuarioDao: UsuarioDao) : UsuarioRepository  {

    override fun getAllUsuarioStream(): Flow<List<Usuario>> = usuarioDao.getAllItems()

    override fun getUsuarioStream(id: Int): Flow<Usuario?> = usuarioDao.getItem(id)

    override suspend fun insertUsuario(usuario: Usuario) = usuarioDao.insert(usuario)

    override suspend fun deleteUsuario(usuario: Usuario) = usuarioDao.delete(usuario)

    override suspend fun updateUsuario(usuario: Usuario) = usuarioDao.update(usuario)
}