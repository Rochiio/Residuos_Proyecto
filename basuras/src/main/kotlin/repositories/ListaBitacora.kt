package repositories

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import models.Bitacora

@Serializable
data class ListaBitacora(
    @JsonProperty("bitacoras")
    var lista: List<Bitacora>
){
}