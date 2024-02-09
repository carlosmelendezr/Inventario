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
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.ListaItemRepository
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.api.GetArticuloRMH
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaAgregarItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val listaitemsRepository: ListaItemRepository,
    context: Context
) : ViewModel() {

     private val mp: MediaPlayer = MediaPlayer.create( context, R.raw.error)

    /**
     * Holds current item ui state
     */

    var listaId: Int = checkNotNull(savedStateHandle[ListaAgregarItemDestination.itemIdArg])

    var listaItemUiState by mutableStateOf(AgregarItemUiState())
        private set

    private var existe by mutableStateOf(false)
    private var error by mutableStateOf(false)


    var listaArticulosUIState: StateFlow<ListaArticulosUiState> =
        listaitemsRepository.getItemLista(listaId).map { ListaArticulosUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListaArticulosUiState()
            )

    private var articulo by mutableStateOf(Item(id=0,name="",0.0,0,"","","",""))

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    fun updateUiState(itemDetails: ListaItemDetails, camara:Boolean=false) {
            listaItemUiState =
                AgregarItemUiState(listaitemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
            //if (itemDetails.barra.length>12) {
        if (camara) {
                buscarItem()
            }
        }

    fun buscarBarra() {
        if (listaItemUiState.listaitemDetails.barra.isNotBlank()) {
            buscarItem()
            if (!existe) {
                GetArticuloRMH(this, itemsRepository)
            }
        }


    }

        suspend fun saveItem() {
            if (validateInput()) {
                existe = false
                listaitemsRepository.insertItem(listaItemUiState.listaitemDetails.toItem(listaId))
                listaItemUiState =
                    AgregarItemUiState(listaitemDetails = ListaItemDetails(), isEntryValid =false)
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
             /*if (!existe ) {
                /Log.d("RMH","Buscando RMH")
                 val newItem = GetArticuloRMH(listaItemUiState.listaitemDetails.barra)
                 Log.d("RMH","Resultado "+newItem.sap)
                 if (newItem.sap.isNotBlank()) {
                     existe = true
                     listaItemUiState =
                         AgregarItemUiState(
                             listaitemDetails = ListaItemDetails(
                                 name = newItem.name,
                                 barra = newItem.barra,
                                 sap = newItem.sap, descrip = newItem.name,
                                 quantity = listaItemUiState.listaitemDetails.quantity
                             ), isEntryValid = true
                         )
                     itemsRepository.insertItem(newItem)

                 } else {
                     listaItemUiState =
                         AgregarItemUiState(
                             listaitemDetails = ListaItemDetails(
                                 barra = listaItemUiState.listaitemDetails.barra,
                                 descrip = "ERROR: ARTICULO " + listaItemUiState.listaitemDetails.barra + " NO EXISTE"
                             ), isEntryValid = false
                         )
                     mp.start()
                 }*/
        }

    }

        private fun validateInput(uiState: ListaItemDetails = listaItemUiState.listaitemDetails): Boolean {
            return with(uiState) {
                name.isNotBlank() && quantity.isNotBlank()
            }
        }

}

data class ListaArticulosUiState(val itemList: List<ListaItems> = listOf())

/**
 * Represents Ui State for an Item.
 */
data class AgregarItemUiState(
    val listaitemDetails: ListaItemDetails = ListaItemDetails(),
    val isEntryValid: Boolean = false

)

data class ListaItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val sap: String = "",
    val barra:String = "",
    var descrip:String = "",
    var lote:String = "",
    var fecvenc:String = ""
)

fun ListaItemDetails.toItem(listaId:Int): ListaItems = ListaItems(
    id = id,
    iditem = id,
    idlista = listaId,
    cant = quantity.toIntOrNull() ?: 0,
    barra = barra,
    sap = sap,
    descrip = descrip,
    lote = lote,
    fecvence = fecvenc,

)





