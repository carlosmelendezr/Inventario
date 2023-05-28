package com.veramed.inventario.data

import android.util.Log
import com.veramed.inventario.data.Api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun PostListaHomeServer(lista: Lista,listaItem: ListaItems) {

    var url = "http://192.168.1.102:8090/"
    // on below line we are creating a retrofit
    // builder and passing our base url
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        // as we are sending data in json format so
        // we have to add Gson converter factory
        .addConverterFactory(GsonConverterFactory.create())

        // at last we are building our retrofit builder.
        .build()
    // below the line is to create an instance for our retrofit api class.

    var idCreado:Int = 0

    val retrofitapi = retrofit.create(RetrofitAPI::class.java)

    val listaApi:ListaApi = lista.toListaApi()
    val llamarLista: Call<ListaApi?>? = retrofitapi.postListaApi(listaApi)

    llamarLista!!.enqueue(object:Callback<ListaApi?> {

        override fun onResponse(llamarLista: Call<ListaApi?>?, response: Response<ListaApi?>) {

            //idCreado = response.body()
            val resp = "ID = " + idCreado +
                    "Response Code : " + response.code()

            Log.d("APIV", resp)
        }

        override fun onFailure(call: Call<ListaApi?>?, t: Throwable) {
            // we get error response from API.
            Log.e("APIV", "Error found is : " + t.message)
        }

    })

    if (idCreado>0) {

        var listaItemApi: ListaItemsApi = listaItem.toListaItemApi(idCreado = idCreado)
        val llamar: Call<ListaItemsApi?>? = retrofitapi.postListaItemApi(listaItemApi)

        llamar!!.enqueue(object : Callback<ListaItemsApi?> {

            override fun onResponse(
                llamar: Call<ListaItemsApi?>?,
                response: Response<ListaItemsApi?>
            ) {

                val id = response.code()
                val resp = "ID = " + id +
                        "Response Code : " + response.code()

                Log.d("APIV", resp)
            }

            override fun onFailure(call: Call<ListaItemsApi?>?, t: Throwable) {
                // we get error response from API.
                Log.e("APIV", "Error found is : " + t.message)
            }
        })
    }

}







