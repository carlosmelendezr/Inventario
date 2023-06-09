package com.veramed.inventario.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaRepository
import com.veramed.inventario.data.Sesion
import com.veramed.inventario.data.SesionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaEntryViewModel(private val listaRepository: ListaRepository,
                          private val sesionRepository: SesionRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var listaUiState by mutableStateOf(ListaUiState())
        private set

    var sesion = Sesion(0,"",0)
    init {
        viewModelScope.launch {
            sesion = sesionRepository.getSesionActual().first()
        }
    }



    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(listaDetails: ListaDetalles) {
        listaUiState =
            ListaUiState(listaDetails = listaDetails, isEntryValid = validateInput(listaDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            listaRepository.insertLista(listaUiState.listaDetails.toItem(sesion.id))
        }
    }

    private fun validateInput(uiState: ListaDetalles = listaUiState.listaDetails): Boolean {
        return with(uiState) {
            descrip.isNotBlank() && tipo.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ListaUiState(
    val listaDetails: ListaDetalles=ListaDetalles(),
    val isEntryValid: Boolean = false,
    val expanded:Boolean = false
)

data class ListaDetalles(
    val id: Int = 0,
    val idusuario: Int = 0,
    val descrip: String = "",
    val color: Int = 0,
    val fecha:String = "",
    val feccrea: String = "",
    val tipo: String = "",
    val centro: String = ""

)


fun ListaDetalles.toItem(idusr:Int=idusuario): Lista = Lista(
    id = id,
    idusuario = idusr,
    descrip=descrip,
    color=color,
    feccrea=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(Date()),
    tipo=tipo.substring(0,tipo.indexOf("-")).toInt(),
    centro=centro.substring(0,centro.indexOf("-")).toInt(),
    fecha= Date().time,
    articulos = 0
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Lista.toListaUiState(isEntryValid: Boolean = false): ListaUiState = ListaUiState(
    listaDetails = this.toListaDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Lista.toListaDetails(): ListaDetalles = ListaDetalles(
    id = id,
    idusuario =idusuario,
    descrip=descrip,
    color=color,
    feccrea=feccrea,
    tipo=tipo.toString(),
    centro=centro.toString(),

)

