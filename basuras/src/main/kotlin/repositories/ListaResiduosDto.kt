package repositories

import dto.ResiduosDto
import kotlinx.serialization.Serializable

@Serializable
class ListaResiduosDto(
    val residuos: List<ResiduosDto>
){

}