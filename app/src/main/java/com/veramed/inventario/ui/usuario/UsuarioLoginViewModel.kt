package com.veramed.inventario.ui.usuario


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.UsuarioRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch



/**
 * View Model to validate and insert items in the Room database.
 */
class UsuarioLoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var usuarioUiState by mutableStateOf(UsuarioUiState())
        private set

    /**
     * Updates the [usuarioUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(usuarioDetails: UsuarioDetails) {
        usuarioUiState =
            UsuarioUiState(usuarioDetails = usuarioDetails, isEntryValid = validateInput(usuarioDetails))
    }

    suspend fun buscarUsuario() {

       if (validateInput()) {
            val idusr = usuarioUiState.usuarioDetails.toItem().id
            val pass = usuarioUiState.usuarioDetails.toItem().password
            viewModelScope.launch {
                usuarioUiState = usuarioRepository.getUsuario(idusr,pass)
                    .filterNotNull()
                    .first()
                    .toUsuarioUiState(true,existe=true)

            }


            }
        }


    private fun validateInput(uiState: UsuarioDetails = usuarioUiState.usuarioDetails): Boolean {
        return with(uiState) {
            id.isNotBlank() && password.isNotBlank()
        }

    }


}





