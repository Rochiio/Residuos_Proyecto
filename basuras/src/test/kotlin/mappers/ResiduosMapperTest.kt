package mappers

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class ResiduosMapperTest {
    var residuosMapperTest = ResiduosMapper()
    private var direccion = System.getProperty("user.dir")+"${File.separator}src${File.separator}test${File.separator}resources"
    private var correctoTest = File(direccion+File.separator+"residuoCorrecto.csv")
    private var camposTest = File(direccion+File.separator+"residuoCamposErroneos.csv")
    private var lineaTest = File(direccion+File.separator+"residuoSinLineas.csv")

    @Test
    fun checkCSVCorrecto() {
        var resultado = residuosMapperTest.checkCSV(correctoTest)
        assertTrue(resultado)
    }

    @Test
    fun checkCSVCamposIncorrectos() {
        var resultado = residuosMapperTest.checkCSV(camposTest)
        assertFalse(resultado)
    }

    @Test
    fun checkCSVSinLineas() {
        var resultado = residuosMapperTest.checkCSV(lineaTest)
        assertFalse(resultado)
    }
}