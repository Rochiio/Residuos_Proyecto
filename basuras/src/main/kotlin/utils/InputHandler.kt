package utils

import java.io.File

class InputHandler/*val rutaOrigen: String, val rutaDestino: String*/ {

    fun checkPath(path: String) : Boolean {
        try{
            val existe = File(path).exists()
        } catch (e : FileSystemException){
            return false
        }
        val file = File(path)

        return File(path).isDirectory
    }


    fun retrieveCsv(directory : String) : List<File>{
        val list = mutableListOf<File>()
        if(checkPath(directory)) {
            val file = File(directory)

            file.list()?.forEach { file -> if (file.endsWith(".csv")) list.add(File(file)) }
        }
        return list
    }


}