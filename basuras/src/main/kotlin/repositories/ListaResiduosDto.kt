package repositories

import dto.ResiduosDto

@kotlinx.serialization.Serializable
class ListaResiduosDto(
    val lista: List<ResiduosDto>
){

}