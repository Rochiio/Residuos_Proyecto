package mappers

import exceptions.CSVFormatException
import dto.ContenedorDTO

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Contenedor

import models.TipoContenedor
import mu.KotlinLogging

import nl.adaptivity.xmlutil.serialization.XML
import repositories.ListaContenedorDTO
import java.io.File


/**
 * Clase de mapeo de contenedores.
 */

class ContenedorMapper {
    val cabecera = "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" +
            "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" +
            "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

    private val logger = KotlinLogging.logger {}



    /**
     * Convierte de ContenedorDTO a Contenedor
     * @param contenedorDto ContenedorDTO
     * @return Contenedor
     */
    fun fromDto(contenedorDto: ContenedorDTO): Contenedor {

        logger.info("Convirtiendo contenedor DTO a contenedor")

        return Contenedor(
            contenedorDto.codigoSituacion,
            tipoContenedor = TipoContenedor.valueOf(contenedorDto.tipoContenedor.replace("-", "")),
            modeloContenedor = contenedorDto.modeloContenedor,
            descripcionModelo = contenedorDto.descripcionModelo,
            cantidad = contenedorDto.cantidad,
            lote = contenedorDto.lote,
            distrito = contenedorDto.distrito,
            "NO DISPONIBLE",
            tipoVia = contenedorDto.tipoVia,
            nombreVia = contenedorDto.nombreVia,
            numeroVia = contenedorDto.numeroVia,
            coordenadas = Pair(contenedorDto.coordX, contenedorDto.coodY),
            coordenadasGeo = Pair(contenedorDto.longitud, contenedorDto.latitud)

        )
    }



    /**
     * Convierte un Contenedor a un ContenedorDTO
     * @param contenedor Contenedor
     * @return ContenedorDTO
     */
    fun toDTO(contenedor: Contenedor): ContenedorDTO {

        logger.info("Convirtiendo contenedor a contenedor DTO")

        return ContenedorDTO(
            contenedor.codigoSituacion,
            contenedor.tipoContenedor.tipo,
            contenedor.modeloContenedor,
            contenedor.descripcionModelo,
            contenedor.cantidad,
            contenedor.lote,
            contenedor.distrito,
            "NO DISPONIBLE",
            contenedor.tipoVia,
            contenedor.nombreVia,
            contenedor.numeroVia,
            contenedor.coordenadas.first,
            contenedor.coordenadas.second,
            contenedor.coordenadasGeo.first,
            contenedor.coordenadasGeo.second,
            "${contenedor.tipoVia} ${contenedor.nombreVia} " +
                    "${if (contenedor.numeroVia == -1) "s/n" else contenedor.numeroVia}"
        )
    }




    /**
     * Lee un archivo CSV desde una ruta y devuelve una lista de DTO
     * @param ruta String
     * @return List<Contenedor>
     */
    fun readCSV(ruta: String): List<ContenedorDTO> {

        logger.info("Leyendo CSV")

        val file = File(ruta)
        if (!checkCSV(file))
            throw CSVFormatException()
        else
            return file.readLines()
                .drop(1)
                .map { it.split(";") }
                .map { mapContenedorDTO(it) }

    }




    /**
     * Comprueba si el archivo tiene la cabecera correcta y si tiene contenido
     * @param file File
     * @return Boolean
     */

    fun checkCSV(file: File): Boolean {
        logger.info("Comprobando si el csv es correcto")

        val head = file.readLines().take(1).first().split(";").size == 16
        val lines = file.readLines().size > 1
        return head && lines
    }


    /**
     * Convierte el contenido de una lista de cadenas en un Contenedor
     * @param it List<String>
     * @return Contenedor
     */
    private fun mapContenedorDTO(it: List<String>): ContenedorDTO {

        logger.info("Mappeando String a contenedor DTO")

        if (it.size != 16)
            throw CSVFormatException()
        else return ContenedorDTO(
            it[0],
            it[1],
            it[2],
            it[3],
            it[4].toIntOrNull() ?: -1,
            it[5].toIntOrNull() ?: -1,
            it[6],

            "NO DISPONIBLE",
            it[8],
            it[9],
            it[10].toIntOrNull() ?: -1,

            it[11].replace(",", ".").toDouble(),
            it[12].replace(",", ".").toDouble(),

            it[13].replace(",", ".").toDouble(),
            it[14].replace(",", ".").toDouble(),
            direccion = it[15]

        )
    }

    /**
     * Escribe la lista de contenedores en la ruta indicada
     */
    fun writeCsv(contendores: List<ContenedorDTO>, ruta: String) {
        logger.info("Escribiendo CSV")

        var destino = ruta
        if (!ruta.endsWith(".csv"))
            destino += "contenedores-procesado.csv"
        if (File(destino).createNewFile()) {
            val file = File(destino)
            file.writeText(cabecera+"\n")
            contendores.forEach { file.appendText(it.toLine()+"\n") }
        }

    }


    /**
     * Escribe una lista de ContendorDto a un archivo Json en ruta
     * @param ruta String
     * @param contenedores List<ContenedorDTO>
     */
    fun toJson(ruta: String, contenedores: ListaContenedorDTO) {

        logger.info("Creando JSON e3 lista contenedor DTO")

        val json = Json { prettyPrint = true }
        File(ruta).writeText(json.encodeToString(contenedores))
    }


    /**
     * Lee un Json en ruta y devuelve una lista de ContendorDTO
     * @param ruta String
     * @return List<ContenedorDTO>
     */
    fun fromJson(ruta: String): ListaContenedorDTO {

        logger.info("Conviertiendo JSON a lista contenedor DTO")

        val json = Json { prettyPrint = true }
        return Json.decodeFromString<ListaContenedorDTO>(File(ruta).readText())
    }



    /**
     * Escribe una lista de ContendorDto a un archivo Xml en ruta
     * @param ruta String
     * @param contenedores List<ContenedorDTO>
     */
    fun toXML(ruta: String, contenedores: ListaContenedorDTO) {
        logger.info("Creando Xml de lista contenedor DTO")
        val xml = XML{indentString = " "}
        val file = File(ruta)
        file.writeText(xml.encodeToString(contenedores))
    }



    /**
     * Lee un Xml en ruta y devuelve una lista de ContendorDTO
     * @param ruta String
     * @return List<ContenedorDTO>
     */
    fun fromXML(ruta: String): ListaContenedorDTO {
        logger.info("Conviertiendo XML a lista contenedor DTO")
        val file = File(ruta)
        val xml = XML { indentString = " " }
        return XML.decodeFromString(file.readText())
    }


    /**
     * Revisar si la ruta al csv es correcta
     * @param ruta ruta a revisar
     * @return si es correcta
     */
    fun checkRutaCSV(ruta: String): Boolean {
        logger.info("Revisando ruta termina en .csv")
        return ruta.endsWith(".csv")
    }


    /**
     * Mappear una lista de contenedores en una lista de contenedores DTO
     * @param contenedores lista de contenedores a mappear.
     * @return lista ya mappeada
     */
    fun mapListToDTO(contenedores: List<Contenedor>):List<ContenedorDTO>{

        logger.info("Mappear una lista de contenedores a una lista de contenedores DTO")

        return contenedores.map { toDTO(it) }.toList()
    }


    /**
     * Mappear una lista de contenedores DTO a una lista de contenedores
     * @param contenedores lista de DTO a mappear.
     * @return lista ya mappeada.
     */
    fun mapListFromDTO(contenedores: List<ContenedorDTO>):List<Contenedor>{

        logger.info("Mappear una lista de contenedores DTO a una lista de contenedores")

        return contenedores.map { fromDto(it) }.toList()
    }
}