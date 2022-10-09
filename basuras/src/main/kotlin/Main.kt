import controllers.DataframeController
import jetbrains.datalore.plot.base.DataFrame
import mappers.ContenedorMapper
import mappers.ResiduosMapper
import repositories.ListaResiduosDto
import utils.html.HtmlDirectory
import java.io.File

/**
 * Práctica Acceso a Datos.
 * Rocío Palao y Mohamed Asidah.
 *
 *
 * TODO CSV CONTENEDORES CAMPO NUMERO CONTIENE VACIOS
 */
fun main(args: Array<String>) {
    var residuos = ResiduosMapper.readCsvResiduo("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba\\modelo_residuos_2021.csv")
    var contenedores = ContenedorMapper.readCSV("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\src\\main\\resources\\datos\\contenedores_varios.csv")

    var listaR = residuos.map { ResiduosMapper.fromDto(it) }.toList()
    var listaC = contenedores.map { ContenedorMapper.fromDto(it) }.toList()

    //PROBANDO DATAFRAME
    var frame = DataframeController(listaC,listaR)
    var html = frame.resumen()
     HtmlDirectory.copyHtmlDataResumen(html,"C:\\Users\\rpala\\Desktop")
}