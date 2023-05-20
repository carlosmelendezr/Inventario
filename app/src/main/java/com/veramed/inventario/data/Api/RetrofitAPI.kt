package com.veramed.inventario.data.Api


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAPI {


    @POST("lista")
    fun
            postListaApi(@Body listaApi: ListaApi?): Call<ListaApi?>?

    @POST("listaitem")
    fun
        postListaItemApi(@Body listaItemsApi: ListaItemsApi?): Call<ListaItemsApi?>?



}
