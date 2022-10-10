package utils

import kotlinx.serialization.Serializer
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlValue
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

@kotlinx.serialization.Serializable
class Bitacora(
    @XmlElement(true)
    val opcionElegida: String,
    @XmlElement(true)

    val exito: Boolean,
    @XmlElement(true)

    val tiempo: Long,
    @XmlElement(true)

    val path: String
) {
    @kotlinx.serialization.Serializable(with = UUIDSerializer::class)
    @XmlElement(true)

    val id:UUID = UUID.randomUUID()
    @kotlinx.serialization.Serializable(with = LocalDateTimeSerializer::class)
    @XmlElement(true)

    val instante: LocalDateTime = LocalDateTime.now()

    init {
        bitacoraXml()
    }

    /**
     * Crear el xml de bitacora
     */
    private fun bitacoraXml(){
        val xml = XML { indentString = "  " }
        if(!File("bitacora.xml").exists())
            File("bitacora.xml").createNewFile()
        var fichero : File = File("bitacora.xml")
        fichero.writeText(xml.encodeToString(this))
    }

}