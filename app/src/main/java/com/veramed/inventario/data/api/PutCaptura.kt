package com.veramed.inventario.data.api

import android.util.Log
import com.google.gson.JsonObject
import com.veramed.inventario.data.Articulos
import com.veramed.inventario.data.Captura
import com.veramed.inventario.data.Item
import com.veramed.inventario.data.ItemsRepository
import com.veramed.inventario.data.Table
import com.veramed.inventario.ui.lista.AgregarItemUiState
import com.veramed.inventario.ui.lista.ListaAgregarItemViewModel
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


fun PutCaptura(viewModel: ListaAgregarItemViewModel, captura: Captura) {

    val url = "http://INV_MAIN/WebSite1/Service.asmx/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val inventarioapi = retrofit.create(InvenApi::class.java)


    val llamar: Call<Captura> = inventarioapi.postCaptura(captura)
    //Log.d("RMH", "Iniciando llamada "+viewModel.listaItemUiState.listaitemDetails.barra)


    llamar!!.enqueue(object : Callback<Captura> {

        override fun onResponse(
            llamar: Call<Articulos>,
            response: Response<Articulos>
        ) {
            if (!response.isSuccessful) {
                Log.d("INVEN", "Codigo de Respuesta "+response.code())
                Log.d("INVEN", response.body().toString()+" url = "+llamar.request().toString())
            } else {
                Log.d("INVEN", response.body().toString()+" url = "+llamar.request().toString())
            }

        }
        override fun onFailure(call: Call<Articulos>?, t: Throwable) {
            // we get error response from API.
            Log.e("APIV", "Error found is : " + t.message)
        }
    })

}