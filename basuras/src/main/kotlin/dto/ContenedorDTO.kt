package dto

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Clase de Data Transfer Object de Contenedor
 */
@Serializable
data class ContenedorDTO(
    @XmlElement(true)
    val codigoSituacion: String,
    @XmlElement(true)
    val tipoContenedor: String,
    @XmlElement(true)
    val modeloContenedor: String,
    @XmlElement(true)
    val descripcionModelo: String,
    @XmlElement(true)
    val cantidad: Int,
    @XmlElement(true)
    val lote: Int,
    @XmlElement(true)
    val distrito: String,
    @XmlElement(true)
    val barrio: String = "NO DISPONIBLE",
    @XmlElement(true)
    val tipoVia: String,
    @XmlElement(true)
    val nombreVia: String,
    @XmlElement(true)
    val numeroVia: Int,
    @XmlElement(true)
    val coordX: Double,
    @XmlElement(true)
    val coodY: Double,
    @XmlElement(true)
    val longitud: Double,
    @XmlElement(true)
    val latitud: Double,
    @XmlElement(true)
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