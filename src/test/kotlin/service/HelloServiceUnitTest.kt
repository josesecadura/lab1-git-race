// Test para el HelloService comprobando que devuelve bien segun idioma y hora

package es.unizar.webeng.hello.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

// Probamos el metodo del service en local sin levantar el servidor solo para ver si 
// funciona bien dependiendo de la hora y el idioma por cabecera
class HelloServiceUnitTests {

    private val service = HelloService()

    // Español
    @Test
    fun `should return Spanish greetings depending on hour`() {
        assertThat(service.getGreeting("Jose", "es", 6))
            .isEqualTo("¡Buenos días, Jose!")
        assertThat(service.getGreeting("Jose", "es", 11))
            .isEqualTo("¡Buenos días, Jose!")

        assertThat(service.getGreeting("Jose", "es", 8))
            .isEqualTo("¡Buenos días, Jose!")

        assertThat(service.getGreeting("Jose", "es", 12))
            .isEqualTo("¡Buenas tardes, Jose!")
        assertThat(service.getGreeting("Jose", "es", 14))
            .isEqualTo("¡Buenas tardes, Jose!")

        assertThat(service.getGreeting("Jose", "es", 19))
            .isEqualTo("¡Buenas tardes, Jose!")
        assertThat(service.getGreeting("Jose", "es", 20))
            .isEqualTo("¡Buenas noches, Jose!")
        assertThat(service.getGreeting("Jose", "es", 22))
            .isEqualTo("¡Buenas noches, Jose!")
    }

    // Frances
    @Test
    fun `should return French greetings depending on hour`() {
        assertThat(service.getGreeting("Jose", "fr", 8))
            .isEqualTo("Bonjour, Jose!")

        assertThat(service.getGreeting("Jose", "fr", 14))
            .isEqualTo("Bon après-midi, Jose!")

        assertThat(service.getGreeting("Jose", "fr", 22))
            .isEqualTo("Bonne nuit, Jose!")
    }

    // Ingles
    @Test
    fun `should return English greetings depending on hour`() {
        assertThat(service.getGreeting("Jose", "en", 8))
            .isEqualTo("Good morning, Jose!")

        assertThat(service.getGreeting("Jose", "en", 14))
            .isEqualTo("Good afternoon, Jose!")

        assertThat(service.getGreeting("Jose", "en", 22))
            .isEqualTo("Good night, Jose!")
    }

    // Lenguaje no soportado usamos el ingles

    @Test
    fun `should use English for unsupported language`() {
        assertThat(service.getGreeting("Jose", "it", 8))
            .isEqualTo("Good morning, Jose!")
    }

    // Comprobar que los saludos se guardan en la cola
    @Test
    fun `should store recent greetings`() {
        val service = HelloService()

        service.getGreeting("Jose", "es", 8)
        service.getGreeting("Ana", "en", 14)
        service.getGreeting("Luis", "es", 22)

        val greetings = service.getLastGreetings()

        assertThat(greetings).hasSize(3)
        assertThat(greetings[0].name).isEqualTo("Luis")
        assertThat(greetings[1].name).isEqualTo("Ana")
        assertThat(greetings[2].name).isEqualTo("Jose")
    }

    // Comprobar se guardan maximo los ultimos 13
    @Test
    fun `should keep only the last 13 greetings`() {
        val service = HelloService()

        for (i in 1..15) {
            service.getGreeting("User$i", "es", 8)
        }

        val greetings = service.getLastGreetings()

        assertThat(greetings).hasSize(13)
        assertThat(greetings[0].name).isEqualTo("User15")
        assertThat(greetings[12].name).isEqualTo("User3")
    }
}