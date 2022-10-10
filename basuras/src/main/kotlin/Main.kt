
import jetbrains.datalore.plot.base.DataFrame
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import java.io.File

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 *
 * TODO CSV CONTENEDORES CAMPO NUMERO CONTIENE VACIOS
 */
fun main(args: Array<String>) {

    BasuraController.executeCommand(
        arrayOf("resumen", "${System.getProperty("user.dir")}${File.separator}datosPrueba", "${System.getProperty("user.dir")}${File.separator}salidaPrueba"))
   // BasuraController.parser("${System.getProperty("user.dir")}${File.separator}datosPrueba", "${System.getProperty("user.dir")}${File.separator}salidaPrueba")

}