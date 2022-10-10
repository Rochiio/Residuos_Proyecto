package mappers

import exceptions.CSVFormatException
import dto.ContenedorDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Contenedor
import models.TipoContenedor
import nl.adaptivity.xmlutil.serialization.XML
import repositories.ListaContenedorDTO
import java.io.File

object ContenedorMapper {
    val cabecera = "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" +
            "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" +
            "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

    /**
     * Convierte de ContenedorDTO a Contenedor
     * @param contenedorDto ContenedorDTO
     * @return Contenedor
     */
    fun fromDto(contenedorDto: ContenedorDTO): Contenedor {
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
        );
    }

    /**
     * Convierte un Contenedor a un ContenedorDTO
     * @param contenedor Contenedor
     * @return ContenedorDTO
     */
    fun toDTO(contenedor: Contenedor): ContenedorDTO {
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
    private fun checkCSV(file: File): Boolean {
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
        val json = Json { prettyPrint = true }
        File(ruta).writeText(json.encodeToString(contenedores))
    }

    /**
     * Lee un Json en ruta y devuelve una lista de ContendorDTO
     * @param ruta String
     * @return List<ContenedorDTO>
     */
    fun fromJson(ruta: String): ListaContenedorDTO {
        val json = Json { prettyPrint = true }
        return Json.decodeFromString<ListaContenedorDTO>(File(ruta).readText())
    }


    fun toXML(ruta: String, contenedores: ListaContenedorDTO) {
        val xml = XML{indentString = " "}
        val file = File(ruta)
        file.writeText(xml.encodeToString(contenedores))
    }

    fun fromXML(ruta: String): ListaContenedorDTO {
        val file = File(ruta)
        val xml= XML{indentString = " "}
        val contenedores = XML.decodeFromString<ListaContenedorDTO>(file.readText())
        return contenedores
    }

    fun checkRutaCSV(ruta: String): Boolean {
        return ruta.endsWith(".csv")
    }
}