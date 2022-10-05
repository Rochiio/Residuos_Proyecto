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
    var csv = ResiduosMapper.readCsvResiduo("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba\\modelo_residuos_2021.csv")
    var lista = ListaResiduosDto(csv)
    ResiduosMapper.writeCsvResiduo(lista,"C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba")
    ResiduosMapper.toJson("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba",lista)
    ResiduosMapper.toXml("C:\\Users\\rpala\\Documents\\Residuos_Proyecto\\basuras\\datosPrueba",lista)

}