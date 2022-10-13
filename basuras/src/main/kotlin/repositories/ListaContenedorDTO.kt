package repositories

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable
import dto.ContenedorDTO


/**
 * Clase que contiene una lista de contenedor dto, utilizado para el mappeo a JSON
 */
@Serializable
data class ListaContenedorDTO(
    @JsonProperty("contenedores")
    val contenedores: List<ContenedorDTO>

)