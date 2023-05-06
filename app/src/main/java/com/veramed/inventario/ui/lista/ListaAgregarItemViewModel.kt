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
import com.veramed.inventario.ui.home.HomeUiState
import com.veramed.inventario.ui.item.toItemUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaAgregarItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,private val listaitemsRepository: ListaItemRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var listaItemUiState by mutableStateOf(AgregarItemUiState())
        private set

    var listaArticulosUIState: StateFlow<ListaArticulosUiState> =
        listaitemsRepository.getItemLista(1).map { ListaArticulosUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListaArticulosUiState()
            )

    var articulo by mutableStateOf(Item(id=0,name="",0.0,0,"",""))



    private val itemId: Int = checkNotNull(savedStateHandle[ListaAgregarItemDestination.itemIdArg])

    init {

/*        viewModelScope.launch {
             itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()

        }*/


      }
        companion object {
            private const val TIMEOUT_MILLIS = 5_000L
        }



        /**
         * Updates the [itemUiState] with the value provided in the argument. This method also triggers
         * a validation for input values.
         */
        fun updateUiState(itemDetails: ListaItemDetails) {
            listaItemUiState =
                AgregarItemUiState(listaitemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
            if (itemDetails.barra.length>7) {
                buscarItem()
            }
        }

        /**
         * Inserts an [Item] in the Room database
         */
        suspend fun saveItem() {
            Log.d("INVBAR","Intentando guardar")
            if (validateInput()) {
                listaitemsRepository.insertItem(listaItemUiState.listaitemDetails.toItem())
                listaItemUiState =
                    AgregarItemUiState(listaitemDetails = ListaItemDetails(), isEntryValid =false)
                articulo = Item(barra="",name="",price=0.0,id=0, quantity = 0,sap="")
            }
        }

    fun buscarItem() {

        Log.d("INVBAR","Buscando barra "+listaItemUiState.listaitemDetails.barra)
        var barra = listaItemUiState.listaitemDetails.barra.trim()

        viewModelScope.launch {
            articulo = itemsRepository.getItembyBarra(barra)
                .filterNotNull()
                .first()

            if (articulo.name.isNotBlank()) {
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
            }

        }
        if (!listaItemUiState.isEntryValid){
            Log.d("INVBAR","BARRA NO EXISTE "+listaItemUiState.listaitemDetails.barra)
            listaItemUiState =
                AgregarItemUiState(
                    listaitemDetails = ListaItemDetails(
                        barra = listaItemUiState.listaitemDetails.barra,
                        descrip = "ARTICULO NO EXISTE"
                    ), isEntryValid = false
                )

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
    val descrip:String = ""
)




/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ListaItemDetails.toItem(): ListaItems = ListaItems(
    id = id,
    iditem = 1,
    idlista = 1,
    cant = quantity.toIntOrNull() ?: 0,
    barra = barra,
    sap = sap,
    descrip = descrip
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun ListaItemDetails.toListaItemUiState(isEntryValid: Boolean = false): AgregarItemUiState = AgregarItemUiState(
    listaitemDetails = this.toListaItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun ListaItemDetails.toListaItemDetails(): ListaItemDetails = ListaItemDetails(
    id = id,
    name = name,
    quantity = quantity.toString(),
    barra = barra,
    sap = sap
)



