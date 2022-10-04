package dto

import kotlinx.serialization.Serializable

@Serializable
data class ResiduosDto(
    val año: Short,
    val mes: String,
    val lote: Byte,
    val residuo: String,
    val distrito: Byte,
    val nombreDistrito: String,
    val toneladas: Float
) {

    fun toLine() : String{
        return "$año;$mes;$lote;$residuo;$distrito;$nombreDistrito;$toneladas"
    }

}