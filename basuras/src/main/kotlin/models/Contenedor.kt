/*
Autor: Mohamed Asidah Bchiri
GitHub: loopedMoha
 */

package models

data class Contenedor(
    val codigoSituacion: String,
    val tipoContenedor: TipoContenedor,
    val modeloContenedor: String,
    val descripcionModelo: String,
    val cantidad: Int,
    val lote: Int,
    val distrito: String,
    val tipoVia: String,
    val nombreVia: String,
    val numeroVia: Int,
    val coordenadas: Pair<Double, Double>, //(X, Y)
    val coordenadasGeo: Pair<Double, Double> //(Longitud, latitud)
)

enum class TipoContenedor(val tipo: String) {
    ENVASES("ENVASES"),
    ORGANICA("ORGANICA"),
    PAPEL("PAPEL-CARTON"),
    RESTO("RESTO"),
    VIDRIO("VIDRIO")
}
