package repositories

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import dto.ContenedorDTO
@kotlinx.serialization.Serializable
data class ListaContenedorDTO(
    @JsonProperty("contenedores")
    val contenedores: List<ContenedorDTO>

)