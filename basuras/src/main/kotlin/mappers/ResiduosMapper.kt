package mappers

import dto.ResiduosDto
import models.Residuos
import models.tipoResiduo
import java.nio.file.Files
import java.nio.file.Path

/**
 * Clase de mapeo de Residuos
 */
class ResiduosMapper {

    /**
     * Pasar un residuoDto a residuo
     * @param residuoDto residuo en tipo dto
     * @return el residuo en tipo residuo
     */
    fun dtoTo(residuoDto: ResiduosDto): Residuos{
        return Residuos(
            año = residuoDto.año,
            mes = residuoDto.mes,
            lote = residuoDto.lote,
            residuo = tipoResiduo.valueOf(residuoDto.residuo),
            distrito = residuoDto.distrito,
            nombreDistrito = residuoDto.nombreDistrito,
            toneladas = residuoDto.toneladas
        )
    }

    /**
     * Pasar un residuo a residuoDto
     * @param residuo en tipo residuo
     * @return el residuo en tipo residuoDto
     */
    fun toDto(residuo: Residuos): ResiduosDto{
        return ResiduosDto(
            año = residuo.año,
            mes = residuo.mes,
            lote = residuo.lote,
            residuo = residuo.residuo.name,
            distrito = residuo.distrito,
            nombreDistrito = residuo.nombreDistrito,
            toneladas = residuo.toneladas
        )
    }


    /**
     * pasar un csv a residuos
     * @param directorio directorio donde se encuentra el fichero csv
     * @return lista de residuos.
     */
    fun csvToResiduo(directorio: String): List<Residuos> {
        return Files.lines(Path.of(directorio)).skip(1).map { mapToResiduo(it) }.toList()
    }


    /**
     * Pasar un string a residuo
     * @param line linea a mappear a residuo
     * @return residuo creado
     */
    private fun mapToResiduo(line:String):Residuos{
        val campos = line.split(";")
        return Residuos(
            año = campos[0].toShort(),
            mes = campos[1],
            lote = campos[2].toByte(),
            residuo = toTipoResiduo(campos[3]),
            distrito = campos[4].toByte(),
            nombreDistrito = campos[5],
            toneladas = campos[6].toInt()
        )
    }


    /**
     * Saber que tipo de residuo es
     * @param campo string a saber que tipo de residuo es
     * @return tipo de residuo
     */
    private fun toTipoResiduo(campo: String): tipoResiduo {
        var tipo:tipoResiduo = tipoResiduo.PILAS
        when(campo){
            "RESTO" -> tipo =tipoResiduo.RESTO
            "ENVASES" -> tipo =tipoResiduo.ENVASES
            "VIDRIO" -> tipo =tipoResiduo.VIDRIO
            "VIDRIO COMERCIAL" -> tipo =tipoResiduo.VIDRIO_COMERCIAL
            "CLINICOS" -> tipo =tipoResiduo.CLINICOS
            "ORGANICA" -> tipo =tipoResiduo.ORGANICA
            "PAPEL-CARTON" -> tipo =tipoResiduo.PAPEL_CARTON
            "CARTON COMERCIAL" -> tipo =tipoResiduo.CARTON_COMERCIAL
            "RCD" -> tipo =tipoResiduo.RCD
            "PUNTOS LIMPIOS" -> tipo =tipoResiduo.PUNTOS_LIMPIOS
            "CONTENEDORES DE ROPA" -> tipo =tipoResiduo.CONTENEDORES_DE_ROPA
            "CAMA DE CABALLO" -> tipo =tipoResiduo.CAMA_DE_CABALLO
            "ANIMALES MUERTOS" -> tipo =tipoResiduo.ANIMALES_MUERTOS
            "PILAS" -> tipo =tipoResiduo.PILAS
        }
        return tipo
    }

    
}


