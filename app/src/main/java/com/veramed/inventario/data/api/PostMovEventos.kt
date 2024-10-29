package com.veramed.inventario.data.api

import android.util.Log
import com.veramed.inventario.data.ListaAPI
import com.veramed.inventario.data.Moveventos
import com.veramed.inventario.ui.lista.ListaIngresoSapViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun PostMovEventos(moveventos: Moveventos,
                   viewModel: ListaIngresoSapViewModel
) {
    var exito = false
    var url = "http://192.10.47.88:8090/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var idCreado: Int

    val retrofitapi = retrofit.create(IngresosApi::class.java)

    val llamarLista: Call<Moveventos?>? = retrofitapi.postEventosApi(moveventos)

    llamarLista!!.enqueue(object : Callback<Moveventos?> {

        override fun onResponse(llamarLista: Call<Moveventos?>?, response: Response<Moveventos?>) {

            idCreado = response.body()?.id ?: 0

            val resp = "ID = " + idCreado +
                    "Response Code : " + response.code()

            Log.d("MOVEVENTOS", resp)

            //viewModel.idservidor = idCreado

            if (idCreado>0) {

                /*viewModel.envioExitoso = true
                viewModel.idservidor = idCreado*/
            }
        }

        override fun onFailure(call: Call<Moveventos?>?, t: Throwable) {
            // we get error response from API.
            Log.e("MOVEVENTOS", "Error found is : " + t.message)
            //viewModel.envioExitoso=false
        }

    })

}

