package com.veramed.inventario.data

import com.google.gson.annotations.SerializedName


/**
 * Entity data class represents a single row in the database.
 */
data class Table(
   @SerializedName("CodArticulo")
   val CodArticulo: String,
   @SerializedName("CodBarra")
   val CodBarra: String,
   @SerializedName("Bloqueado")
   val Bloqueado: Boolean,
   @SerializedName("Descripcion")
   val Descripcion: String,
   @SerializedName("PrecioBase")
   val PrecioBase: Double,
   @SerializedName("Iva")
   val Iva: Double,
   @SerializedName("PrecioIva")
   val PrecioIva: Double,
   @SerializedName("PrecioRef")
   val PrecioRef: Double,
   @SerializedName("Tasa")
   val Tasa: Double,
   @SerializedName("TasaEuro")
   val TasaEuro: Double,
   @SerializedName("NomProm")
   val NomProm: String,
   @SerializedName("PrecioBaseProm")
   val PrecioBaseProm: Double,
   @SerializedName("PrecioIVAProm")
   val PrecioIVAProm:Double



)
