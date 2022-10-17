package mappers

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ContenedorMapperTest {
    var contenedorMapperTest = ContenedorMapper()
    private var direccion = System.getProperty("user.dir")+"${File.separator}src${File.separator}test${File.separator}resources"
    private var correctoTest = File(direccion+ File.separator+"contenedorCorrecto.csv")
    private var camposTest = File(direccion+ File.separator+"contenedorCamposErroneos.csv")
    private var lineaTest = File(direccion+ File.separator+"contenedorSinLineas.csv")

    @Test
    fun checkCSVCorrecto() {
        var resultado = contenedorMapperTest.checkCSV(correctoTest)
        assertTrue(resultado)
    }

    @Test
    fun checkCSVCamposIncorrectos() {
        var resultado = contenedorMapperTest.checkCSV(camposTest)
        assertFalse(resultado)
    }

    @Test
    fun checkCSVSinLineas() {
        var resultado = contenedorMapperTest.checkCSV(lineaTest)
        assertFalse(resultado)
    }
}