package utils

import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

class Bitacora(
    val opcionElegida: String,
    val exito: Boolean,
    val tiempo: Long,
    val path: String
) {
    val id:UUID = UUID.randomUUID()
    val instante: String = Format.formatDate(LocalDateTime.now())

    init {
        bitacoraXml()
    }

    /**
     * Crear el xml de bitacora
     */
    private fun bitacoraXml(){
        val xml = XML { indentString = "  " }
        val fichero = File(path+File.separator+"bitacora.xml")
        fichero.writeText(xml.encodeToString(this))
    }

}