package com.veramed.inventario.data.api

import com.google.gson.JsonObject
import com.veramed.inventario.data.Articulos
import com.veramed.inventario.data.Table
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query

interface RMHApi {

    @GET("GetArticuloConsultaPrecioMovil")
    fun getArticulo(@Query("CodigoBarra") barra:String): Call<Articulos>
    //fun getArticulo(@Query("CodigoBarra") barra:String): Call<JsonObject>
}