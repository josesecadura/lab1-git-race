/*  
    * Service para el saludo elegir entre buenos dias/tardes/noches 
    * dependiendo de la hora además del idioma
    * Autor: Jose Secadura Del Olmo NIP: 815327
*/
package es.unizar.webeng.hello.service
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.time.format.DateTimeFormatter
// Recibiera el nombre, recogerá el idioma y hora y devolvera el saludo que toque

data class GreetingRecord(
    val name: String,
    val time: String
)

@Service
class HelloService {
    private val greetingQueue: ArrayDeque<GreetingRecord> = ArrayDeque()
    private val maxGreetings = 13

    // Language lo cogemos de la petición http que nos llega al llamar al endpoint
    fun getGreeting(name: String, language: String, hour: Int): String {
        // greeting var de saludo que devolvemos segun lang
        val time = LocalTime.now()
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

        synchronized(greetingQueue) {
            // Añadimos el nombre y la hora del saludo a la cola
            greetingQueue.addFirst(
                GreetingRecord(
                    name = name,
                    time = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                )
            )
            // Comprobamos el tamaño si lo superamos eliminamos el ultimo
            if (greetingQueue.size > maxGreetings) {
                greetingQueue.removeLast()
            }
        }
        return greeting
    }

    // Funcion para devolver los ultimos saludos
    fun getLastGreetings(): List<GreetingRecord> {
        return synchronized(greetingQueue) {
            greetingQueue.toList()
        }
    }
}
