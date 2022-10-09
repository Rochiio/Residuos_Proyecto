package repositories

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import models.Bitacora
import java.io.File

/**
 * Lista de bitacoras para el mappeo a xml
 */
@Serializable
class ListaBitacora {
    private var listaBitacoras = mutableListOf<Bitacora>()

    /**
     * Añadir una bitacora a la lista
     * @param bitacora bitacora a añadir
     */
    fun addBitacora(bitacora: Bitacora){
        listaBitacoras.add(bitacora)
    }



    /**
     * Crear el xml de bitacora
     * @param path directorio destino de la bitacora
     */
    fun bitacoraXml(path:String){
        val xml = XML { indentString = "  " }
        val fichero = File(path+ File.separator+"bitacora.xml")
        fichero.writeText(xml.encodeToString(this))
    }
}