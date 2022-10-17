package controllers

import dto.ContenedorDTO
import dto.ResiduosDTO
import exceptions.CSVFormatException
import exceptions.FileFormatException
import exceptions.InputFormatException
import repositories.ListaResiduosDTO
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import repositories.ListaContenedorDTO
import utils.html.HtmlDirectory
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object BasuraController {
    private const val CABECERARESIDUOS = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    private const val CABECERACONTENEDOR =
        "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" + "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" + "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"
private val DISTRITOS = listOf<String>("arganzuela","barajas","carabanchel","centro","chamartín",
    "chamberí","ciudad lineal","fuencarral - el pardo","hortaleza","latina","moncloa - aravaca","moratalaz","puente de vallecas","retiro","salamanca",
            "san blas - canillejas","sin distrito","tetuán","usera","vicálvaro","villa de vallecas","villaverde")
    val contenedorMapper: ContenedorMapper = ContenedorMapper()
    val residuosMapper: ResiduosMapper = ResiduosMapper()

    fun executeCommand(args: Array<String>) {
        if (args.size < 3 || args.size > 7) {
            println("El formato del comando de entrada no es correcto. Comprueba que has introducido todos los campos obligatorios.")
        } else {
            when (getOption(args)) {
                1 -> parser(args[1], args[2])
                2 -> resumen(args[1], args[2], "")
                3 -> {
                    val distritoPartes = args.dropLast(2).drop(1)
                    var distrito = distritoPartes.toString().replace(",", "")
                    distrito = distrito.replace("[", "")
                    distrito = distrito.replace("]", "")
                    resumen(args.takeLast(2).take(1).toString()
                        .replace("[","")
                        .replace("]", ""),
                        args.last(), distrito)
                }
                else -> println("Saliendo del programa")
            }
        }
    }

    /**
     * Lee todos los csv en origen y los escribe en destino como csv, xml y json
     * @param origen String
     * @param destino String
     */
    fun parser(origen: String, destino: String) {
        var contenedores: List<ContenedorDTO> = emptyList()
        var residuos: List<ResiduosDTO> = emptyList()
        if (!checkPath(origen)) {
            println("Ruta origen incorrecta. Por favor revisa que el directorio exista y esté bien escrito")
        } else {
            if (!File(destino).exists()) {
                Files.createDirectories(Paths.get(destino))
            }
            if (File(destino).isDirectory) {
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
                                        contenedores = readContenedoresCsv(file)
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

                                        residuos = readResiduosCsv(file)
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
                                    }
                                }
                            }
                        }
                    } else {
                        println("No se han encontrado archivos csv para parsear en el directorio $origen")
                    }
                }
            }
        }
    }

    fun leerCsv() {

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
        return residuosMapper.readCsvResiduo(file.path)
    }

    fun resumen(origen: String, destino: String, distrito: String) {
        var contenedores: List<ContenedorDTO> = mutableListOf()
        var residuos: List<ResiduosDTO> = mutableListOf()

        if(distrito != "" && DISTRITOS.contains(distrito.lowercase())) {
            //Lectura de archivos
            if (checkPath(origen)) {
                if (!File(destino).exists()) {
                    Files.createDirectories(Paths.get(destino))
                }
                //primero busca csvs
                val csvs = retrieveCsv(origen)
                if (csvs.isNotEmpty()) {
                    for (f in csvs) {
                        val file = File(origen + File.separator + f.path)

                        if (cabeceraResiduos(file.readLines().first())) {
                            try {
                                residuos = residuosMapper.readCsvResiduo(file.path)
                            } catch (e: FileFormatException) {
                                println(e.message)
                            }

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
            }

            if (residuos.isNotEmpty() && contenedores.isNotEmpty()) {
                val dataframeController: DataframeController = DataframeController(
                    contenedorMapper.mapListFromDTO(contenedores),
                    residuosMapper.mapListFromDTO(residuos)
                )
                if (distrito == "") {
                    HtmlDirectory.copyHtmlDataResumen(dataframeController.resumen(), destino)
                } else {
                    HtmlDirectory.copyHtmlDataResumen(dataframeController.resumenDistrito(distrito), destino)
                }

            }
        }else{
            println("El distrito que has introducido no existe")
        }
    }

    fun getOption(args: Array<String>): Int {
        var opt = -1

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