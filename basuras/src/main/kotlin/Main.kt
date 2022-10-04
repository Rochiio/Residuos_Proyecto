import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.sun.tools.javac.Main
import mappers.ContenedorMapper
import java.io.File

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
    val csv = ContenedorMapper.readCSV(path.file)?.take(5)
    ContenedorMapper.toXML("datos/", csv)
   // val json = ContenedorMapper.toJson("basuras/src/main/resources/datos/contenedores_varios.json", csv)


}