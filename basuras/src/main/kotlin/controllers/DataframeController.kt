package controllers

import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.Geom
import jetbrains.letsPlot.Stat.identity
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomBar
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.label.labs
import jetbrains.letsPlot.letsPlot
import models.Contenedor
import models.Residuos
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.DisplayConfiguration
import org.jetbrains.kotlinx.dataframe.io.html
import utils.Format
import utils.html.HtmlTemplete
import java.io.File
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis

/**
 * Clase consultas dataframe y gráficos.
 */
class DataframeController(
    private val contenedores:List<Contenedor>,
    private val residuos:List<Residuos>
    ) {

    private val IMAGES = System.getProperty("user.dir")+"${File.separator}src${File.separator}" +
            "main${File.separator}resources${File.separator}resumenGenerator${File.separator}img${File.separator}"

    private var residuosData: DataFrame<Residuos>
    private var contenedoresData : DataFrame<Contenedor>

    private lateinit var dfNumeroContenedoresTipoDistrito: DataFrame<Contenedor>


    init {
        residuosData = residuos.toDataFrame()
        residuosData.cast<Residuos>()
        contenedoresData = contenedores.toDataFrame()
        contenedoresData.cast<Contenedor>()
        DisplayConfiguration.DEFAULT.rowsLimit=10000
    }


    /**
     * Consultas a realizar cuando se elige el comando resumen.
     * @return el html ya creado
     */
    fun resumen(): String {
        var numeroContenedoresTipoDistrito: String ; var mediamContenedoresTipoDistrito: String
        var mediaToneladasAnualesBasuraDistrito: String ; var maxMinMedDesvToneladasAnualesBasuraDistrito: String
        var sumaRecogidoDistrito: String ; var cantidadResiduoDistrito: String

        var tiempo = measureTimeMillis {
            numeroContenedoresTipoDistrito = consultaNumContenedoresTipoDistrito()
            mediamContenedoresTipoDistrito= consultaMediaContenedoresTipoDistrito()
            graficoContenedoresDistrito()
            mediaToneladasAnualesBasuraDistrito= consultaMediaToneladasAnuales()
            graficoMediaToneladasMensuales()
            maxMinMedDesvToneladasAnualesBasuraDistrito= consultaMaxMinMedDesvToneladasAnuales()
            sumaRecogidoDistrito= consultaSumaAñoDistrito()
            cantidadResiduoDistrito= consultaCantidadResiduoDistrito()
        }

        //Aqui creamos el html de prueba
        var templete = HtmlTemplete("Madrid", Format.formatDate(LocalDateTime.now()), numeroContenedoresTipoDistrito = numeroContenedoresTipoDistrito,
        maxMinMediaDesv = maxMinMedDesvToneladasAnualesBasuraDistrito, tiempoGeneracion=tiempo,
            mediaContenedoresTipoDistrito = mediamContenedoresTipoDistrito, mediaToneladasAnuales = mediaToneladasAnualesBasuraDistrito,
        sumaRecogidoDistrito = sumaRecogidoDistrito, porDistritoTipoResiduoCantidad = cantidadResiduoDistrito)
        return templete.generateHtmlResumen()
    }


    /**
     * Consulta: Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
     * @return String de resultado.
     */
    private fun consultaCantidadResiduoDistrito(): String {
        return residuosData.groupBy("nombreDistrito","residuo")
            .aggregate {
                sum("toneladas") into "total_recogido"
            }.html()
    }

    /**
     * Consulta: Suma de tod0 lo recogido en un año por distrito.
     * @return String de resultado.
     */
    private fun consultaSumaAñoDistrito(): String {
        return residuosData.groupBy("nombreDistrito","año").aggregate {
            sum("toneladas") into "suma"
        }.html()
    }


    /**
     * Consulta: Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
     * de basura agrupadas por distrito.
     * @return String de resultado.
     * TODO Creo que está bien
     */
    private fun consultaMaxMinMedDesvToneladasAnuales(): String {
        return residuosData.groupBy("residuo","nombreDistrito","año")
            .aggregate {
                max("toneladas") into "max"
                min("toneladas") into "min"
                mean("toneladas") into "media"
                std("toneladas") into "desviacion"
            }.html()
    }


    /**
     * Gráfico de media de toneladas mensuales de recogida de basura por distrito
     * TODO No se si es correcto del todo
     */
    private fun graficoMediaToneladasMensuales() {
        var agrupado = residuosData.groupBy("nombreDistrito","mes")
            .aggregate {
                mean("toneladas") into "media"
            }.toMap()

        var fig: Plot = letsPlot(data=agrupado) + geomBar(
            stat = identity,
            alpha = 0.8,
            fill = Color.BLUE,
            color = Color.BLACK
        ){
            x="nombreDistrito"
            y="media"
        } + labs(
            x="Distrito",
            y="Media",
            title = "Media de Toneladas Mensuales por Distrito"
        )

        ggsave(fig,"02-mediaToneladasMensuales.png", path=IMAGES)
    }


    /**
     * Consulta: Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
     * distrito.
     * @return String de resultado.
     * TODO resultado incorrecto
     */
    private fun consultaMediaToneladasAnuales(): String {
         return residuosData.groupBy("año","residuo","nombreDistrito")
            .aggregate {
                mean("toneladas") into "Media"
            }.sortBy("nombreDistrito").html()
    }


    /**
     * Gráfico con el total de contenedores por distrito.
     */
    private fun graficoContenedoresDistrito() {
        var agrupado = contenedoresData.groupBy("distrito")
            .aggregate {
                count() into "total"
            }.toMap()

        var fig: Plot = letsPlot(data = agrupado) + geomBar(
            stat=identity,
            alpha=0.8,
            fill = Color.BLUE
        ) {
            x = "distrito"
            y = "total"
        } + labs(
            x="Distritos",
            y ="Total",
            title = "Total de Contenedores por Distrito"
        )

        ggsave(fig, "01-totalContenedoresDistrito.png", path=IMAGES)
    }


    /**
     * Consulta: Media de contenedores de cada tipo que hay en cada distrito.
     * @return String de resultado.
     * TODO Este es imposible
     */
    private fun consultaMediaContenedoresTipoDistrito(): String {
       return dfNumeroContenedoresTipoDistrito.groupBy("distrito","tipoContenedor")
           .aggregate {
               mean("total") into "media"
       }.html()
    }


    /**
     * Consulta: Número de contenedores de cada tipo que hay en cada distrito.
     * @return String de resultado.
     */
    private fun consultaNumContenedoresTipoDistrito(): String {
        dfNumeroContenedoresTipoDistrito = contenedoresData.groupBy("distrito","tipoContenedor")
            .aggregate {
            sum("cantidad") into "total"
        }.sortBy("distrito")

        return dfNumeroContenedoresTipoDistrito.html()
    }


}