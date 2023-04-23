package com.veramed.inventario.ui.lista


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.ui.item.ItemEditDestination
import com.veramed.inventario.ui.item.toItemUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ListaItemDestination.itemIdArg])

    /*init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }*/


        /**
         * Updates the [itemUiState] with the value provided in the argument. This method also triggers
         * a validation for input values.
         */
        fun updateUiState(itemDetails: ItemDetails) {
            itemUiState =
                ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
        }

        /**
         * Inserts an [Item] in the Room database
         */
        suspend fun saveItem() {
            if (validateInput()) {
                itemsRepository.insertItem(itemUiState.itemDetails.toItem())
            }
        }

        private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
            return with(uiState) {
                name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
            }
        }

}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val sap: String = "",
    val barra:String = ""
)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    barra = barra,
    sap = sap
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
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    barra = barra,
    sap = sap
)

