import com.sun.tools.javac.Main
import mappers.ContenedorMapper
import mappers.ListaContenedorDTO

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

    val cosa = ContenedorMapper.toJson("fichero.json", ListaContenedorDTO(csv))
    val lista = ContenedorMapper.fromJson("fichero.json")
    ContenedorMapper.toXML("fichero.xml", ListaContenedorDTO(csv))
    val listaxml = ContenedorMapper.fromXML("fichero.xml")
println()
}