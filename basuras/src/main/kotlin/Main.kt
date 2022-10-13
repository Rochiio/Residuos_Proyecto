import controllers.DataframeController
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import repositories.ListaBitacora
import models.Bitacora
import utils.html.HtmlDirectory

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 *
 * TODO CSV CONTENEDORES CAMPO NUMERO CONTIENE VACIOS
 */
fun main(args: Array<String>) {
    val contenedorMapper: ContenedorMapper = ContenedorMapper()
    val residuosMapper: ResiduosMapper = ResiduosMapper()

    var residuos = residuosMapper.readCsvResiduo("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba\\modelo_residuos_2021.csv")
    var contenedores = contenedorMapper.readCSV("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\src\\main\\resources\\datos\\contenedores_varios.csv")

    var listaR = residuos.map { residuosMapper.fromDto(it) }.toList()
    var listaC = contenedores.map { contenedorMapper.fromDto(it) }.toList()

    //PROBANDO DATAFRAME
    var frame = DataframeController(listaC,listaR)
    var html = frame.resumen()
     HtmlDirectory.copyHtmlDataResumen(html,"C:\\Users\\rpala\\Desktop\\Prueba")


//    var bitacoras = ListaBitacora()
//    bitacoras.addBitacora(Bitacora("resumen",true, 2L))
//    bitacoras.addBitacora(Bitacora("parser",true, 4L))
//    bitacoras.addBitacora(Bitacora("resumen distrito",true, 1L))
//    bitacoras.bitacoraXml("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba")
}