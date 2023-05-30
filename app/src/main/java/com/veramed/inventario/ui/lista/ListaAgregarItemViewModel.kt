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


    fun updateUiState(itemDetails: ListaItemDetails) {
            listaItemUiState =
                AgregarItemUiState(listaitemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
            if (itemDetails.barra.length>7) {
                buscarItem()
            }
        }

    fun buscarBarra() {
            Log.d("INVBAR","Buscando barra")
            buscarItem()

    }

        /**
         * Inserts an [Item] in the Room database
         */
        suspend fun saveItem() {
            Log.d("INVBAR","Intentando guardar")

            if (validateInput()) {
                existe = false
                listaitemsRepository.insertItem(listaItemUiState.listaitemDetails.toItem(listaId))
                listaItemUiState =
                    AgregarItemUiState(listaitemDetails = ListaItemDetails(), isEntryValid =false)
            }
        }

    private fun buscarItem() {

        error = false
        Log.d("INVBAR","Buscando barra "+listaItemUiState.listaitemDetails.barra)
         viewModelScope.launch {
             launch {
             try {

                 articulo = itemsRepository.getItembyBarra(listaItemUiState.listaitemDetails.barra)
                     .catch { emit(Item(id = -1, name = "ERROR", 0.0, 0, "-1", "","",""))}
                     .onEmpty { Log.d("INVBAR", "La lista esta vacia") }
                     .filterNotNull()
                     .first()


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



                 Log.d("INVBAR", "Articulo SAP =" + articulo.sap)
             } catch (e: Exception) {
                 Log.d("INVBAR", "Articulo No existe =" + e.message)

             } finally {
                 Log.d("INVBAR", "Articulo No existe finally")
                 error = false
             }
         }


            /*if (articulo.name.isNotBlank()) {
                Log.d("INVBAR","BARRA EXISTE "+articulo.name)
                listaItemUiState =
                    AgregarItemUiState(
                        listaitemDetails = ListaItemDetails(
                            name = articulo.name,
                            barra = articulo.barra,
                            sap = articulo.sap, descrip = articulo.name,
                            quantity = listaItemUiState.listaitemDetails.quantity
                        ), isEntryValid = true
                    )

            }*/

             if (!existe ) {

                Log.d("INVBAR","BARRA NO EXISTE "+listaItemUiState.listaitemDetails.barra)
                listaItemUiState =
                    AgregarItemUiState(
                        listaitemDetails = ListaItemDetails(
                            barra = listaItemUiState.listaitemDetails.barra,
                            descrip = "ERROR: ARTICULO "+listaItemUiState.listaitemDetails.barra+" NO EXISTE"
                        ), isEntryValid = false
                    )
                mp.start()

            }

        }

       /* if (!existe && listaItemUiState.listaitemDetails.barra.isNotBlank()) {

            android.util.Log.d("INVBAR","BARRA NO EXISTE "+listaItemUiState.listaitemDetails.barra)
            listaItemUiState =
                AgregarItemUiState(
                    listaitemDetails = ListaItemDetails(
                        barra = listaItemUiState.listaitemDetails.barra,
                        descrip = "ERROR: ARTICULO "+listaItemUiState.listaitemDetails.barra+" NO EXISTE"
                    ), isEntryValid = false
                )
            mp.start()

        }*/



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

/*fun ListaItemDetails.toListaItemUiState(isEntryValid: Boolean = false): AgregarItemUiState = AgregarItemUiState(
    listaitemDetails = this.toListaItemDetails(),
    isEntryValid = isEntryValid
)*/

/*fun ListaItemDetails.toListaItemDetails(): ListaItemDetails = ListaItemDetails(
    id = id,
    name = name,
    quantity = quantity.toString(),
    barra = barra,
    sap = sap,
    lote = lote,
    fecvenc = fecvenc
)*/



