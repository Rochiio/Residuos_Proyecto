package mappers

import exceptions.CSVFormatException
import dto.ResiduosDTO
import exceptions.FileFormatException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Residuos
import models.tipoResiduo
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import repositories.ListaResiduosDTO
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Clase de mapeo de Residuos
 */
class ResiduosMapper {
    val CABECERA = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"

    private val logger = KotlinLogging.logger {}


    /**
     * Pasar un residuoDto a residuo
     * @param residuoDto residuo en tipo dto
     * @return el residuo en tipo residuo
     */
    fun fromDto(residuoDto: ResiduosDTO): Residuos{
        logger.info("Pasando un residuoDto a Residuo")
        return Residuos(
            año = residuoDto.año,
            mes = residuoDto.mes,
            lote = residuoDto.lote,
            residuo = toTipoResiduo(residuoDto.residuo),
            distrito = residuoDto.distrito,
            nombreDistrito = residuoDto.nombreDistrito,
            toneladas = residuoDto.toneladas
        )
    }


    /**
     * Pasar un residuo a residuoDto
     * @param residuo en tipo residuo
     * @return el residuo en tipo residuoDto
     */
    fun toDto(residuo: Residuos): ResiduosDTO{
        logger.info("Pasando un residuo a residuoDTO")
        return ResiduosDTO(
            año = residuo.año,
            mes = residuo.mes,
            lote = residuo.lote,
            residuo = residuo.residuo.name,
            distrito = residuo.distrito,
            nombreDistrito = residuo.nombreDistrito,
            toneladas = residuo.toneladas
        )
    }


    /**
     * Comprueba si el archivo tiene la CABECERA correcta y si tiene contenido
     * @param file File
     * @return Boolean
     */
    fun checkCSV(file: File): Boolean {
        logger.info("Comprobando si el csv es correcto")
        val head = file.readLines().take(1).first().split(";").size == 7
        val lines = file.readLines().size > 1
        return head && lines
    }


    /**
     * pasar un csv a residuos DTO
     * @param directorio directorio donde se encuentra el fichero csv
     * @return lista de residuos DTO.
     */
    fun readCsvResiduo(directorio: String): List<ResiduosDTO> {
        logger.info("Leyendo csv a residuoDTO")
        val file = File(directorio)
        if (!checkCSV(file))
            throw CSVFormatException()
        else
            return Files.lines(Path.of(directorio))
                .skip(1)
                .map { mapToResiduo(it) }
                .toList()
    }


    /**
     * Escribe la lista de residuos en la ruta indicada
     * @param residuoLista lista de residuos a pasar al csv.
     * @param ruta directorio donde se va a almacenar el fichero csv.
     */
    fun writeCsvResiduo(residuoLista: ListaResiduosDTO, ruta: String) {
        logger.info("Escribiendo lista de residuos DTO a CSV")
        var destino = ruta
        if (!ruta.endsWith(".csv"))
            destino += File.separator + "residuos-procesado.csv"
        if (File(destino).createNewFile()) {
            val file = File(destino)
            file.writeText(CABECERA+"\n")
            residuoLista.residuos.forEach { file.appendText(it.toLine()+"\n") }
        }

    }


    /**
     * Pasar un string a residuo
     * @param line linea a mappear a residuo
     * @return residuo creado
     */
    private fun mapToResiduo(line:String):ResiduosDTO{
        logger.info("Mappeando linea a ResiduoDTO")
        val campos = line.split(";")
        return ResiduosDTO(
            año = campos[0].toShort(),
            mes = campos[1],
            lote = campos[2].toByte(),
            residuo = campos[3],
            distrito = campos[4].toByte(),
            nombreDistrito = campos[5],
            toneladas = campos[6].replace(",",".").toFloat()
        )
    }


    /**
     * Saber que tipo de residuo es
     * @param campo string a saber que tipo de residuo es
     * @return tipo de residuo
     */
    private fun toTipoResiduo(campo: String): tipoResiduo {
        logger.info("Buscando tipo de residuo")
        var tipo:tipoResiduo = tipoResiduo.PILAS
        when(campo){
            "RESTO" -> tipo =tipoResiduo.RESTO
            "ENVASES" -> tipo =tipoResiduo.ENVASES
            "VIDRIO" -> tipo =tipoResiduo.VIDRIO
            "VIDRIO COMERCIAL" -> tipo =tipoResiduo.VIDRIO_COMERCIAL
            "CLINICOS" -> tipo =tipoResiduo.CLINICOS
            "ORGANICA" -> tipo =tipoResiduo.ORGANICA
            "PAPEL-CARTON" -> tipo =tipoResiduo.PAPEL_CARTON
            "CARTON COMERCIAL" -> tipo =tipoResiduo.CARTON_COMERCIAL
            "RCD" -> tipo =tipoResiduo.RCD
            "PUNTOS LIMPIOS" -> tipo =tipoResiduo.PUNTOS_LIMPIOS
            "CONTENEDORES DE ROPA" -> tipo =tipoResiduo.CONTENEDORES_DE_ROPA
            "CAMA DE CABALLO" -> tipo =tipoResiduo.CAMA_DE_CABALLO
            "ANIMALES MUERTOS" -> tipo =tipoResiduo.ANIMALES_MUERTOS
            "PILAS" -> tipo =tipoResiduo.PILAS
        }
        return tipo
    }


    /**
     * Pasar residuoDto a un xml
     * @param directorio directorio donde debemos crear el xml
     * @param listaResiduosDto lista de residuos para pasar a xml
     */
    fun toXml (directorio: String, listaResiduosDto: ListaResiduosDTO){
        logger.info("Creando xml de lista de residuos DTO")
        val xml = XML { indentString = "  " }
        val fichero = File(directorio)
        fichero.writeText(xml.encodeToString(listaResiduosDto))
    }

    /**
     * Pasar xml a lista de residuos
     * @param directorio directorio donde debemos coger el xml
     * @return lista de residuos dto
     */
    fun fromXml(directorio: String):List<ResiduosDTO>{
        logger.info("Pasando xml a lista de residuos DTO")
        val xml = XML {indentString = "  "}
        val fichero = File(directorio)
        return xml.decodeFromString<List<ResiduosDTO>>(fichero.readText())
    }


    /**
     * Pasar residuoDto a un json
     * @param ruta ruta donde debemos crear el json
     * @param listaResiduosDto lista de residuos para pasar a json
     */
    fun toJson(ruta: String, listaResiduosDto: ListaResiduosDTO){
        logger.info("Creando JSON de lista de residuos DTO")
        val json = Json { prettyPrint = true }
        File(ruta).writeText(json.encodeToString(listaResiduosDto))
    }


    /**
     * Pasar json a lista de residuos
     * @param directorio directorio donde debemos coger el json
     * @return lista de residuos dto
     */
    fun fromJson(directorio: String):ListaResiduosDTO{
        logger.info("Pasando de JSON a una lista de residuos DTO")
        var fichero = File(directorio)

        if(fichero.exists() && fichero.endsWith(".json")){
            val json = Json { prettyPrint = true }
            return json.decodeFromString<ListaResiduosDTO>(File(directorio).readText())
        }
        throw FileFormatException()
    }


    /**
     * Mappear una lista de residuos en una lista de residuos DTO
     * @param residuos lista de residuos a mappear.
     * @return lista ya mappeada
     */
    fun mapListToDTO(residuos: List<Residuos>):List<ResiduosDTO>{
        logger.info("Mappeando una lista de residuos a una lista de residuos DTO")
        return residuos.map { toDto(it) }.toList()
    }


    /**
     * Mappear una lista de residuos DTO a una lista de residuos
     * @param residuosDto lista de DTO a mappear.
     * @return lista ya mappeada.
     */
    fun mapListFromDTO(residuosDto: List<ResiduosDTO>):List<Residuos>{
        logger.info("Mappeando una lista de residuos DTO a una lista de residuos")
        return residuosDto.map { fromDto(it) }.toList()
    }
}


