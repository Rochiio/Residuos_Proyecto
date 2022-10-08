package controllers

import jetbrains.datalore.base.values.Color
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
import utils.HtmlTemplete
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

    private var residuosData: DataFrame<Residuos>
    private var contenedoresData : DataFrame<Contenedor>


    init {
        residuosData = residuos.toDataFrame()
        residuosData.cast<Residuos>()
        contenedoresData = contenedores.toDataFrame()
        contenedoresData.cast<Contenedor>()
        DisplayConfiguration.DEFAULT.rowsLimit=10000
    }


    /**
     * Consultas a realizar cuando se elige el comando resumen.
     */
    fun resumen(){
        var numeroContenedoresTipoDistrito: String
        var mediamContenedoresTipoDistrito: String
        var mediaToneladasAnualesBasuraDistrito: String
        var maxMinMedDesvToneladasAnualesBasuraDistrito: String
        var sumaRecogidoDistrito: String
        var cantidadResiduoDistrito: String
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
        var html = templete.generateHtmlResumen()
        var fichero = File(System.getProperty("user.dir")+ File.separator +"datosPrueba"+File.separator +"prueba.html")
        fichero.writeText(html)
    }

    /**
     * Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
     * TODO no está correcto
     */
    private fun consultaCantidadResiduoDistrito(): String {
        return residuosData.groupBy("nombreDistrito","residuo")
            .aggregate {
                sum("toneladas") into "total_recogido"
            }.html()
    }

    /**
     * Suma de todo lo recogido en un año por distrito.
     */
    private fun consultaSumaAñoDistrito(): String {
        return residuosData.groupBy("nombreDistrito","año").aggregate {
            sum("toneladas") into "suma"
        }.html()
    }


    /**
     * Consulta: Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
     * de basura agrupadas por distrito.
     * TODO revisar
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

        ggsave(fig,"02-mediaToneladasMensuales.png")
    }


    /**
     * Consulta: Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
     * distrito.
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

        ggsave(fig, "01-totalContenedoresDistrito.png")
    }


    /**
     * Consulta: Media de contenedores de cada tipo que hay en cada distrito.
     * TODO Revisar
     */
    private fun consultaMediaContenedoresTipoDistrito(): String {
        return contenedoresData.groupBy("distrito","tipoContenedor")
            .aggregate {
                mean("cantidad") into "media"
            }.sortBy("distrito").html()
    }


    /**
     * Consulta: Número de contenedores de cada tipo que hay en cada distrito.
     * @return String de resultado.
     */
    private fun consultaNumContenedoresTipoDistrito(): String {
        return contenedoresData.groupBy("distrito","tipoContenedor")
            .aggregate {
            count() into "total"
        }.sortBy("distrito").html()
    }


}