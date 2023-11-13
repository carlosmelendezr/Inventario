package com.veramed.inventario.ui.lista

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.data.*
import com.veramed.inventario.ui.item.ItemDetails
import com.veramed.inventario.ui.item.ItemUiState
import com.veramed.inventario.ui.item.toItemDetails
import com.veramed.inventario.ui.item.toItemUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ListaTransmitirViewModel(
    savedStateHandle: SavedStateHandle,
    private val listaRepository: ListaRepository,
    private val listaitemsRepository: ListaItemRepository
) : ViewModel() {

     /**
     * Holds current item ui state
     */

    var listaId: Int = checkNotNull(savedStateHandle[ListaAgregarItemDestination.itemIdArg])

    var envioExitoso by mutableStateOf(false)
    var idservidor by mutableStateOf(0)
    var itemcount by mutableStateOf(0)

    var listaTUiState by mutableStateOf(ListaTUiState())
        private set

    init {
        viewModelScope.launch {
            listaTUiState = listaRepository.getItemStream(listaId)
                .filterNotNull()
                .first()
                .toListaTUiState()

        }
    }

    var listaArticulosUIState: StateFlow<ListaArticulosUiState> =
        listaitemsRepository.getItemLista(listaId).map { ListaArticulosUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListaArticulosUiState()
            )

    suspend fun guardarLista() {
        Log.d("TRX","validando envio ${itemcount}/${listaArticulosUIState.value.itemList.size}")
        if (idservidor>0 && tranmisionOk()) {

            Log.d("TRX","datos OK intentando guardar, id=${idservidor}")
                listaRepository.updateLista(listaTUiState.toLista(color = 3, idservidor))
            envioExitoso = false
            idservidor = 0
            itemcount=0
            Log.d("TRX","datos limpios, id=${idservidor}")

        }

    }

    fun tranmisionOk():Boolean {
        return itemcount.equals(listaArticulosUIState.value.itemList.size)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}

data class ListaTUiState(
    val id:Int = 0,
    val idusuario:Int =0,
    val descrip:String="",
    val color:Int = 0,
    val fecha:Long=0,
    val feccrea:String="",
    val tipo:Int =0,
    val centro:Int=0,
    val articulos:Int = 0

)

fun ListaTUiState.toListaAPI(color:Int, idservidor:Int): ListaAPI = ListaAPI(
    id = 0,
    idusuario = idusuario,
    descrip = descrip,
    color = color,
    fecha = fecha,
    feccrea = feccrea,
    tipo = tipo,
    centro=centro
)

fun ListaTUiState.toLista(color:Int, idservidor:Int): Lista = Lista(
    id = id,
    idusuario = idusuario,
    descrip = descrip,
    color = color,
    fecha = fecha,
    feccrea = feccrea,
    tipo = tipo,
    articulos=articulos,
    centro=centro,
    idservidor=idservidor
)

fun Lista.toListaTUiState(): ListaTUiState = ListaTUiState(
    id = id,
    idusuario = idusuario,
    descrip = descrip,
    color = color,
    fecha = fecha,
    feccrea = feccrea,
    tipo = tipo,
    centro = centro,
    articulos= articulos?:0

)



