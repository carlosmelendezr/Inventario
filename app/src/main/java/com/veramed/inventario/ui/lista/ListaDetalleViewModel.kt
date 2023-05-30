package com.veramed.inventario.ui.lista

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ListaDetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val listaItemRepository: ListaItemRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])

    var venceUiState by mutableStateOf(DatosVence())
    var detalleUiState by mutableStateOf(ListaItemDetalleUiState())

    init {

        viewModelScope.launch {
                listaItemRepository.getItemStream(id=itemId)
                    .filterNotNull()
                    .collect{
                        detalleUiState = ListaItemDetalleUiState(
                            outOfStock = false,
                            itemDetalle = it.toItemDetails())
                        venceUiState = DatosVence(lote=it.lote, fecvenc = it.fecvenc)
                    }

        }

    }



    fun actualizaUiState(datosVence: DatosVence) {

      venceUiState = DatosVence(lote = datosVence.lote,
            fecvenc = datosVence.fecvenc,
            quantity = datosVence.quantity)


    }
    fun saveItem() {
        var itemDetalle: ListaItemDetails = detalleUiState.itemDetalle
        itemDetalle.lote = venceUiState.lote
        itemDetalle.fecvenc = venceUiState.fecvenc

        viewModelScope.launch {
            listaItemRepository.updateItem(itemDetalle.toItem(listaId =itemId ))

        }
    }


    suspend fun deleteItem() {
        listaItemRepository.deleteItem(detalleUiState.itemDetalle.toItem(listaId =itemId ))
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class ListaItemDetalleUiState(
    val outOfStock: Boolean = true,
    val itemDetalle: ListaItemDetails = ListaItemDetails(),

)

data class DatosVence(
    val lote:String="",
    val fecvenc:String="",
    val quantity:Int=0
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
    quantity = cant.toString(),
    lote = lote,
    fecvenc = fecvenc

)

