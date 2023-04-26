package com.veramed.inventario.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
    val listaRepository: ListaRepository
    val tipoRepository: TipoRepository
    val usuarioRepository:UsuarioRepository
    val sesionRepository:SesionRepository
    val listaItemRepository:ListaItemRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }

    override val listaRepository: ListaRepository by lazy {
        OfflineListaRepository(InventoryDatabase.getDatabase(context).listaDao())
    }

    override val tipoRepository: TipoRepository by lazy {
        OfflineTipoRepository(InventoryDatabase.getDatabase(context).tipoDao())
    }

    override val usuarioRepository: UsuarioRepository by lazy {
        OfflineUsuarioRepository(InventoryDatabase.getDatabase(context).usuarioDao())
    }
    override val sesionRepository: SesionRepository by lazy {
        OfflineSesionRepository(InventoryDatabase.getDatabase(context).sesionDao())
    }


    override val listaItemRepository: ListaItemRepository by lazy {
        OfflineListaItemRepository(InventoryDatabase.getDatabase(context).ListaItemDao())
    }
}