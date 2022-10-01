package dto

import models.tipoResiduo


data class ResiduosDto(
    val año: Short,
    val mes: String,
    val lote: Byte,
    val residuo: String,
    val distrito: Byte,
    val nombreDistrito: String,
    val toneladas: Int
) {

}