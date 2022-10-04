package dto

import kotlinx.serialization.Serializable


@Serializable

data class ContenedorDTO(
    val codigoSituacion: String,
    val tipoContenedor: String,
    val modeloContenedor: String,
    val descripcionModelo: String,
    val cantidad: Int,
    val lote: Int,
    val distrito: String,
    val barrio: String = "NO DISPONIBLE",
    val tipoVia: String,
    val nombreVia: String,
    val numeroVia: Int,
    val coordX: Double,
    val coodY: Double,
    val longitud: Double,
    val latitud: Double,
    val direccion: String
){
    fun toLine() : String{
        return "$codigoSituacion;$tipoContenedor;$modeloContenedor;$descripcionModelo;" +
                "$cantidad;" +
                "$lote;" +
                "$distrito;" +
                "$barrio;" +
                "$tipoVia;" +
                "$nombreVia;" +
                "$numeroVia;" +
                "$coordX;" +
                "$coodY;" +
                "$longitud;" +
                "$latitud;" +
                direccion
    }
}