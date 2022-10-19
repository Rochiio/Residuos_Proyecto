

package models

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

/**
 * Clase para la creación de contenedores.
 */

@DataSchema
data class Contenedor(
    val codigoSituacion: String,
    val tipoContenedor: TipoContenedor,
    val modeloContenedor: String,
    val descripcionModelo: String,
    val cantidad: Int,
    val lote: Int,
    val distrito: String,
    val barrio : String,
    val tipoVia: String,
    val nombreVia: String,
    val numeroVia: Int,
    val coordenadas: Pair<Double, Double>, //(X, Y)
    val coordenadasGeo: Pair<Double, Double> //(Longitud, latitud)
)



/**
 * Enum de tipos de contenedores.
 */

enum class TipoContenedor(val tipo: String) {
    ENVASES("ENVASES"),
    ORGANICA("ORGANICA"),
    PAPELCARTON("PAPEL-CARTON"),
    RESTO("RESTO"),
    VIDRIO("VIDRIO")
}
