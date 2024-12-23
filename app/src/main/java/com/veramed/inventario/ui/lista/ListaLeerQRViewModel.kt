package com.veramed.inventario.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ListaLeerQRViewModel(
    savedStateHandle: SavedStateHandle) : ViewModel() {

    var codigoQRUiState by mutableStateOf(CodigoQR())


    fun updateUiState(codigoQR: CodigoQR) {
        codigoQRUiState =
            CodigoQR(texto = codigoQR.texto)


    }


}

data class CodigoQR (
    val texto:String =""

)

fun CodigoQR.retDocMat(texto:String):String {
    val list = texto.split(";")
    return list.get(3)
}