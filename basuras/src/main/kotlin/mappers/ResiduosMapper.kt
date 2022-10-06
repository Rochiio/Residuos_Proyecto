package mappers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import dto.ResiduosDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Residuos
import models.tipoResiduo
import nl.adaptivity.xmlutil.serialization.XML
import repositories.ListaResiduosDto
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Clase de mapeo de Residuos
 * TODO pobrarlo con csv de prueba
 * TODO toJson jsonTo toXML xmlTo
 */
class ResiduosMapper {
    val CABECERA = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"


    /**
     * Pasar un residuoDto a residuo
     * @param residuoDto residuo en tipo dto
     * @return el residuo en tipo residuo
     */
    fun fromDto(residuoDto: ResiduosDto): Residuos {
        return Residuos(
            año = residuoDto.año,
            mes = residuoDto.mes,
            lote = residuoDto.lote,
            residuo = tipoResiduo.valueOf(residuoDto.residuo),
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
    fun toDto(residuo: Residuos): ResiduosDto {
        return ResiduosDto(
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
    private fun checkCSV(file: File): Boolean {
        val head = file.readLines().take(1).first().split(";").size == 7
        val lines = file.readLines().size > 1
        return head && lines
    }


    /**
     * pasar un csv a residuos
     * @param directorio directorio donde se encuentra el fichero csv
     * @return lista de residuos.
     */
    fun readCsvResiduo(directorio: String): List<ResiduosDto>? {
        val file = File(directorio)
        return if (checkCSV(file)) {
            Files.lines(Path.of(directorio))
                .skip(1)
                .map { mapToResiduo(it) }.toList()
        } else
            return null
    }


    /**
     * Escribe la lista de contenedores en la ruta indicada
     */
    fun writeCsvResiduo(residuoLista: ListaResiduosDto, ruta: String) {
        var destino = ruta
        File("$destino${File.separator}residuos_procesado.csv").createNewFile()

        val file = File("$destino${File.separator}residuos_procesado.csv")
        file.writeText(CABECERA+"\n")
        residuoLista.lista.forEach { file.appendText(it.toLine()+"\n") }


    }


    /**
     * Pasar un string a residuo
     * @param line linea a mappear a residuo
     * @return residuo creado
     */
    private fun mapToResiduo(line: String): ResiduosDto {
        val campos = line.split(";")
        return ResiduosDto(
            año = campos[0].toShort(),
            mes = campos[1],
            lote = campos[2].toByte(),
            residuo = campos[3],
            distrito = campos[4].toByte(),
            nombreDistrito = campos[5],
            toneladas = campos[6].replace(",", ".").toFloat()
        )
    }


    /**
     * Saber que tipo de residuo es
     * @param campo string a saber que tipo de residuo es
     * @return tipo de residuo
     */
    private fun toTipoResiduo(campo: String): tipoResiduo {
        var tipo: tipoResiduo = tipoResiduo.PILAS
        when (campo) {
            "RESTO" -> tipo = tipoResiduo.RESTO
            "ENVASES" -> tipo = tipoResiduo.ENVASES
            "VIDRIO" -> tipo = tipoResiduo.VIDRIO
            "VIDRIO COMERCIAL" -> tipo = tipoResiduo.VIDRIO_COMERCIAL
            "CLINICOS" -> tipo = tipoResiduo.CLINICOS
            "ORGANICA" -> tipo = tipoResiduo.ORGANICA
            "PAPEL-CARTON" -> tipo = tipoResiduo.PAPEL_CARTON
            "CARTON COMERCIAL" -> tipo = tipoResiduo.CARTON_COMERCIAL
            "RCD" -> tipo = tipoResiduo.RCD
            "PUNTOS LIMPIOS" -> tipo = tipoResiduo.PUNTOS_LIMPIOS
            "CONTENEDORES DE ROPA" -> tipo = tipoResiduo.CONTENEDORES_DE_ROPA
            "CAMA DE CABALLO" -> tipo = tipoResiduo.CAMA_DE_CABALLO
            "ANIMALES MUERTOS" -> tipo = tipoResiduo.ANIMALES_MUERTOS
            "PILAS" -> tipo = tipoResiduo.PILAS
        }
        return tipo
    }


    /**
     * Pasar residuoDto a un xml
     * @param directorio directorio donde debemos crear el xml
     * @param listaResiduosDto lista de residuos para pasar a xml
     */
    fun toXml(directorio: String, listaResiduosDto: ListaResiduosDto) {
        val xml = XML { indentString = "  " }
        val fichero = File(directorio + "${File.separator}residuos_xml.xml")
        fichero.writeText(xml.encodeToString(listaResiduosDto))
    }

    /**
     * Pasar xml a lista de residuos
     * @param directorio directorio donde debemos coger el xml
     * @return lista de residuos dto
     */
    fun fromXml(directorio: String): List<ResiduosDto> {
        val xml = XML { indentString = "  " }
        val fichero = File(directorio)
        return xml.decodeFromString<List<ResiduosDto>>(fichero.readText())
    }


    /**
     * Pasar residuoDto a un json
     * @param ruta ruta donde debemos crear el json
     * @param listaResiduosDto lista de residuos para pasar a json
     */
    fun toJson(ruta: String, listaResiduosDto: ListaResiduosDto) {
        val json = Json { prettyPrint = true }
        var fichero = File(ruta)

        if (!fichero.exists()) {
            File(ruta).createNewFile()
            fichero = File(ruta)
        }
        fichero.writeText(json.encodeToString(listaResiduosDto))
    }


    /**
     * Pasar json a lista de residuos
     * @param directorio directorio donde debemos coger el json
     * @return lista de residuos dto
     */
    fun fromJson(directorio: String): ListaResiduosDto? {
        val fichero = File(directorio)

        if (fichero.exists() && fichero.endsWith(".json")) {
            val json = Json { prettyPrint = true }
            return json.decodeFromString(fichero.readText())
        }
        return null
    }


}


