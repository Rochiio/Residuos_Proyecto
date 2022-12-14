package utils.html

import mu.KotlinLogging

/**
 * Clase para la generación del html.
 */

class HtmlTemplete(
    //Variables iguales
    private val distrito: String,
    private val fechaHora: String,
    private val numeroContenedoresTipoDistrito: String="",
    private val maxMinMediaDesv: String="",
    private val tiempoGeneracion: Long,
    //Variable Resumen Distrito
    private val totalToneladasResiduo: String ="",
    //Variable Resumen
    private val mediaContenedoresTipoDistrito: String="",
    private val mediaToneladasAnuales: String="",
    private val sumaRecogidoDistrito: String="",
    private val porDistritoTipoResiduoCantidad: String=""
) {

    private val logger = KotlinLogging.logger {}



    /**
     * Generación html para la opción RESUMEN
     * @return string con el html ya generado.
     */
    fun generateHtmlResumen():String {

        logger.info("Generando Html de RESUMEN")

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="./css/styleResumen.css">
                <title>Resumen</title>
            </head>
            <body>
                <h1 id="titulo">Resumen de recogidas de basura y reciclaje de $distrito</h1>
                <img id ="logo" src="./img/logo.png">
                <p class="datos">$fechaHora</p>
                <p class="datos">Rocío Palao & Mohamed Asidah</p>
                
                <hr>
                
                <div id="container">
                    <h4>Número de contenedores de cada tipo que hay en cada distrito: </h4>
                    <p>$numeroContenedoresTipoDistrito</p>
                    
                    <h4>Media de contenedores de cada tipo que hay en cada distrito: </h4>
                    <p>$mediaContenedoresTipoDistrito</p>

                    <br>

                    <h4>Gráfico con el total de contenedores por distrito:</h4>
                    <img class="images" src="./img/01-totalContenedoresDistrito.png">

                    <br>

                    <h4>Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
                    distrito:</h4>
                    <p>$mediaToneladasAnuales</p>

                    <br>

                    <h4>Gráfico de media de toneladas mensuales de recogida de basura por distrito:</h4>
                    <img class="images" src="./img/02-mediaToneladasMensuales.png">

                    <br>

                    <h4>Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
                    de basura agrupadas por distrito:</h4>
                    <p>$maxMinMediaDesv</p>

                    <br>

                    <h4>Suma de todo lo recogido en un año por distrito:</h4>
                    <p>$sumaRecogidoDistrito</p>

                    <br>

                    <h4>Por cada distrito obtener para cada tipo de residuo la cantidad recogida:</h4>
                    <p>$porDistritoTipoResiduoCantidad</p>

                    <br>

                    <p>Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                </div>
                
            </body>
            </html>
        """.trimIndent()
    }


        /**
         * Generación html para la opción RESUMEN DISTRITO.
         * @return string con el html ya generado.
         */
        fun generateHtmlResumenDistrito(): String {

            logger.info("Generando html de RESUMEN DISTRITO")

            return """
            <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="./css/styleResumen.css">
            <title>Resumen$distrito</title>
        </head>
        <body>
            <h1 id="titulo">Resumen de recogidas de basura y reciclaje de $distrito</h1>
            <img id ="logo" src="./img/logo.png">
            <p class="datos">$fechaHora</p>
            <p class="datos">Rocío Palao & Mohamed Asidah</p>
        
            <hr>
        
            <div id="container">
        
                <h4>Número de contenedores de cada tipo en el distrito de $distrito: </h4>
                <p>$numeroContenedoresTipoDistrito</p>
        
                <br>
                
                <h4>Total de toneladas recogidas de cada tipo de residuo en el distrito de $distrito: </h4>
                <p>$totalToneladasResiduo</p>
        
                <br>
        
                <h4>Gráfico con el total de toneladas de cada residuo recogidas en el distrito de $distrito:</h4>
                <img class="images" src="./img/03-totalToneladasResiduo$distrito.png">
        
                <br>
        
        
                <h4>Máximo, mínimo y media por mes por residuo recogidos en el distrito de $distrito:</h4>
                <p>$maxMinMediaDesv</p>
        
                <br>
        
                <h4>Gráfica del máximo, minimo  media por meses en el distrito de $distrito:</h4>
                <img class="images" src="./img/04-maxMinMediaPorMes$distrito.png">
        
                <br>
        
                <p>Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                
        
            </div>
            
        </body>
        </html>
        """.trimIndent()
        }
    }
