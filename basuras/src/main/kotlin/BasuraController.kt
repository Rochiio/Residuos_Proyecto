import mappers.ContenedorMapper
import mappers.ListaContenedorDTO
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import java.io.File
import java.util.StringJoiner

object BasuraController {
    fun executeCommand(args: Array<String>) {
        when (getOption(args)) {
            1 -> println()
            2 -> println()
            3 -> println()
            else -> println("Saliendo del programa")
        }
    }

    fun parser(origen: String, destino: String) {
        val cabeceraResiduos = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
        val cabeceraContenedor =
            "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;" + "Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;" + "COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

        if (checkPath(origen) && checkPath(destino)) {
            val files = retrieveCsv(origen)
            if (files.isNotEmpty()) {
                for (f in files) {
                    var file = File(origen + File.separator + f.path)
                    var cabeza2 = f.path.lowercase()
                    var cabeza3 = f.path.lowercase().contentEquals("residuos")


                    if (f.path.lowercase().contains("residuos")) {
                        val residuosMapper: ResiduosMapper = ResiduosMapper()
                        val residuos = residuosMapper.readCsvResiduo(file.path)
                        if (residuos != null) {
                            residuosMapper.toJson(
                                "$destino${File.separator}residuos_parse.json", ListaResiduosDto(residuos)
                            )
                            residuosMapper.toXml(
                                destino,
                                ListaResiduosDto(residuos)
                            )
                        } else
                            println("No se pudo leer el archivo CSV")
                    } else {
                        val contenedorMapper = ContenedorMapper()
                        val contenedores = contenedorMapper.readCSV(file.path)
                        if (contenedores != null) {
                            contenedorMapper.toJson(
                                "$destino${File.separator}residuos_parse.json", ListaContenedorDTO(contenedores)
                            )
                            contenedorMapper.toXML(
                                destino,
                                ListaContenedorDTO(contenedores)
                            )
                        } else
                            println("No se pudo leer el archivo CSV")
                    }


                }
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


    fun retrieveCsv(directory: String): List<File> {
        val list = mutableListOf<File>()
        if (checkPath(directory)) {
            val file = File(directory)

            file.list()?.forEach { file -> if (file.endsWith(".csv")) list.add(File(file)) }
        }
        return list
    }
}