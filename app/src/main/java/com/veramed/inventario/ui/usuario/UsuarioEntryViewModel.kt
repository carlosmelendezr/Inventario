package com.veramed.inventario.ui.usuario

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.veramed.inventario.data.Usuario

import com.veramed.inventario.data.UsuarioRepository


/**
 * View Model to validate and insert items in the Room database.
 */
class UsuarioEntryViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var usuarioUiState by mutableStateOf(UsuarioUiState())
        private set

    /**
     * Updates the [usuarioUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: UsuarioDetails) {
        usuarioUiState =
            UsuarioUiState(usuarioDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            usuarioRepository.insertUsuario(usuarioUiState.usuarioDetails.toItem())
        }
    }

    private fun validateInput(uiState: UsuarioDetails = usuarioUiState.usuarioDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && id.isNotBlank() && password.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class UsuarioUiState(
    val usuarioDetails: UsuarioDetails = UsuarioDetails(),
    val isEntryValid: Boolean = false
)

data class UsuarioDetails(
    val id: String = "",
    val name: String = "",
    val nivel:Int=0,
    val password:String=""


)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun UsuarioDetails.toItem(): Usuario = Usuario(
    id = id.toInt(),
    name = name,
    nivel = nivel,
    password = password


)

/**
 * Eusuariotension function to convert [Item] to [ItemUiState]
 */
fun Usuario.toUsuarioUiState(isEntryValid: Boolean = false): UsuarioUiState = UsuarioUiState(
    usuarioDetails = this.toUsuarioDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Usuario.toUsuarioDetails(): UsuarioDetails = UsuarioDetails(
    id = id.toString(),
    name = name,
    nivel=nivel,
    password=password
)

