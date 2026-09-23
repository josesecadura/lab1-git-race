package es.unizar.webeng.hello.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// Service que hemos implementado
import es.unizar.webeng.hello.service.HelloService
// Header de la peticion http para saber el idioma
import org.springframework.web.bind.annotation.RequestHeader
// LocallTime lobtener la hora
import java.time.LocalTime
import es.unizar.webeng.hello.service.GreetingRecord

@Controller
class HelloController(
    @param:Value("\${app.message:Hello World}") 
    private val message: String
) {
    // Endpoint principal al arrancar la web
    @GetMapping("/")
    fun welcome(
        model: Model,
        @RequestParam(defaultValue = "") name: String
    ): String {
        val greeting = if (name.isNotBlank()) "Hello, $name!" else message
        model.addAttribute("message", greeting)
        model.addAttribute("name", name)
        return "welcome"
    }
}

@RestController
class HelloApiController (
    private val helloService: HelloService
    ) {
    // End point al pulsar el test web page y añadir el
    @GetMapping("/api/hello", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun helloApi(
        @RequestParam(defaultValue = "World") name: String, 
        @RequestHeader("Accept-Language", defaultValue = "en") language: String,
        // Para poder pasar la hora como parametro y probar los test
        @RequestParam(required = false) hour: Int?
    ): Map<String, String> {
        // Parseamos el idioma de la cabecera viene como es-X
        val languageCode = language.substringBefore("-").substringBefore(",")
        // Recogemos la hora actual para pasarla al servicio
        val currentHour = hour ?: LocalTime.now().hour
        // Llamamos al servicio para obtener el saludo
        val greeting = helloService.getGreeting(name, languageCode, currentHour)
        return mapOf(
            "message" to greeting,
            "timestamp" to java.time.Instant.now().toString()
        )
    }

    // End point para devolver ult saludos
    @GetMapping("/api/last-greetings", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun lastGreetings(): Map<String, List<GreetingRecord>> {
        val lastGreetings = helloService.getLastGreetings()
        return mapOf("lastGreetings" to lastGreetings)
    }
}
