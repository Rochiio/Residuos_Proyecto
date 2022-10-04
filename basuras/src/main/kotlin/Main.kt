import com.sun.tools.javac.Main
import mappers.ContenedorMapper

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

    val path = Main::class.java.classLoader.getResource("datos/contenedores_varios.csv")
    val csv = ContenedorMapper.readCSV(path.file)
    val json = ContenedorMapper.toJson("basuras/src/main/resources/datos/contenedores_varios.json", csv)


}