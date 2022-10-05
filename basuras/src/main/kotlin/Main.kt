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

    File("/log/").createNewFile()
    if(args.size < 3){
        println("Introduce una opción válida para el programa")
        val bitacora = Bitacora("error", false, 0, "")
    }
}