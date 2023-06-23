package com.veramed.inventario.data.api


import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaItems
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAPI {
    @POST("listaadd")
    fun
            postListaApi(@Body lista: Lista?): Call<Lista?>?

    @POST("listaitem")
    fun
        postListaItemApi(@Body listaItems: ListaItems?): Call<ListaItems?>?



}
