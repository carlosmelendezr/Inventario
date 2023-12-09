package com.veramed.inventario.ui.item

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
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.ui.lista.AgregarItemUiState
import com.veramed.inventario.ui.lista.ListaAgregarItemDestination
import com.veramed.inventario.ui.lista.ListaItemDetails
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class ConsultorViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    context: Context
) : ViewModel() {

    private val mp: MediaPlayer = MediaPlayer.create( context, R.raw.error)

    var listaItemUiState by mutableStateOf(AgregarItemUiState())
        private set

    private var existe by mutableStateOf(false)
    private var error by mutableStateOf(false)

    var articulo by mutableStateOf(Item(id=0,name="",0.0,0,"","","",""))

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }



    fun buscarBarra() {
        if (listaItemUiState.listaitemDetails.barra.isNotBlank()) {
            buscarItem()
            /*if (!existe) {
                GetArticuloRMH(this, itemsRepository)
            }*/
        }


    }


    private fun buscarItem() {
        Log.d("INVBAR", "Buscando Barra =" + listaItemUiState.listaitemDetails.barra)
        error = false
        viewModelScope.launch {
            launch {
                try {

                    articulo = itemsRepository.getItembyBarra(
                        listaItemUiState.listaitemDetails.barra.trim())
                        .catch { emit( Item(id = -1, name = "", 0.0, 0, "-1", "","","") )}
                        .filterNotNull()
                        .first()
                    if (articulo.sap.isNotBlank()) {
                        existe = true
                        listaItemUiState =
                            AgregarItemUiState(
                                listaitemDetails = ListaItemDetails(
                                    name = articulo.name,
                                    barra = articulo.barra,
                                    sap = articulo.sap, descrip = articulo.name,
                                    quantity = listaItemUiState.listaitemDetails.quantity
                                ), isEntryValid = true
                            )
                        Log.d("INVBAR", "Resultadp =" + articulo.name)
                    }

                } catch (e: Exception) {
                    Log.d("INVBAR", "Articulo No existe =" + e.message)

                } finally {
                    error = false
                }
            }
            if (!existe ) {

                listaItemUiState =
                    AgregarItemUiState(
                        listaitemDetails = ListaItemDetails(
                            barra = listaItemUiState.listaitemDetails.barra,
                            descrip = "ERROR: ARTICULO " + listaItemUiState.listaitemDetails.barra + " NO EXISTE"
                        ), isEntryValid = false
                    )
                mp.start()


            }

        }

    }

    fun updateUiState(itemDetails: ListaItemDetails) {
        Log.d("CONSUL",itemDetails.barra)
        listaItemUiState =
            AgregarItemUiState(listaitemDetails = itemDetails, isEntryValid = true)

        if (itemDetails.barra.length>7) {
            buscarItem()
        }
    }


}



