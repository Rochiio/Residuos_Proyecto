package repositories

import dto.ResiduosDto

@kotlinx.serialization.Serializable
class ListaResiduosDto(
    val residuos: List<ResiduosDto>
){

}