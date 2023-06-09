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
import com.veramed.inventario.data.ListaItemRepository
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.ui.item.*
import com.veramed.inventario.ui.item.toItemDetails
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ListaDetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val listaItemRepository: ListaItemRepository
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
                        venceUiState = DatosVence(lote=it.lote,
                            fecvenc = it.fecvence,
                            mes=it.fecvence.substringBefore("-"),
                            year=it.fecvence.substringAfter("-"),
                            idlista = it.idlista)
                    }

        }

    }



    fun actualizaUiState(datosVence: DatosVence) {

      venceUiState = DatosVence(lote = datosVence.lote,
            fecvenc = datosVence.mes+"-"+datosVence.year,
            quantity = datosVence.quantity,
            idlista = datosVence.idlista,
            mes=datosVence.mes,year=datosVence.year)


    }
    fun saveItem() {
        var itemDetalle: ListaItemDetails = detalleUiState.itemDetalle
        Log.d("APIV","itemDetalle lote "+itemDetalle.lote)
        itemDetalle.descrip = itemDetalle.name
        itemDetalle.lote = venceUiState.lote
        itemDetalle.fecvenc = venceUiState.fecvenc


        viewModelScope.launch {
            listaItemRepository.updateItem(itemDetalle.toItem(venceUiState.idlista))

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
    val fecvenc:String="",
    val quantity:Int=0,
    val idlista:Int=0,
    val mes:String="",
    val year:String="",
    val lote:String= "$mes-$year",
    )

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)


fun ListaItems.toItemDetails(): ListaItemDetails = ListaItemDetails(
    id = id,
    name = descrip,
    sap=sap,
    barra=barra,
    quantity = cant.toString(),
    lote = lote,
    fecvenc = fecvence

)

