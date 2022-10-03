package mappers

import dto.ContenedorDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import models.Contenedor
import models.TipoContenedor
import kotlinx.serialization.json.Json

import org.jetbrains.kotlinx.dataframe.io.JSON
import java.io.File
import java.nio.file.Path

object ContenedorMapper {

    /**
     * Convierte de ContenedorDTO a Contenedor
     * @param contenedorDto ContenedorDTO
     * @return Contenedor
     */
    fun fromDto(contenedorDto: ContenedorDTO): Contenedor {
        return Contenedor(
            contenedorDto.codigoSituacion,
            tipoContenedor = TipoContenedor.valueOf(contenedorDto.tipoContenedor),
            modeloContenedor = contenedorDto.modeloContenedor,
            descripcionModelo = contenedorDto.descripcionModelo,
            cantidad = contenedorDto.cantidad,
            lote = contenedorDto.lote,
            distrito = contenedorDto.distrito,
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
            contenedor.tipoVia,
            contenedor.nombreVia,
            contenedor.numeroVia,
            contenedor.coordenadas.first,
            contenedor.coordenadas.second,
            contenedor.coordenadasGeo.first,
            contenedor.coordenadasGeo.second
        )
    }

    /**
     *
     * @param ruta String
     * @return List<Contenedor>
     */
    fun readCSV(ruta: String): List<Contenedor?>? {
        val path = this::class.java.classLoader.getResource("datos/contenedores_varios.csv")
        val file = File(path.file)
        return if (checkCSV(file))
            file.readLines()
                .drop(1)
                .map { it.split(";") }
                .map { mapContenedor(it) }
        else null
    }

    /**
     * Comprueba si el archivo tiene la cabecera correcta y si tiene contenido
     * @param file File
     * @return Boolean
     */
    private fun checkCSV(file: File): Boolean {
        val head = file.readLines().take(1).first().split(";").size==16
        val lines = file.readLines().size > 1
        return head && lines
    }

    /**
     * Convierte el contenido de una lista de cadenas en un Contenedor
     * @param it List<String>
     * @return Contenedor
     */
    private fun mapContenedor(it: List<String>): Contenedor? {
        if(it.size != 16)
            return null
        else return Contenedor(
            it[0],
            TipoContenedor.valueOf(it[1].replace("-", "")),
            it[2],
            it[3],
            it[4].toIntOrNull() ?: -1,
            it[5].toIntOrNull() ?: -1,
            it[6],
            it[8],
            it[9],
            it[10].toIntOrNull() ?: -1,
            Pair(
                it[11].replace(",", ".").toDouble(),
                it[12].replace(",", ".").toDouble()
            ),
            Pair(
                it[13].replace(",", ".").toDouble(),
                it[14].replace(",", ".").toDouble()
            )

        )
    }


    /**
     * Escribe una lista de ContendorDto a un archivo Json en ruta
     * @param ruta String
     * @param contenedores List<ContenedorDTO>
     */
    fun toJson(ruta: String, contenedores: List<ContenedorDTO>) {
        val json = Json { prettyPrint = true }
        var fichero = File(ruta + File.separator + "fichero.json")

        if (!fichero.exists()) {
            File("fichero.json").createNewFile()
            fichero = File("fichero.json")
        }
        fichero.writeText(json.encodeToString(contenedores))

    }

    /**
     * Lee un Json en ruta y devuelve una lista de ContendorDTO
     * @param ruta String
     * @return List<ContenedorDTO>
     */
    fun fromJson(ruta: String): List<ContenedorDTO> {
        val json = Json { prettyPrint = true }
        val file = File(ruta)
        return json.decodeFromString(file.readText())
    }
}