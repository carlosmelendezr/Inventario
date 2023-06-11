package com.veramed.inventario.data

import android.util.Log
import com.veramed.inventario.data.Api.*
import com.veramed.inventario.ui.lista.ListaTransmitirViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun PostListaHomeServer(lista: Lista,listaItem: List<ListaItems>,
                        viewModel: ListaTransmitirViewModel) {
    var exito = false
    var url = "http://192.10.47.88:8090/"
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

    var idCreado: Int

    val retrofitapi = retrofit.create(RetrofitAPI::class.java)

    val llamarLista: Call<Lista?>? = retrofitapi.postListaApi(lista)
        llamarLista!!.enqueue(object : Callback<Lista?> {

            override fun onResponse(llamarLista: Call<Lista?>?, response: Response<Lista?>) {

                idCreado = response.body()?.id ?: 0

                val resp = "ID = " + idCreado +
                        "Response Code : " + response.message()

                Log.d("APIV", resp)

                listaItem.forEach {
                    var itm = it
                    itm.idlista = idCreado
                    PostListaItems(itm)
                }
                viewModel.envioExitoso=true
                Log.e("APIV", "ENVIO EXITOSO : ")

            }

            override fun onFailure(call: Call<Lista?>?, t: Throwable) {
                // we get error response from API.
                Log.e("APIV", "Error found is : " + t.message)
                viewModel.envioExitoso=false

            }

        })

}

fun PostListaItems(listaItem: ListaItems) {

    var url = "http://192.10.47.88:8090/"
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

    val retrofitapi = retrofit.create(RetrofitAPI::class.java)
    val llamar: Call<ListaItems?>? = retrofitapi.postListaItemApi(listaItem)


    llamar!!.enqueue(object : Callback<ListaItems?> {

            override fun onResponse(
                llamar: Call<ListaItems?>?,
                response: Response<ListaItems?>
            ) {
                val id = response.code()

            }
            override fun onFailure(call: Call<ListaItems?>?, t: Throwable) {
                // we get error response from API.
                Log.e("APIV", "Error found is : " + t.message)
            }
        })


}







