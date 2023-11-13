package com.veramed.inventario.data

import android.util.Log
import com.veramed.inventario.data.api.*
import com.veramed.inventario.ui.lista.ListaTransmitirViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*fun TransmitirLista(lista: Lista,listaItem: List<ListaItems>,
                     viewModel: ListaTransmitirViewModel) {

    PostListaHomeServer(lista,listaItem,viewModel)
    if (viewModel.idservidor>0) {
        listaItem.forEach {
            PostListaItems(it,viewModel)
        }
        viewModel.envioExitoso = true
    }


}*/

fun PostListaHomeServer(listaAPI: ListaAPI,listaItem: List<ListaItems>,
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

    val llamarLista: Call<ListaAPI?>? = retrofitapi.postListaApi(listaAPI)
        llamarLista!!.enqueue(object : Callback<ListaAPI?> {

            override fun onResponse(llamarLista: Call<ListaAPI?>?, response: Response<ListaAPI?>) {

                idCreado = response.body()?.id ?: 0

                val resp = "ID = " + idCreado +
                        "Response Code : " + response.code()

                Log.d("APIV", resp)

                viewModel.idservidor = idCreado

                if (idCreado>0) {
                    listaItem.forEach {
                        var itm = it
                        itm.idlista = idCreado
                        PostListaItems(itm,viewModel)
                    }
                    viewModel.envioExitoso = true
                    viewModel.idservidor = idCreado
                }


            }

            override fun onFailure(call: Call<ListaAPI?>?, t: Throwable) {
                // we get error response from API.
                Log.e("APIV", "Error found is : " + t.message)
                viewModel.envioExitoso=false

            }


        })

}

fun PostListaItems(listaItem: ListaItems,
                   viewModel: ListaTransmitirViewModel) {

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
                viewModel.itemcount++
                //Log.e("APIV", "Sumandi itemcount ${viewModel.itemcount}: " )

            }
            override fun onFailure(call: Call<ListaItems?>?, t: Throwable) {
                // we get error response from API.
                Log.e("APIV", "Error found is : " + t.message)
            }
        })


}







