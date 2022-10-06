package utils

class HtmlTemplete(
    //Variables iguales
    val distrito: String,
    val fechaHora: String,
    val numeroContenedoresTipoDistrito: Int,
    val maxMinMediaDesv: String,
    val tiempoGeneracion: Long,
    //Variable Resumen Distrito
    val totalToneladasResiduo: String,
    //Variable Resumen
    val mediaContenedoresTipoDistrito: Int,
    val mediaToneladasAnuales: String,
    val sumaRecogidoDistrito: String,
    val porDistritoTipoResiduoCantidad: String
) {

    /**
     * Generación html para la opción RESUMEN
     */
    fun generateHtmlResumen():String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="./resumencss">
                <title>Resumen</title>
            </head>
            <body>
                <h1>Resumen de recogidas de basura y reciclaje de $distrito</h1>
                <h6>$fechaHora</h6>
                <h5>Rocío Palao & Mohamed Asidah</h5>
                <br>
                <div id="container">
                    <h5>Número de contenedores de cada tipo que hay en cada distrito: </h5>
                    <p>$numeroContenedoresTipoDistrito</p>
                    
                    <h5>Media de contenedores de cada tipo que hay en cada distrito: </h5>
                    <p>$mediaContenedoresTipoDistrito</p>

                    <br>

                    <h4>Gráfico con el total de contenedores por distrito:</h4>
                    <img src="./img/01-totalContenedoresDistrito.png">

                    <h5>Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
                    distrito:</h5>
                    <p>$mediaToneladasAnuales/p>

                    <br>

                    <h4>Gráfico de media de toneladas mensuales de recogida de basura por distrito:</h4>
                    <img src="./img/02-mediaToneladasMensuales.png">

                    <h5>Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
                    de basura agrupadas por distrito:</h5>
                    <p>$maxMinMediaDesv</p>

                    <h5>Suma de todo lo recogido en un año por distrito:</h5>
                    <p>$sumaRecogidoDistrito</p>

                    <h5>Por cada distrito obtener para cada tipo de residuo la cantidad recogida:</h5>
                    <p>$porDistritoTipoResiduoCantidad</p>

                    <p>Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                </div>
                
            </body>
            </html>
        """.trimIndent()

    }

        /**
         * Generación html para la opción RESUMEN DISTRITO
         */
        fun generateHtmlResumenDistrito(): String {
            return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="./resumencss">
                <title>Resumen</title>
            </head>
            <body>
                <h1>Resumen de recogidas de basura y reciclaje de $distrito</h1>
                <h6>$fechaHora</h6>
                <h5>Rocío Palao & Mohamed Asidah</h5>
                <br>
                <div id="container">
                    <h5>Número de contenedores de cada tipo que hay en este distrito: </h5>
                    <p>$numeroContenedoresTipoDistrito</p>

                    <h5>Total de toneladas recogidas en ese distrito por residuo:</h5>
                    <p>$totalToneladasResiduo</p>

                    <br>

                    <h4>Gráfico con el total de toneladas por residuo en ese distrito:</h4>
                    <img src="./img/01-totalToneladasResiduo$distrito.png">

                    <h5>Máximo, mínimo , media y desviación por mes por residuo en dicho distrito:</h5>
                    <p>$maxMinMediaDesv</p>

                    <br>

                    <h4>Gráfica del máximo, mínimo y media por meses en dicho distrito:</h4>
                    <img src="./img/02-calculosMeses$distrito.png">

                    <p>Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                </div>
                
            </body>
            </html>
        """.trimIndent()
        }
    }
