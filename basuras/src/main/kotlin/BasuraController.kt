import mappers.ContenedorMapper
import mappers.ListaContenedorDTO
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import java.io.File

object BasuraController {
    fun executeCommand(args: Array<String>) {
        when (getOption(args)) {
            1 -> parser(args[1], args[2])
            2 -> println()
            3 -> println()
            else -> println("Saliendo del programa")
        }
    }

    /**
     * Lee todos los csv en origen y los escribe en destino como csv, xml y json
     * @param origen String
     * @param destino String
     */
    fun parser(origen: String, destino: String) {
        val cabeceraResiduos = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
        val cabeceraContenedor =
            "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" + "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" + "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

        if (checkPath(origen) && checkPath(destino)) {
            val files = retrieveCsv(origen)
            if (files.isNotEmpty()) {
                for (f in files) {
                    val file = File(origen + File.separator + f.path)
                    val firstLine = file.readLines().first().replace("\uFEFF", "")

                    if (firstLine == cabeceraResiduos) {
                        val residuosMapper: ResiduosMapper = ResiduosMapper()
                        val residuos = residuosMapper.readCsvResiduo(file.path)

                        //Aqui es donde podriamos meter concurrencia
                        if (residuos != null) {
                            //un hilo
                            residuosMapper.toJson(
                                "$destino${File.separator}residuos_parse.json", ListaResiduosDto(residuos)
                            )
                            //otro hilo
                            residuosMapper.toXml(
                                destino,
                                ListaResiduosDto(residuos)
                            )
                            residuosMapper.writeCsvResiduo(ListaResiduosDto(residuos), destino)
                        } else
                            println("No se pudo leer el archivo CSV")
                    } else if (firstLine == cabeceraContenedor) {
                        val contenedorMapper = ContenedorMapper()
                        val contenedores = contenedorMapper.readCSV(file.path)

                        contenedorMapper.toJson(
                            "$destino${File.separator}contenedores_parse.json", ListaContenedorDTO(contenedores)
                        )
                        contenedorMapper.toXML(
                            destino,
                            ListaContenedorDTO(contenedores)
                        )
                        contenedorMapper.writeCsv(contenedores, destino)
                    }
                }
            }
        }

    }

    fun resumen(origen: String, destino: String) {
        if (checkPath(origen) && checkPath(destino)) {
            //primero busca csvs
            val files = retrieveCsv(origen)
            for (f in files) {

            }
        }
    }


    fun getOption(args: Array<String>): Int {
        var opt = -1
        if (args.size < 3) {
            println("Formato incorrecto. Argumentos insuficientes.")
            return -1
        } else if (args.size > 7) {
            println("Orden incorrecta. Revisa el formato")
            return -1
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
        return -1
    }

    fun checkPath(path: String): Boolean {
        try {
            val existe = File(path).exists()
        } catch (e: FileSystemException) {
            return false
        }
        val file = File(path)
        val a = File(path).exists()
        val bn = File(path).isDirectory
        return File(path).isDirectory
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