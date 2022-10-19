package controllers
/* Nombre: Mohamed Asidah Bchiri
 * github: loopedMoha
 * email: moha.as172@gmail.com
 */
import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.*
import jetbrains.letsPlot.Stat.identity
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomBar
import jetbrains.letsPlot.geom.geomTile
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.label.labs
import jetbrains.letsPlot.scale.scaleFillGradient
import models.Contenedor
import models.Residuos
import models.distrito
import models.nombreDistrito
import mu.KotlinLogging

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.DisplayConfiguration
import org.jetbrains.kotlinx.dataframe.io.html
import utils.Format
import utils.html.HtmlTemplete
import java.io.File
import java.text.Collator
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis

/**
 * Clase consultas dataframe y gráficos.
 */
class DataframeController(
    private val contenedores:List<Contenedor>,
    private val residuos:List<Residuos>
    ) {
    private val logger = KotlinLogging.logger {}
    private val IMAGES = System.getProperty("user.dir")+"${File.separator}src${File.separator}" +
            "main${File.separator}resources${File.separator}resumenGenerator${File.separator}img${File.separator}"

    private var residuosData: DataFrame<Residuos>
    private var contenedoresData : DataFrame<Contenedor>

    private lateinit var contenedoresTipoDistrito: DataFrame<Contenedor>

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
        var numeroContenedoresTipoDistrito: String
        var mediamContenedoresTipoDistrito: String
        var mediaToneladasAnualesBasuraDistrito: String
        var maxMinMedDesvToneladasAnualesBasuraDistrito: String
        var sumaRecogidoDistrito: String
        var cantidadResiduoDistrito: String

        var tiempo = measureTimeMillis {
            logger.info("Realizando Consultas RESUMEN")
            numeroContenedoresTipoDistrito = consultaNumContenedoresTipoDistrito()
            mediamContenedoresTipoDistrito = consultaMediaContenedoresTipoDistrito()
            graficoContenedoresDistrito()
            mediaToneladasAnualesBasuraDistrito = consultaMediaToneladasAnuales()
            graficoMediaToneladasMensuales()
            maxMinMedDesvToneladasAnualesBasuraDistrito = consultaMaxMinMedDesvToneladasAnuales()
            sumaRecogidoDistrito = consultaSumaAñoDistrito()
            cantidadResiduoDistrito = consultaCantidadResiduoDistrito()
        }

        //Aqui creamos el html de prueba
        var templete = HtmlTemplete(
            "Madrid",
            Format.formatDate(LocalDateTime.now()),
            numeroContenedoresTipoDistrito = numeroContenedoresTipoDistrito,
            maxMinMediaDesv = maxMinMedDesvToneladasAnualesBasuraDistrito,
            tiempoGeneracion = tiempo,
            mediaContenedoresTipoDistrito = mediamContenedoresTipoDistrito,
            mediaToneladasAnuales = mediaToneladasAnualesBasuraDistrito,
            sumaRecogidoDistrito = sumaRecogidoDistrito,
            porDistritoTipoResiduoCantidad = cantidadResiduoDistrito
        )
        return templete.generateHtmlResumen()
    }



    /**
     * Consultas a realizar cuando se elige el comando RESUMEN DISTRITO.
     * @return el html ya creado
     */
    fun resumenDistrito(distrito: String): String {
        val numeroContenedoresTipoDistrito: String
        val totalToneladasResiduo: String
        val maxMinMediaDesv: String

        val tiempo = measureTimeMillis {
            logger.info("Realizando Consultas RESUMEN DISTRITO")
            numeroContenedoresTipoDistrito = consultaNumeroContenedoresTipoDistrito(distrito)
            totalToneladasResiduo = consultaToneladasDistrito(distrito)
            graficoToneladasResiduoDistrito(distrito)
            maxMinMediaDesv = consultaEstadisticasDistrito(distrito)
            graficoMaxMinMediaMesDistrito(distrito)
        }
        var templete = HtmlTemplete(
            distrito,
            Format.formatDate(LocalDateTime.now()),
            numeroContenedoresTipoDistrito = numeroContenedoresTipoDistrito,
            totalToneladasResiduo = totalToneladasResiduo,
            tiempoGeneracion = tiempo,
            maxMinMediaDesv = maxMinMediaDesv
        )
        return templete.generateHtmlResumenDistrito()
    }

    fun compararDistrito(distrito: String, distritodf: String) : Boolean{
        val collator = Collator.getInstance()
        collator.strength = 0
        return collator.compare(distrito, distritodf) == 0
    }


    fun consultaNumeroContenedoresTipoDistrito(distrito: String): String {
        return contenedoresData.groupBy("distrito", "tipoContenedor")
            .filter { compararDistrito(distrito, it.distrito) }
            .aggregate { sum("cantidad") into "total" }
            .html()
    }

    fun consultaToneladasDistrito(distrito: String): String {
        return residuosData.groupBy("nombreDistrito", "residuo")
            .filter { compararDistrito(distrito, it.nombreDistrito) }
            .aggregate { sum("toneladas") into "total" }
            .html()
    }

    fun consultaEstadisticasDistrito(distrito: String): String {
        return residuosData.groupBy("nombreDistrito", "mes", "residuo")
            .filter { compararDistrito(distrito, it.nombreDistrito) }
            .aggregate {
                max("toneladas") into "Maximo"
                min("toneladas") into "Minimo"
                mean("toneladas") into "Media"
                std("toneladas") into "Desviacion"
            }.html()

    }


    /**
     * Gráfica de las toneladas por residuo en el distrito.
     * @param distrito distrito elegido para realizar las consultas.
     */
    fun graficoToneladasResiduoDistrito(distrito: String) {
        val toneladas = residuosData.groupBy("nombreDistrito", "residuo")
            .filter { compararDistrito(distrito, it.nombreDistrito) }
            .aggregate { sum("toneladas") into "total" }.toMap()


        var fig: Plot = letsPlot(data = toneladas) + geomBar(
            stat = identity,
            alpha = 0.8,
            fill = Color.BLUE,
            color = Color.BLACK
        ) {
            x = "residuo"
            y = "total"
        } + labs(
            x = "Residuo",
            y = "Toneladas",
            title = "Toneladas totales de cada tipo de residuo en el distrito de $distrito"
        )

        ggsave(fig, "03-totalToneladasResiduo$distrito.png", path = IMAGES)

    }


    /**
     * Gráfico de máximo, mínimo, media por mes y distrito.
     * @param distrito distrito elegido para realizar las consultas.
     */
    fun graficoMaxMinMediaMesDistrito(distrito: String) {

        val consulta = residuosData.groupBy("nombreDistrito", "mes")
            .filter { compararDistrito(distrito, it.nombreDistrito) }
            .aggregate {
                max("toneladas") into "max"
                min("toneladas") into "min"
                mean("toneladas") into "mean"
            }.toMap()
        var fig: Plot = letsPlot(data = consulta) + geomBar(
            stat = identity,
            alpha = 0.8,
            fill = Color.BLUE,
            color = Color.BLACK
        ){
            x = "mes"
            y = "max"
        } + geomBar(
            stat = identity,
            alpha = 0.8,
            fill = Color.RED,
            color = Color.BLACK
        ){
            x = "mes"
            y = "min"
        } + geomBar(
            stat = identity,
            alpha = 0.8,
            fill = Color.GREEN,
            color = Color.BLACK
        ) {
            x = "mes"
            y = "mean"
        } + labs(
            x = "Meses",
            y = "Toneladas",
            title = "Toneladas totales de cada tipo de residuo en el distrito de $distrito"
        )

        ggsave(fig, "04-maxMinMediaPorMes$distrito.png", path = IMAGES)
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
        return contenedoresData.groupBy("distrito", "tipoContenedor")
            .aggregate { sum("cantidad") into "Total" }.aggregate { mean("Total") }.toDataFrame().html()

    }


    /**
     * Consulta: Número de contenedores de cada tipo que hay en cada distrito.
     * @return String de resultado.
     */
    private fun consultaNumContenedoresTipoDistrito(): String {
        contenedoresTipoDistrito = contenedoresData.groupBy("distrito","tipoContenedor")
            .aggregate {
            sum("cantidad") into "total"
        }.sortBy("distrito")
        return contenedoresTipoDistrito.html()
    }


}