package com.veramed.inventario.data.api


import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Lista
import com.veramed.inventario.data.ListaAPI
import com.veramed.inventario.data.ListaItems
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitAPI {
    @POST("listaadd")
    fun
            postListaApi(@Body listaAPI: ListaAPI?): Call<ListaAPI?>?

    @POST("listaitem")
    fun
        postListaItemApi(@Body listaItems: ListaItems?): Call<ListaItems?>?

    /*@GET("listaitemsap")
    fun
            Call<List<ArticuloSap?>>  getListaItemSapApi(@Param docmat:String?)*/

    @GET("listaitemsap")
    fun
         getListaItemSapApi(@Query("docmat") docmat: String): Call<List<ArticuloSap>>

}
