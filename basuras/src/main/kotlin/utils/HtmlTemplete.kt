package utils

class HtmlTemplete(
    val distrito: String,
    val fechaHora: String, //Ya formateada a español
    val numeroContenedoresTipoDistrito: Int,
    val mediaContenedoresTipoDistrito: Int,
    val mediaToneladasAnuales: String,
    val maxMinMediaDesv: String,
    val sumaRecogidoDistrito: String,
    val porDistritoTipoResiduoCantidad: String,
    val tiempoGeneracion: Long
) {

    /**
     * Generación html para la opción RESUMEN
     */
    fun generateHtmlResumen():String{
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
                    <h5>Número de contenedores de cada tipo que hay en cada distrito: $numeroContenedoresTipoDistrito</h5>
                    <h5>Media de contenedores de cada tipo que hay en cada distrito: $mediaContenedoresTipoDistrito</h5>

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
}