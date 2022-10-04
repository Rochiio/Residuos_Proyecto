import com.sun.tools.javac.Main
import com.twelvemonkeys.imageio.metadata.Directory
import mappers.ContenedorMapper
import mappers.ListaContenedorDTO
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import utils.InputHandler
import java.io.File
import java.util.InputMismatchException

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 *
 * TODO CSV CONTENEDORES CAMPO NUMERO CONTIENE VACIOS
 */
fun main(args: Array<String>) {
    //InputHandler.checkPath(System.getProperty("user.dir"))
    var a = File(System.getProperty("user.dir"))
    var b = a.list()
    var c = b.any { it.endsWith(".json") }
    InputHandler.checkPath("/data/")

    if (args.size < 3) {
        println("Introduce una opción correcta")
    } else {
        val path = Main::class.java.classLoader.getResource("datos/modelo_residuos_2021.csv")
        val residuos =
            ResiduosMapper.readCsvResiduo("D:\\PracticasDAM\\residuos-ad\\basuras\\src\\main\\resources\\datos\\modelo_residuos_2021.csv")

        ResiduosMapper.toJson("residuos.json", ListaResiduosDto(residuos!!))
        ResiduosMapper.toXml("/", ListaResiduosDto(residuos))
        val rJ = ResiduosMapper.fromJson("residuos.json")
        val rX = ResiduosMapper.fromXml("residuos.xml")

        println()
    }



}