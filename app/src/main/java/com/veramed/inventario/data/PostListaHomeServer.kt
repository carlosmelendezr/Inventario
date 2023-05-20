package com.veramed.inventario.data

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun PostListaHomeServer(listaItem:ListaItems) {

    var url = "http://192.168.15.150:8080/"
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

    val llamar: Call<ListaItems?>? = retrofitapi.postListaItem(listaItem)

    llamar!!.enqueue(object:Callback<ListaItems?> {

        override fun onResponse(llamar: Call<ListaItems?>?, response: Response<ListaItems?>) {
            //val model: ListaItems? = response.body()
            val id = response.code()
            val resp ="ID = "+id+
                "Response Code : " + response.code()

            Log.d("APIV",resp)
        }

        override fun onFailure(call: Call<ListaItems?>?, t: Throwable) {
            // we get error response from API.
            Log.e("APIV","Error found is : " + t.message)
        }
    })

}







