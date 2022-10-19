package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Clase para formateo de datos
 */
object Format {
    /**
     * Formatear una fecha a string, con un formato indicado.
     * @param date Fecha a formatear.
     * @return String con el estilo indicado.
     */
    fun formatDate(date: LocalDateTime):String{
        return date.format(DateTimeFormatter.ofLocalizedDate
            (FormatStyle.FULL).withLocale(Locale("es", "ES")))
    }
}