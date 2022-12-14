package dto

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement


/**
 * Clase de Data Transfer Object de Residuos
 */

@Serializable
data class ResiduosDTO(
    @XmlElement(true)
    val año: Short,
    @XmlElement(true)
    val mes: String,
    @XmlElement(true)
    val lote: Byte,
    @XmlElement(true)
    val residuo: String,
    @XmlElement(true)
    val distrito: Byte,
    @XmlElement(true)
    val nombreDistrito: String,
    @XmlElement(true)
    val toneladas: Float
) {


    /**
     * Para añadirlo a un csv.
     * @return residuo en forma csv.
     */

    fun toLine() : String{
        return "$año;$mes;$lote;$residuo;$distrito;$nombreDistrito;$toneladas"
    }

}