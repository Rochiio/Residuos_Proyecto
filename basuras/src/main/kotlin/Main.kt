import controllers.BasuraController
import mappers.BitacoraMapper
import models.Bitacora
import kotlin.system.measureTimeMillis

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 */
fun main(args: Array<String>) {
    var bitacoraMapper = BitacoraMapper()
    var exito: Boolean

    measureTimeMillis {
        exito = BasuraController.executeCommand(args)
    }.also {
        var bitacora = Bitacora(args[0], exito, it)
        bitacoraMapper.makeBitacora(bitacora)
    }


}