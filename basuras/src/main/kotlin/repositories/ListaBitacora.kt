package repositories

import kotlinx.serialization.Serializable

import models.Bitacora
/**
* Clase que contiene una lista de bitacoras, utilizado para los mapeos con XML.
*/
@Serializable
data class ListaBitacora(
    var lista: List<Bitacora>
){

}