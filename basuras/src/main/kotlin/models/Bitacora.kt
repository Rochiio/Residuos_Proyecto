package models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.time.LocalDateTime
import java.util.UUID

@Serializable
class Bitacora(
    @XmlElement(true)
    val opcionElegida: String,
    @XmlElement(true)
    val exito: Boolean,
    @XmlElement(true)
    val tiempo: Long,
) {
    @XmlElement(true)
    val id: String = UUID.randomUUID().toString()
    @XmlElement(true)
    val instante: String = LocalDateTime.now().toString()
}


