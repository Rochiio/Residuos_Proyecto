package repositories

import com.fasterxml.jackson.annotation.JsonProperty
import dto.ResiduosDTO
import kotlinx.serialization.Serializable

/**
 * Clase que contiene una lista de residuos dto, utilizado para el mappeo a JSON
 */
@Serializable
class ListaResiduosDTO(
    @JsonProperty("residuos")
    val residuos: List<ResiduosDTO>
){

}