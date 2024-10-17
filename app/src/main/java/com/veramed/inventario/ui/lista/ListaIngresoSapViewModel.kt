package com.veramed.inventario.ui.lista

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.R
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.ListaItemRepository
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.api.GetArticuloRMH
import com.veramed.inventario.data.api.getListaIngreso
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaIngresoSapViewModel(
    savedStateHandle: SavedStateHandle,
    context: Context
) : ViewModel() {

    private val mp: MediaPlayer = MediaPlayer.create(context, R.raw.error)

    /**
     * Holds current item ui state
     */

    var docmat: String = checkNotNull(savedStateHandle[ListaIngresoSapDestination.docmat])

    private var existe by mutableStateOf(false)
    private var error by mutableStateOf(false)

    var listaArticulosSapUIState by mutableStateOf(ListaArticuloSapUiState() )

     init {
         if (listaArticulosSapUIState.itemList.isEmpty()) {
             getListaIngreso(this, docmat)
         }
     }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}



data class ListaArticuloSapUiState(var itemList: List<ArticuloSap> = listOf())


