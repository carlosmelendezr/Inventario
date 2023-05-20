package com.veramed.inventario.ui.lista

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.ListaItemRepository
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.ui.item.*
import com.veramed.inventario.ui.item.toItemDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
 */
class ListaDetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val listaItemRepository: ListaItemRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    /**
     * Holds the item details ui state. The data is retrieved from [ItemsRepository] and mapped to
     * the UI state.
     */





        var detalleUiState: StateFlow<ListaItemDetalleUiState> =
            listaItemRepository.getItemStream(id=itemId)
                .also {
                    Log.d("IDL","ID ITEM ="+itemId)
                }
                .filterNotNull()
                .map {
                    ListaItemDetalleUiState(
                        outOfStock = false,
                        itemDetalle = it.toItemDetails()
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ListaItemDetalleUiState()
                )


    /**
     * Reduces the item quantity by one and update the [ItemsRepository]'s data source.
     */
   /* fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentItem = detalleUiState.value.itemDetails.toItem()
            if (currentItem.quantity > 0) {
                listaItemRepository.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
            }
        }
    }*/

    /**
     * Deletes the item from the [ItemsRepository]'s data source.
     */
    suspend fun deleteItem() {
        listaItemRepository.deleteItem(detalleUiState.value.itemDetalle.toItem(listaId =itemId ))
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ListaItemDetalleUiState(
    val outOfStock: Boolean = true,
    val itemDetalle: ListaItemDetails = ListaItemDetails()
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun ListaItems.toItemDetails(): ListaItemDetails = ListaItemDetails(
    id = id,
    name = descrip,
    sap=sap,
    barra=barra,
    quantity = cant.toString()

)

