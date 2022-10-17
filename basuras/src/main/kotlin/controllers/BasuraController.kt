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
     * Revisa cúal es el comando elegido.
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

        if (!checkPath(origen) || !checkPath(destino)) {
            println("Ruta destino o origen incorrectas. Por favor revisa que el directorio exista y esté bien escrito")
            return false
        } else {
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
                                return true
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

    fun cabeceraResiduos(s: String): Boolean {
        val line = s.replace("\uFEFF", "")
        return (line == CABECERARESIDUOS)
    }

    fun cabeceraContenedores(s: String): Boolean {
        val line = s.replace("\uFEFF", "")
        return (line == CABECERACONTENEDOR)
    }

    fun readContenedoresCsv(file: File): List<ContenedorDTO> {
        return contenedorMapper.readCSV(file.path)
    }

    fun readResiduosCsv(file: File): List<ResiduosDTO> {
        return residuosMapper.readCsvResiduo(file.path)!!
    }

    fun resumen(origen: String, destino: String, distrito: String): Boolean {
        var contenedores: List<ContenedorDTO> = mutableListOf()
        var residuos: List<ResiduosDTO> = mutableListOf()

        //Lectura de archivos
        if (checkPath(origen)) {
            if(!File(destino).exists())
                Files.createDirectories(Path.of(destino))
            //primero busca csvs
            val csvs = retrieveCsv(origen)
            if (csvs.isNotEmpty()) {
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
            println("La ruta de destino o de origen no son correctas")
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
                HtmlDirectory.copyHtmlDataResumen(dataFrameController.resumen(), destino)
            } else {
                if (listResiduos.any { collator.compare(distrito, it.nombreDistrito) == 0 }
                    && listContenedores.any { collator.compare(distrito, it.distrito) == 0 }
                ) {
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

    fun getOption(args: Array<String>): Int {
        var opt = -1
        if (args.size < 3 || args.size > 7) {
            println("El formato del comando es el incorrecto")
        }
        if (args.size == 3) {
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


    fun checkPath(path: String): Boolean {
        return File(path).exists() && File(path).isDirectory
    }


    private fun retrieveCsv(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)

            file.list()?.forEach { file -> if (file.endsWith(".csv")) list.add(File(file)) }
        }
        return list
    }

    fun retrieveJson(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)
            file.list()?.forEach { file -> if (file.endsWith(".json")) list.add(File(file)) }
        }
        return list
    }

    fun retrieveXml(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)
            file.list()?.forEach { file -> if (file.endsWith(".xml")) list.add(File(file)) }
        }
        return list
    }
}