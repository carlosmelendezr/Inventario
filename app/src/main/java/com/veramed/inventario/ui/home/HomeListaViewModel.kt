package com.veramed.inventario.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.veramed.inventario.data.*
import com.veramed.inventario.ui.lista.toItem
import com.veramed.util.convertLongToTime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * View Model to retrieve all items in the Room database.
 */
class HomeListaViewModel(
    private val  listaRepository: ListaRepository,
    val sesion: Sesion) : ViewModel() {

    val listaUiState2 = MutableLiveData<List<ListaUiState>>()

    //val listaUiState = StateFlow<MutableList<ListaUiState>>(mutableListOf())

    val listaUiState: StateFlow<ListaUiState> =
            listaRepository.getAllListaStream().map { ListaUiState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ListaUiState()
                )





    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun deleteLista(lista:Lista) {
        viewModelScope.launch {
            listaRepository.deleteLista(lista)
        }

    }

    fun liberarLista(lista:Lista) {
        viewModelScope.launch {
            listaRepository.updateLista(lista)
        }

    }

}




/**
 * Ui State for HomeScreen
 */
data class ListaUiState(val listas: List<Lista> = listOf())


