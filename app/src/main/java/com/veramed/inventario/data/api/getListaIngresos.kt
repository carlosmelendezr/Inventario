package com.veramed.inventario.data.api

import android.util.Log
import com.google.gson.JsonObject
import com.veramed.inventario.data.ArticuloSap
import com.veramed.inventario.data.Articulos
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.Table
import com.veramed.inventario.ui.lista.AgregarItemUiState
import com.veramed.inventario.ui.lista.ListaAgregarItemViewModel
import com.veramed.inventario.ui.lista.ListaIngresoSapViewModel
import com.veramed.inventario.ui.lista.ListaItemDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun getListaIngreso():List<ArticuloSap> {

    var articulos: List<ArticuloSap> = listOf()

    var url = "http://192.10.47.88:8090/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val retrofitapi = retrofit.create(RetrofitAPI::class.java)

    val llamar: Call<List<ArticuloSap>> = retrofitapi.getListaItemSapApi("4916380978")


    llamar!!.enqueue(object : Callback<List<ArticuloSap>?> {

        override fun onResponse(
            call: Call<List<ArticuloSap>?>,
            response: Response<List<ArticuloSap>?>
        ) {
            if (!response.isSuccessful) {
                Log.d("RMH", "Codigo de Respuesta " + response.code())
                //Log.d("RMH", response.body().toString()+" url = "+llamar.request().toString())
            } else {
                Log.d("RMH", response.body().toString() + " url = " + llamar.request().toString())
                //Log.d("RMH", response.body())
            }

            articulos = response.body()!!

            if (!articulos!!.isEmpty()) {
                for (articuloSap in articulos!!) {

                    Log.d("Ingreso", "Sap = ${articuloSap.Sap}")
                    Log.d("Ingreso", "Descripcion = ${articuloSap.Descripcion}")
                    Log.d("Ingreso", "Cantidad = ${articuloSap.cantidad}")


                }
            }


        }

        override fun onFailure(call: Call<List<ArticuloSap>?>, t: Throwable) {
            Log.e("APIV", "Error found is : " + t.message)
        }
    })
    return articulos
}