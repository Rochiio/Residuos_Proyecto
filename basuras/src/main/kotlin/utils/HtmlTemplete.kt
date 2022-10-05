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
                    <p class="resultados">$numeroContenedoresTipoDistrito</p>
                    
                    <h4>Media de contenedores de cada tipo que hay en cada distrito: </h4>
                    <p class="resultados">$mediaContenedoresTipoDistrito</p>

                    <br>

                    <h4>Gráfico con el total de contenedores por distrito:</h4>
                    <img src="./img/01-totalContenedoresDistrito.png">

                    <br>

                    <h4>Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
                    distrito:</h4>
                    <p class="resultados">$mediaToneladasAnuales/p>

                    <br>

                    <h4>Gráfico de media de toneladas mensuales de recogida de basura por distrito:</h4>
                    <img src="./img/02-mediaToneladasMensuales.png">

                    <br>

                    <h4>Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
                    de basura agrupadas por distrito:</h4>
                    <p class="resultados">$maxMinMediaDesv</p>

                    <br>

                    <h4>Suma de todo lo recogido en un año por distrito:</h4>
                    <p class="resultados">$sumaRecogidoDistrito</p>

                    <br>

                    <h4>Por cada distrito obtener para cada tipo de residuo la cantidad recogida:</h4>
                    <p class="resultados">$porDistritoTipoResiduoCantidad</p>

                    <br>

                    <p>Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                </div>
                
            </body>
            </html>
        """.trimIndent()



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
        
                <h4>Número de contenedores de cada tipo que hay en cada distrito: </h4>
                <p class="resultados">$numeroContenedoresTipoDistrito</p>
        
                <br>
                
                <h4>Media de contenedores de cada tipo que hay en cada distrito: </h4>
                <p class="resultados">$mediaContenedoresTipoDistrito</p>
        
                <br>
        
                <h4>Gráfico con el total de contenedores por distrito:</h4>
                <img src="./img/01-totalContenedoresDistrito.png">
        
                <br>
        
                <h4>Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
                distrito:</h4>
                <p class="resultados">$mediaToneladasAnuales</p>
        
                <br>
        
                <h4>Gráfico de media de toneladas mensuales de recogida de basura por distrito:</h4>
                <img src="./img/02-mediaToneladasMensuales.png">
        
                <br>
        
                <h4>Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
                de basura agrupadas por distrito:</h4>
                <p class="resultados">$maxMinMediaDesv</p>
        
                <br>
        
                <h4>Suma de todo lo recogido en un año por distrito:</h4>
                <p class="resultados">$sumaRecogidoDistrito</p>
        
                <br>
        
                <h4>Por cada distrito obtener para cada tipo de residuo la cantidad recogida:</h4>
                <p class="resultados">$porDistritoTipoResiduoCantidad</p>
        
                <br>
        
                <p id="tiempo">Tiempo de generación del mismo en milisegundos: <b>$tiempoGeneracion</b></p>
                
        
            </div>
            
        </body>
        </html>
        """.trimIndent()
        }
    }
}