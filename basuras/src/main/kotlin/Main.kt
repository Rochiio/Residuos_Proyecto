import com.sun.tools.javac.Main
import com.twelvemonkeys.imageio.metadata.Directory
import mappers.ContenedorMapper
import mappers.ListaContenedorDTO
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import utils.Bitacora
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
    val inputHandler = InputHandler()
    val dir = System.getProperty("user.dir")
    val origen = "datosPrueba"
    val lista = BasuraController.parser("${System.getProperty("user.dir")}${File.separator}$origen","${System.getProperty("user.dir")}${File.separator}datosPrueba")
}