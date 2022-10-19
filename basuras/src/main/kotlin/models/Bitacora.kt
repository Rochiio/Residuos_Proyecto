package models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

/**
 * Clase para la creación de la bitacora
 */
@Serializable
class Bitacora(
    @XmlElement(true)
    private val opcionElegida: String,
    @XmlElement(true)
    private val exito: Boolean,
    @XmlElement(true)
    private val tiempo: Long,
) {
    @XmlElement(true)
    private val id: String = UUID.randomUUID().toString()
    @XmlElement(true)
    private val instante: String = LocalDateTime.now().toString()

}


