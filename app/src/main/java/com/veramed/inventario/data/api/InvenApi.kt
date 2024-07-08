package com.veramed.inventario.data.api


import com.veramed.inventario.data.Captura
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface InvenApi {

        @POST("putCaptura")
        fun
                postCaptura(@Body linea: Captura): Call<Captura>
 }
