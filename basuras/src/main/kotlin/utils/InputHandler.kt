package utils

import com.twelvemonkeys.imageio.metadata.Directory
import org.apache.poi.sl.draw.geom.Path
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths

object InputHandler {

    fun checkPath(path : String){
        if(!File(path).exists())

            println("ruta incorrecta")

    }
}