package utils.html

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
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
        var cssDestino = directory+File.separator+"css"+File.separator
        var imgDestino = directory+File.separator+"img"+File.separator


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
     */
    private fun addData(html:String,fichero: File, cssDestino: String, imgDestino: String) {
        fichero.writeText(html)

        Files.walk(Path(css)).forEach {
            Files.copy(it, Path(cssDestino).resolve(Path(css).relativize(it)),
                StandardCopyOption.REPLACE_EXISTING)
        }

        Files.walk(Path(img)).forEach {
            Files.copy(it, Path(imgDestino).resolve(Path(img).relativize(it)),
                StandardCopyOption.REPLACE_EXISTING)
        }

    }


    /**
     * Crear los directorios si no existen en el directorio destino.
     * @param cssDestino carpeta destino del css.
     * @param imgDestino carpeta destino de las imágenes.
     */
    private fun createDirectories(cssDestino:String, imgDestino:String){
        if (Files.notExists(Path.of(cssDestino))){
            Files.createDirectory(Path.of(cssDestino))
        }

        if (Files.exists(Path.of(imgDestino))){
            Files.createDirectory(Path.of(imgDestino))
        }
    }

}