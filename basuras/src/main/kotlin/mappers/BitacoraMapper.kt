package mappers

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import models.Bitacora
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import repositories.ListaBitacora
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class BitacoraMapper {
    private val DIR = System.getProperty("user.dir")+"${File.separator}bitacoras"
    private val FICHERO = DIR+"${File.separator}bitacora.xml"
    private lateinit var listaBitacoras: ListaBitacora
    private val logger = KotlinLogging.logger {}


    init {
        listaBitacoras = ListaBitacora(listOf())
        checkDirectory()
        getBitacora()
    }


    /**
     * Pasar listaBitacoras bitacora a un xml
     * @param directorio directorio donde debemos crear el xml
     * @param listaResiduosDto listaBitacoras de residuos para pasar a xml
     */
    fun toXml (directorio: String, lista: ListaBitacora){
        logger.info("Creando xml de bitacora")
        val xml = XML { indentString = "  " }
        val fichero = File(directorio + File.separator +  "bitacora.xml")
        fichero.writeText(xml.encodeToString(lista))
    }

    /**
     * Pasar xml a listaBitacoras bitacora
     * @param directorio directorio donde debemos coger el xml
     * @return listaBitacoras de residuos dto
     */
    fun fromXml(directorio: String):ListaBitacora{
        logger.info("Pasando xml a listaBitacoras de bitacora")
        val xml = XML {indentString = "  "}
        val fichero = File(directorio)
        return xml.decodeFromString<ListaBitacora>(fichero.readText())
    }


    /**
     * Para saber si existe el directorio necesario para la bitacora.
     */
    private fun checkDirectory(){
        if(Files.notExists(Path.of(DIR))){
            logger.info("Creando directorio bitacoras")
            Files.createDirectories(Path.of(DIR))
        }
    }


    /**
     * Si existe ya un fichero bitacora, cargarlo.
     */
    private fun getBitacora(){
        if (Files.exists(Path.of(FICHERO))){
            logger.info("Cargando fichero de bitacoras")
            listaBitacoras = fromXml(FICHERO)
        }
    }


    /**
     * Crear la bitacora, coger todos los datos y crear el xml
     */
    fun makeBitacora(bitacora: Bitacora){
        var lista = listaBitacoras.lista.toMutableList()
        lista.add(bitacora)
        listaBitacoras.lista=lista.toList()
        toXml(DIR,listaBitacoras)
    }
}