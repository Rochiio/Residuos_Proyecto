import com.sun.tools.javac.Main
import mappers.ContenedorMapper
import java.nio.file.Path

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 *
 * TODO CSV CONTENEDORES CAMPO NUMERO CONTIENE VACIOS
 */
fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    val contenedores = ContenedorMapper.readCSV("")
    println(contenedores)

}