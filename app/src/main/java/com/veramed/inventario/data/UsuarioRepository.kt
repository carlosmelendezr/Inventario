package com.veramed.inventario.data

import kotlinx.coroutines.flow.Flow


interface UsuarioRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllUsuarioStream(): Flow<List<Usuario>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getUsuarioStream(id: Int): Flow<Usuario?>

    fun getUsuario(id: Int): Usuario?

    /**
     * Insert item in the data source
     */
    suspend fun insertUsuario(usuario:Usuario)

    /**
     * Delete item from the data source
     */
    suspend fun deleteUsuario(usuario:Usuario)

    /**
     * Update item in the data source
     */
    suspend fun updateUsuario(usuario: Usuario)
}
