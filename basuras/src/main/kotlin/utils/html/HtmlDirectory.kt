package utils.html

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

/**
 * Creacion y copia de archivos necesarios para el html en el directorio destino.
 */
object HtmlDirectory {
    private val generator = System.getProperty("user.dir")+"${File.separator}src${File.separator}" +
        "main${File.separator}resources${File.separator}resumenGenerator${File.separator}"
    private val img = generator+"img"+File.separator
    private val css = generator+"css"+File.separator


    /**
     * Para copiar el html al directorio indicado.
     * @param html plantilla creado.
     * @param directory en el que hay que almacenarlo.
     * @return verdadero o false si ha salido todo correctamente, para crear la bitácora.
     */
    fun copyHtmlDataResumen(html:String,directory: String):Boolean{
        var fichero = File(directory+ File.separator +"resumen.html")
        var cssDestino = File(directory+File.separator+"css"+File.separator)
        var imgDestino = File(directory+File.separator+"img"+File.separator)


        createDirectories(cssDestino, imgDestino)
        addData(html, fichero, cssDestino, imgDestino)

        return true
    }


    /**
     * Añadiendo los ficheros necesarios para el html.
     * @param html html a escribir.
     * @param fichero fichero en el que se va a escirbir.
     * @param cssDestino fichero destino del css.
     * @param imgDestino fichero destino de las imágenes.
     * TODO da problemas cuando existen las carpetas en el destino
     */
    private fun addData(html:String,fichero: File, cssDestino: File, imgDestino: File) {
        fichero.writeText(html)

        Files.walk(Paths.get(css))
            .forEach { source: Path ->
                val destination: Path = Paths.get(
                    cssDestino.toString(), source.toString()
                        .substring(css.length-1)
                )
                try {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                } catch (e: IOException) {
                   println("Error: Debe elegir una carpeta vacía")
                }
            }

        Files.walk(Paths.get(img))
            .forEach { source: Path ->
                val destination: Path = Paths.get(
                    imgDestino.toString(), source.toString()
                        .substring(img.length-1)
                )
                try {
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                } catch (e: IOException) {
                    println("Error: Debe elegir una carpeta vacía")
                }
            }
    }


    /**
     * Crear los directorios si no existen en el directorio destino.
     * @param cssDestino carpeta destino del css.
     * @param imgDestino carpeta destino de las imágenes.
     */
    private fun createDirectories(cssDestino:File, imgDestino:File){
        if (!cssDestino.exists()) { cssDestino.mkdir() }
        if (!imgDestino.exists()) { imgDestino.mkdir() }

    }

}