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
    var residuos = ResiduosMapper()
    //var residuos = ResiduosMapper.readCsvResiduo("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba\\modelo_residuos_2021.csv")
    var contenedores = ContenedorMapper()
    var listaR = residuos.readCsvResiduo("D:\\PracticasDAM\\residuos-ad\\basuras\\datosPrueba\\modelo_residuos_2021.csv").map { residuos.fromDto(it) }.toList()
    var listaC = contenedores.readCSV("D:\\PracticasDAM\\residuos-ad\\basuras\\datosPrueba\\contenedores_varios.csv").map { contenedores.fromDto(it) }.toList()

    //PROBANDO DATAFRAME
    var frame = DataframeController(listaC,listaR)
    frame.graficoMaxMinMediaMesDistrito("Usera")
    var html = frame.resumenDistrito("Usera")
    HtmlDirectory.copyHtmlDataResumen(html,System.getProperty("user.dir"))


   /* var bitacoras = ListaBitacora()
    bitacoras.addBitacora(Bitacora("resumen",true, 2L))
    bitacoras.addBitacora(Bitacora("parser",true, 4L))
    bitacoras.addBitacora(Bitacora("resumen distrito",true, 1L))
    bitacoras.bitacoraXml("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba")*/
}