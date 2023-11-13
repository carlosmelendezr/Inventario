package com.veramed.inventario.ui.usuario


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.AppContainer
import com.veramed.inventario.data.Sesion
import com.veramed.inventario.data.SesionRepository
import com.veramed.inventario.data.UsuarioRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * View Model to validate and insert items in the Room database.
 */
class UsuarioLoginViewModel(private val usuarioRepository: UsuarioRepository,
                            private val sesionRepository: SesionRepository,
                            var appContainer: AppContainer
) : ViewModel() {

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
            UsuarioUiState(usuarioDetails = usuarioDetails, isEntryValid = validateInput())

    }

    fun buscarUsuario() {
            val idusr = usuarioUiState.usuarioDetails.toItem().id
            val pass = usuarioUiState.usuarioDetails.toItem().password

        runBlocking {usuarioUiState = usuarioRepository.getUsuario(idusr,pass)
            .filterNotNull()
            .first()
            .toUsuarioUiState(true,existe=true)
        }

       }


    private fun validateInput(): Boolean {
        return with(usuarioUiState.usuarioDetails) {
            id.isNotBlank() && password.isNotBlank()
        }
    }

    fun abrirSesion() {
        val sesion = Sesion(id=usuarioUiState.usuarioDetails.id.toInt(),
            name=usuarioUiState.usuarioDetails.name,
            nivel=0)

        appContainer.sesion = sesion

        runBlocking {
            sesionRepository.deleteAll()
            sesionRepository.insertSesion(sesion)
        }



    }


}





