package com.veramed.inventario.data.api

import android.util.Log
import com.google.gson.JsonObject
import com.veramed.inventario.data.Articulos
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


fun GetArticuloRMH(viewModel: ListaAgregarItemViewModel,  itemsRepository: ItemsRepository) {

    //var item = Item(id = -1, name = "", 0.0, 0, "-1", "","","")

    val url = "http://192.10.47.21/CONSDESC/posws.asmx/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val retrofitapi = retrofit.create(RMHApi::class.java)


    val llamar: Call<Articulos> = retrofitapi.getArticulo(viewModel.listaItemUiState.listaitemDetails.barra)
    //Log.d("RMH", "Iniciando llamada "+viewModel.listaItemUiState.listaitemDetails.barra)


    llamar!!.enqueue(object : Callback<Articulos> {

        override fun onResponse(
            llamar: Call<Articulos>,
            response: Response<Articulos>
        ) {
            if (!response.isSuccessful) {
                //Log.d("RMH", "Codigo de Respuesta "+response.code())
                //Log.d("RMH", response.body().toString()+" url = "+llamar.request().toString())
            } else {
                // Log.d("RMH", response.body().toString()+" url = "+llamar.request().toString())
                //Log.d("RMH", response.body())
            }

            val articulos: Articulos? = response.body()

            if (articulos != null) {
                //Log.d("RMH", "Descripcion = ${articulos.tables.first().Descripcion}")
                if (articulos.tables != null) {
                    for (table in articulos.tables ) {

                        //Log.d("RMH", "Articulos ${articulos.tables.size}")


                        val item = Item(id=0,
                            name=table.Descripcion,
                            barra=table.CodBarra.trimEnd(),price=0.0,
                            sap=table.CodArticulo.trimEnd(), quantity = 0, lote = "", fecvence = "")

                        GlobalScope.launch {
                            itemsRepository.insertItem(item)
                        }


                    }
                }

            }


        }
        override fun onFailure(call: Call<Articulos>?, t: Throwable) {
            // we get error response from API.
            Log.e("APIV", "Error found is : " + t.message)
        }
    })

}