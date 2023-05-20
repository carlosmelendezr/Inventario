package com.veramed.inventario.data.Api

import com.veramed.inventario.data.ListaItems
import com.veramed.inventario.ui.lista.ListaItemDetails

data class ListaItemsApi(
    val idlista:Int,
    val iditem:Int,
    val sap:String,
    val barra:String,
    val descrip:String,
    val cant:Int
)




fun ListaItems.toListaItemApi(): ListaItemsApi = ListaItemsApi(
    idlista = idlista,
    iditem = iditem,
    descrip= descrip,
    barra = barra,
    sap = sap,
    cant=cant
)