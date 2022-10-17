package models

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

/**
 * Clase para la creación de residuos.
 */
@DataSchema
data class Residuos(
    val año: Short,
    val mes: String,
    val lote: Byte,
    val residuo: tipoResiduo,
    val distrito: Byte,
    val nombreDistrito: String,
    val toneladas: Float
) {
}