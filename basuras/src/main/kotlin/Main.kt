import controllers.BasuraController
import controllers.DataframeController
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import models.Bitacora
import utils.html.HtmlDirectory
import kotlin.system.measureTimeMillis

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 */
fun main(args: Array<String>) {
    var tiempo = measureTimeMillis {
        BasuraController.executeCommand(args)
    }

    //TODO como ponemos si ha sido un exito?
    var bitacora = Bitacora(args[0],true,tiempo)
    bitacora.bitacoraXml(args[args.size-1])



//    val contenedorMapper: ContenedorMapper = ContenedorMapper()
//    val residuosMapper: ResiduosMapper = ResiduosMapper()
//
//    var residuos = residuosMapper.readCsvResiduo("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba\\modelo_residuos_2021.csv")
//    var contenedores = contenedorMapper.readCSV("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\src\\main\\resources\\datos\\contenedores_varios.csv")
//
//    var listaR = residuos.map { residuosMapper.fromDto(it) }.toList()
//    var listaC = contenedores.map { contenedorMapper.fromDto(it) }.toList()
//
//    //PROBANDO DATAFRAME
//    var frame = DataframeController(listaC,listaR)
//    var html = frame.resumen()
//     HtmlDirectory.copyHtmlDataResumen(html,"C:\\Users\\rpala\\Desktop\\Prueba")


}