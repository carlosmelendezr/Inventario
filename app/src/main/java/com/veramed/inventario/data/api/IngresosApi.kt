package com.veramed.inventario.data.api

import com.veramed.inventario.data.ArticuloSap
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query

interface IngresosApi {
    @GET("listaitemsap")
    fun getArticulo(@Query("docmat") docmat: String): Call<ArticuloSap>

}

