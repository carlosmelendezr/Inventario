package com.veramed.inventario.ui.lista

import android.content.Context
import android.media.MediaPlayer
import android.text.TextUtils.substring
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veramed.inventario.R
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.ListaItemRepository
import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.data.Moveventos
import com.veramed.inventario.data.api.GetArticuloRMH
import com.veramed.inventario.data.api.PostMovEventos
import com.veramed.inventario.data.api.getListaIngreso
import com.veramed.util.convertLongToTime
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date


/**
 * View Model to validate and insert items in the Room database.
 */
class ListaIngresoSapViewModel(
    savedStateHandle: SavedStateHandle,
    context: Context
) : ViewModel() {

    private val mp: MediaPlayer = MediaPlayer.create(context, R.raw.error)
    var mRoomList = mutableStateListOf<ArticuloSap>()


    var isFirstComposition by mutableStateOf(true)
    private val hoy= convertLongToTime(Date().time)

    /**
     * Holds current item ui state
     */

    var docmat: String = checkNotNull(savedStateHandle[ListaIngresoSapDestination.docmat])
    var corregir by mutableStateOf(false)

    var itemCorregir by mutableStateOf(MovEventoDetalle())
    private var error by mutableStateOf(false)

    var listaArticulosSapUIState by mutableStateOf(ListaArticuloSapUiState() )

     init {
        if (isFirstComposition) {
            Log.d("SAP", " Iniciando... lectura")
            getListaIngreso(this, docmat)
            isFirstComposition = false
            docmat = ""
        }

     }

    fun buscaItem(idingreso:Int) {
        for (item in mRoomList) {

            if (item.id == idingreso) {

                itemCorregir = MovEventoDetalle(idingreso = item.id,
                    id=item.id, cantidad = item.cantidad.toString(),sap=item.Sap,
                    descrip=item.Descripcion)

            }
        }


    }

    fun guardaItemOk(idingreso:Int) {

        var nuevaLista : ArrayList<ArticuloSap> = ArrayList()

        for (item in mRoomList) {
            Log.d("SAPMOV", " Estatus Antes ${item.estatus} ${item.Sap}")

           if (item.id==idingreso) {
               PostMovEventos(
                   Moveventos(idingreso=item.id,cant=item.cantidad, idcausa = 0, fecha=hoy)
               )
               nuevaLista.add(item.copy(estatus = 1))

           } else {
               nuevaLista.add(item)
           }


        }
        mRoomList.clear()

        for (item in nuevaLista) {
            mRoomList.add(item)
        }

    }

    fun guardaItemError() {
        PostMovEventos(itemCorregir.toMovEvento(itemCorregir))
        corregir=false

        var nuevaLista : ArrayList<ArticuloSap> = ArrayList()
        for (item in  mRoomList) {
            if (itemCorregir.idingreso != item.id) {
                nuevaLista.add(item)
            }
        }

        mRoomList.clear()

        for (item in nuevaLista) {
            mRoomList.add(item)
        }

    }

    fun saveItem() {
        //corregir=false
    }

    fun actualizaUiState(item: MovEventoDetalle) {

        itemCorregir = MovEventoDetalle(idingreso = item.id,
            id=item.id, cantidad = item.cantidad,sap=item.sap,
            barra="",descrip=item.descrip, idcausa = item.idcausa, fecha=hoy)

    }

    fun corregirItem(item: ArticuloSap) {

        itemCorregir = MovEventoDetalle(idingreso = item.id,
            id=item.id, cantidad = item.cantidad.toString(),sap=item.Sap,
            barra="",descrip=item.Descripcion, fecha=hoy)

        corregir=true


    }




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}



data class ListaArticuloSapUiState(var itemList: List<ArticuloSap> = listOf())

data class MovEventoDetalle(
    val id: Int = 0,
    val idingreso:Int=0,
    val cantidad: String= "",
    val sap: String = "",
    val barra:String = "",
    var descrip:String = "",
    var idcausa:String = "",
    var fecha:String=""
)


fun MovEventoDetalle.toMovEvento(mov:MovEventoDetalle): Moveventos = Moveventos(
    id = id,
    idingreso= idingreso,
    cant = cantidad.toIntOrNull() ?: 0,
    idcausa = idcausa.substring(0,idcausa.indexOf("-")).toInt(),
    fecha = fecha

)

