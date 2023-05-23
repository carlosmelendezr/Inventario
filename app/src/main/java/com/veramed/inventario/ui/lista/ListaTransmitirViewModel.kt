package com.veramed.inventario.ui.lista

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.*
import kotlinx.coroutines.flow.*

class ListaTransmitirViewModel(
    savedStateHandle: SavedStateHandle,
    private val listaRepository: ListaRepository,
    private val listaitemsRepository: ListaItemRepository,
    context: Context
) : ViewModel() {

     /**
     * Holds current item ui state
     */

    var listaId: Int = checkNotNull(savedStateHandle[ListaAgregarItemDestination.itemIdArg])


    var listaTUiState: StateFlow<ListaTUiState> =
        listaRepository.getItemStream(listaId).map{ListaTUiState(it)}
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ListaTransmitirViewModel.TIMEOUT_MILLIS),
            initialValue = ListaTUiState(Lista(0,0,"",0,0,"",0,0))
        )


    var listaArticulosUIState: StateFlow<ListaArticulosUiState> =
        listaitemsRepository.getItemLista(listaId).map { ListaArticulosUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListaArticulosUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}

data class ListaTUiState(val lista: Lista = Lista(0,0,"",0,0,"",0,0) )





