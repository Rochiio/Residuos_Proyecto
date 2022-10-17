import controllers.BasuraController
import mappers.BitacoraMapper
import models.Bitacora
import kotlin.system.measureTimeMillis

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 */
fun main(args: Array<String>) {
    var bitacoraMapper = BitacoraMapper()
    var exito: Boolean
    var a = System.getProperty("user.dir")
    var tiempo = measureTimeMillis {
        exito = BasuraController.executeCommand(args)
    }

    //TODO como ponemos si ha sido un exito?
    var bitacora = Bitacora(args[0], exito, tiempo)
    bitacoraMapper.makeBitacora(bitacora)

}