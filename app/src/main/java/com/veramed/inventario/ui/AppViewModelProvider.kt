

package com.veramed.inventario.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.veramed.inventario.InventoryApplication
import com.veramed.inventario.ui.home.HomeListaViewModel
import com.veramed.inventario.ui.home.HomeViewModel
import com.veramed.inventario.ui.item.ItemDetailsViewModel
import com.veramed.inventario.ui.item.ItemEditViewModel
import com.veramed.inventario.ui.item.ItemEntryViewModel
import com.veramed.inventario.ui.lista.ListaEntryViewModel
import com.veramed.inventario.ui.usuario.UsuarioEntryViewModel
import com.veramed.inventario.ui.usuario.UsuarioLoginViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        initializer {
            ListaEntryViewModel(inventoryApplication().container.listaRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }
        // Initializer for HomeListaViewModel
        initializer {
            HomeListaViewModel(inventoryApplication().container.listaRepository)
        }

        // Initializer for HomeListaViewModel
        initializer {
            UsuarioEntryViewModel(inventoryApplication().container.usuarioRepository)
        }

        initializer {
            UsuarioLoginViewModel(inventoryApplication().container.usuarioRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InventoryApplication)
