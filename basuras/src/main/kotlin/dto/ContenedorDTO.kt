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
    val tipoVia: String,
    val nombreVia: String,
    val numeroVia: Int,
    val coordX : Double,
    val coodY : Double,
    val longitud : Double,
    val latitud : Double,
    val direccion: String = tipoVia + nombreVia + numeroVia
)