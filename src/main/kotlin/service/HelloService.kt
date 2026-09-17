/*  
    * Service para el saludo elegir entre buenos dias/tardes/noches 
    * dependiendo de la hora además del idioma
    * Autor: Jose Secadura Del Olmo NIP: 815327
*/


// Recibiera el nombre, recogerá el idioma y hora y devolvera el saludo que toque

class HelloService {
    // Language lo cogemos de la petición http que nos llega al llamar al endpoint
    fun getGreeting(name: String, language: String, hour: Int): String {
        // greeting var de saludo que devolvemos segun lang
        val greeting = when (language) {
            "es" -> when (hour) {
                in 6..11 -> "¡Buenos días, $name!"
                in 12..19 -> "¡Buenas tardes, $name!"
                else -> "¡Buenas noches, $name!"
            }
            "fr" -> when (hour) {
                in 6..11 -> "Bonjour, $name!"
                in 12..19 -> "Bon après-midi, $name!"
                else -> "Bonne nuit, $name!"
            }
            else -> when (hour) {
                in 6..11 -> "Good morning, $name!"
                in 12..19 -> "Good afternoon, $name!"
                else -> "Good night, $name!"
            }
        }
        return greeting
    }
}
