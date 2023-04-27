package com.veramed.inventario.data

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import com.veramed.inventario.R
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader


/*private fun loadJSONArray(context: Context): JSONArray?{

    val inputStream = context.resources.openRawResource(R.raw.maestro)

    BufferedReader(inputStream.reader()).use {
        return JSONArray(it.readText())
    }
}

suspend fun cargarMaestroArticulos(context: Context) {

    val dao = InventoryDatabase.getDatabase(context)?.itemDao()

    try {
        val articulos = loadJSONArray(context)
        if (articulos != null){
            for (i in 0 until articulos.length()){
                val item = articulos.getJSONObject(i)
                val sap = item.getString("sap")
                val barra = item.getString("barra")
                val descripcion = item.getString("descripcion")

                val art = Item(
                    sap=sap,barra=barra,name=descripcion,id = 0,price=0.0, quantity = 0
                )

                dao?.insert(art)
            }
        }
    }

    catch (e: JSONException) {
        Log.d("Maestro","Cargando Maestro: $e")
    }
}*/