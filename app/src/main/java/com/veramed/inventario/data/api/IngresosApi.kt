package com.veramed.inventario.data.api

import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.ListaAPI
import com.veramed.inventario.data.Moveventos
import retrofit2.Call;
import retrofit2.http.Body
import retrofit2.http.GET;
import retrofit2.http.POST
import retrofit2.http.Query

interface IngresosApi {
    @GET("listaitemsap")
    fun getArticulo(@Query("docmat") docmat: String): Call<ArticuloSap>

    @POST("moveventos")
    fun
            postEventosApi(@Body moveventos: Moveventos?): Call<Moveventos?>?

}

