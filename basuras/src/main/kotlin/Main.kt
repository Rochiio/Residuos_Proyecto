import com.sun.tools.javac.Main
import mappers.ContenedorMapper
import mappers.ListaContenedorDTO
import mappers.ResiduosMapper
import repositories.ListaResiduosDto

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

    val path = Main::class.java.classLoader.getResource("datos/modelo_residuos_2021.csv")
    val residuos = ResiduosMapper.readCsvResiduo("D:\\PracticasDAM\\residuos-ad\\basuras\\src\\main\\resources\\datos\\modelo_residuos_2021.csv")

    ResiduosMapper.toJson("residuos.json", ListaResiduosDto(residuos!!))
    ResiduosMapper.toXml("/", ListaResiduosDto(residuos))
    val rJ = ResiduosMapper.fromJson("residuos.json")
    val rX = ResiduosMapper.fromXml("residuos.xml")

println()
}