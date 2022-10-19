package controllers

import dto.ContenedorDTO
import dto.ResiduosDTO
import exceptions.FileFormatException
import exceptions.InputFormatException
import repositories.ListaResiduosDTO
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import mu.KotlinLogging
import repositories.ListaContenedorDTO
import utils.html.HtmlDirectory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.Collator

object BasuraController {
    const val CABECERARESIDUOS = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    const val CABECERACONTENEDOR =
        "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" + "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" + "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

    val contenedorMapper: ContenedorMapper = ContenedorMapper()
    val residuosMapper: ResiduosMapper = ResiduosMapper()


    private val logger = KotlinLogging.logger {}

    private lateinit var dataFrameController: DataframeController


    /**
     * Ejecuta el comando introducido como argumento y devuelve true si tiene exito, false si falla
     * @param args Array<String>
     * @return Boolean
     */
    fun executeCommand(args: Array<String>) : Boolean{
        var salida : Boolean
        when (getOption(args)) {
            1 -> salida = parser(args[1], args[2])
            2 -> salida = resumen(args[1], args[2], "")
            3 -> {
                val distritoPartes = args.dropLast(2).drop(1)
                val distrito = distritoPartes.joinToString("").replace("[", "").replace("]","")
                salida = resumen(args.takeLast(2)[0],args.takeLast(2)[1], distrito)
            }
            else -> {
                println("Saliendo del programa")
                return false
            }
        }
    return salida
    }


    /**
     * Lee todos los csv en origen y los escribe en destino como csv, xml y json
     * @param origen String
     * @param destino String
     */
    fun parser(origen: String, destino: String): Boolean {

        logger.info("Ejecutando parseo")
        if (!checkPath(origen)) {
            println("Ruta origen incorrecta. Por favor revisa que el directorio exista y esté bien escrito")
            return false
        } else {
            if(!File(destino).exists()){
                Files.createDirectories(Path.of(destino))
            }
            val list = File(origen).listFiles()
            if (list != null) {
                if (list.isNotEmpty()) {
                    val csv = mutableListOf<File>()
                    for (file in list) {
                        if (file.path.endsWith(".csv")) {
                            csv.add(file)
                        }
                    }
                    if (csv.size != 0) {
                        for (file in csv) {
                            if (file.readLines().size <= 1) {
                                println("Archivo ${file.path} no tiene contenido")
                            } else {
                                println("Leyendo ${file.path}...")
                                if (cabeceraContenedores(file.readLines().first())) {
                                    println("Leído csv de contenedores...")
                                    val contenedores = readContenedoresCsv(file)
                                    println("Escribiendo XML...")
                                    contenedorMapper.toXML(
                                        "$destino${File.separator}contenedores_parsed.xml",
                                        ListaContenedorDTO(contenedores)
                                    )
                                    println("Escribiendo JSON...")
                                    contenedorMapper.toJson(
                                        "$destino${File.separator}contenedores_parsed.json",
                                        ListaContenedorDTO(contenedores)
                                    )
                                    println("Escribiendo CSV...")
                                    contenedorMapper.writeCsv(
                                        contenedores,
                                        "$destino${File.separator}contenedores_parsed.csv"
                                    )

                                } else if (cabeceraResiduos(file.readLines().first())) {
                                    println("Leído csv de residuos...")

                                    val residuos = readResiduosCsv(file)
                                    println("Escribiendo XML...")
                                    residuosMapper.toXml(
                                        "$destino${File.separator}residuos_parsed.xml",
                                        ListaResiduosDTO(residuos)
                                    )
                                    println("Escribiendo JSON...")
                                    residuosMapper.toJson(
                                        "$destino${File.separator}residuos_parsed.json",
                                        ListaResiduosDTO(residuos)
                                    )
                                    println("Escribiendo CSV...")
                                    residuosMapper.writeCsvResiduo(
                                        ListaResiduosDTO(residuos),
                                        "$destino${File.separator}residuos_parsed.csv"
                                    )
                                } else {
                                    println("La cabecera no coincide con residuos o con contenedores.")
                                    return false
                                }
                            }
                        }
                    } else
                        return false
                } else
                    return false
            }else return false

        }
        return true
    }

    /**
     * Comprueba la cabecera del archivo csv con la cabecera necesaria para residuos
     * @param s String
     * @return Boolean
     */
    fun cabeceraResiduos(s: String): Boolean {
        val line = s.replace("\uFEFF", "")
        return (line == CABECERARESIDUOS)
    }
    /**
     * Comprueba la cabecera del archivo csv con la cabecera necesaria para contenedores
     * @param s String
     * @return Boolean
     */
    fun cabeceraContenedores(s: String): Boolean {
        val line = s.replace("\uFEFF", "")
        return (line == CABECERACONTENEDOR)
    }

    /**
     * Lee el csv en file y devuelve una lista de ContenedorDTO
     * @param file File
     * @return List<ContenedorDTO>
     */
    fun readContenedoresCsv(file: File): List<ContenedorDTO> {
        return contenedorMapper.readCSV(file.path)
    }

    /**
     * Lee el csv en file y devuelve una lista de ResiduosDTO
     * @param file File
     * @return List<ContenedorDTO>
     */
    fun readResiduosCsv(file: File): List<ResiduosDTO> {
        return residuosMapper.readCsvResiduo(file.path)!!
    }

    /**
     * Realiza las consultas necesarias para contenedores y residuos leídos en origen. Devuelve true si tiene exito y false si falla
     * @param origen String
     * @param destino String
     * @param distrito String
     * @return Boolean
     */
    fun resumen(origen: String, destino: String, distrito: String): Boolean {
        var contenedores: List<ContenedorDTO> = mutableListOf()
        var residuos: List<ResiduosDTO> = mutableListOf()
logger.info("Ejecutando resumen")
        //Lectura de archivos
        if (checkPath(origen)) {
            if(!File(destino).exists())
                Files.createDirectories(Path.of(destino))
            //primero busca csvs
            val csvs = retrieveCsv(origen)
            if (csvs.isNotEmpty()) {
                logger.info("Leyendo archivos csv: $csvs")
                for (f in csvs) {
                    val file = File(origen + File.separator + f.path)

                    if (cabeceraResiduos(file.readLines().first())) {
                        residuos = residuosMapper.readCsvResiduo(file.path)!!
                    } else if (cabeceraContenedores(file.readLines().first())) {
                        contenedores = contenedorMapper.readCSV(file.path)
                    }
                }
            }

            //ahora busca jsons
            if (contenedores.isEmpty()) {
                logger.info("Leyendo contenedores desde JSon")
                val json = retrieveJson(origen)
                if (json.isNotEmpty()) {
                    for (f in json) {
                        if (f.readLines().drop(1).contains("contenedores")) {
                            contenedores = contenedorMapper.fromJson(f.path).contenedores
                        }
                    }
                }
            }
            if (residuos.isEmpty()) {
                logger.info("Leyendo residuos desde JSon")
                val json = retrieveJson(origen)
                if (json.isNotEmpty()) {
                    for (f in json) {
                        if (f.readLines().drop(1).contains("residuos")) {
                            try {
                                residuos = residuosMapper.fromJson(f.path).residuos
                            } catch (e: FileFormatException) {
                                println(e.message)
                            }
                        }
                    }
                }
            }

            //ahora busca XML
            if (contenedores.isEmpty()) {
                logger.info("Leyendo contenedores desde XML")
                val xml = retrieveXml(origen)
                if (xml.isNotEmpty()) {
                    for (f in xml) {
                        if (f.readLines().first().contains("ListaContenedorDTO")) {
                            contenedores = contenedorMapper.fromXML(f.path).contenedores
                        }
                    }
                }
            }
            if (residuos.isEmpty()) {
                logger.info("Leyendo residuos desde XML")
                val xml = retrieveXml(origen)
                if (xml.isNotEmpty()) {
                    for (f in xml) {
                        if (f.readLines().first().contains("ListaResiduosDto")) {
                            residuos = residuosMapper.fromXml(f.path)
                        }
                    }
                }
            }
        } else {
            logger.info("La ruta de destino o de origen no son correctas")
            return false
        }

        if (residuos.isNotEmpty() && contenedores.isNotEmpty()) {

            val collator = Collator.getInstance()
            collator.strength = 0
            val listContenedores = contenedorMapper.mapListFromDTO(contenedores)
            val listResiduos = residuosMapper.mapListFromDTO(residuos)
            dataFrameController = DataframeController(
                listContenedores, listResiduos
            )
            if (distrito == "") {
                logger.info("Generando resumen en $destino")

                HtmlDirectory.copyHtmlDataResumen(dataFrameController.resumen(), destino)
            } else {
                if (listResiduos.any { collator.compare(distrito, it.nombreDistrito) == 0 }
                    && listContenedores.any { collator.compare(distrito, it.distrito) == 0 }
                ) {
                    logger.info("Generando resumen para $distrito en $destino")
                    HtmlDirectory.copyHtmlDataResumen(dataFrameController.resumenDistrito(distrito), destino)
                } else {
                    println("No existe el distrito $distrito")
                    return false
                }
            }
            return true
        }
        return false
    }

    /**
     * Comprueba el comando recibido y la validez de los argumentos de entrada
     * @param args Array<String>
     * @return Int
     */
    fun getOption(args: Array<String>): Int {
        var opt = -1
        if (args.size < 3 || args.size > 7) {

            logger.info("El comando introducido no tiene los campos necesarios")
            println("El formato del comando es el incorrecto")

        }
        if (args.size == 3) {
            logger.info("Leyendo comando")
            opt = when (args[0]) {
                "parser" -> 1
                "resumen" -> 2
                else -> -1
            }
        } else {
            if (args[0].lowercase() == "resumen") {
                return 3
            }
        }
        return opt
    }

    /**
     * Comprueba que el path existe y es un directorio
     * @param path String
     * @return Boolean
     */
    fun checkPath(path: String): Boolean {
        return File(path).exists() && File(path).isDirectory
    }

    /**
     * Lista los archivos csv en directory y genera una lista con ellos
     * @param directory String
     * @return List<File>
     */
    private fun retrieveCsv(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)

            file.list()?.forEach { file -> if (file.endsWith(".csv")) list.add(File(file)) }
        }
        return list
    }

    /**
     * Lista los archivos json en directory y genera una lista con ellos
     * @param directory String
     * @return List<File>
     */
    fun retrieveJson(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)
            file.list()?.forEach { file -> if (file.endsWith(".json")) list.add(File(file)) }
        }
        return list
    }

    /**
     * Lista los archivos xml en directory y genera una lista con ellos
     * @param directory String
     * @return List<File>
     */
    fun retrieveXml(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)
            file.list()?.forEach { file -> if (file.endsWith(".xml")) list.add(File(file)) }
        }
        return list
    }
}