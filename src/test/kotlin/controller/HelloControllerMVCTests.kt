package es.unizar.webeng.hello.controller

import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import es.unizar.webeng.hello.service.HelloService
import org.springframework.context.annotation.Import

// Simulamos la peticion get para ver si los endpoint realmente responden

// Import del HelloService para poder probar el /api/helo ya que lo contiene el HelloController
@Import(HelloService::class) 
@WebMvcTest(HelloController::class, HelloApiController::class)
class HelloControllerMVCTests {
    @Value("\${app.message:Welcome to the Modern Web App!}")
    private lateinit var message: String

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return home page with default message`() {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("welcome"))
            .andExpect(model().attribute("message", equalTo(message)))
            .andExpect(model().attribute("name", equalTo("")))
    }
    
    @Test
    fun `should return home page with personalized message`() {
        mockMvc.perform(get("/").param("name", "Developer"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("welcome"))
            .andExpect(model().attribute("message", equalTo("Hello, Developer!")))
            .andExpect(model().attribute("name", equalTo("Developer")))
    }
    
    // Test antiguo del endpoint api/hello ahora devuelve saludo segun params
    /* @Test
    fun `should return API response as JSON`() {
        mockMvc.perform(get("/api/hello").param("name", "Test"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo("Hello, Test!")))
            .andExpect(jsonPath("$.timestamp").exists())
    } */

    // Test nuevo para el endpoint modificado
    // Solo comprueba que el endpoint responde mirando si Test esta en el mensaje que devuelve
    // El resto de saber si responde bien segun idioma y hora miramos en el test del service
    // que es el encargado de eso
    @Test
    fun `should return API response as JSON with greeting`() {
        mockMvc.perform(get("/api/hello")
            .param("name", "Test")
            .param("hour", "8") // Paso la hora para poder probar el sal de buenos dias
            .header("Accept-Language", "es-ES"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo("¡Buenos días, Test!")))
            .andExpect(jsonPath("$.timestamp").exists())    
    }
}

