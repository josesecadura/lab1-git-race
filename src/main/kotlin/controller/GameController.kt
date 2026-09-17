package es.unizar.webeng.hello.controller

import es.unizar.webeng.hello.service.GameService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@Controller
class GameController(
    private val gameService: GameService
) {

    @GetMapping("/game")
    fun game(
        model: Model
    ): String {

        // Al entrar en /game empieza una partida nueva
        gameService.resetStats()

        model.addAttribute("name", "Student")
        model.addAttribute("gameStarted", false)

        return "game"
    }


    @PostMapping("/game/play")
    fun play(
        @RequestParam name: String,
        @RequestParam choice: String,
        @RequestHeader("Accept-Language", defaultValue = "en") language: String,
        model: Model
    ): String {

        // Obtener solamente el código principal del idioma
        val languageCode =
            language.substringBefore("-").substringBefore(",")

        // Convertir la elección recibida a Choice
        val playerChoice =
            GameService.Choice.valueOf(choice.uppercase())

        // Elegir la jugada del ordenador
        val computerChoice =
            gameService.getComputerChoice()

        // Determinar quién gana
        val result =
            gameService.determineWinner(
                playerChoice,
                computerChoice
            )

        // Obtener estadísticas
        val stats =
            gameService.getStats()


        // Datos comunes del juego
        model.addAttribute("name", name)
        model.addAttribute("gameStarted", true)
        model.addAttribute("playerChoice", playerChoice)
        model.addAttribute("computerChoice", computerChoice)
        model.addAttribute("playerWins", stats.first)
        model.addAttribute("computerWins", stats.second)


        // Textos según idioma
        if (languageCode == "es") {

            model.addAttribute("gameTitle", "Piedra, Papel o Tijera")
            model.addAttribute("welcomeMessage", "¡Bienvenido al juego!")
            model.addAttribute(
                "chooseMessage",
                "Introduce tu nombre y elige tu movimiento."
            )

            model.addAttribute("rockText", "Piedra")
            model.addAttribute("paperText", "Papel")
            model.addAttribute("scissorsText", "Tijera")

            model.addAttribute("resultTitle", "Resultado")
            model.addAttribute("playerLabel", "Jugador:")
            model.addAttribute("yourChoiceLabel", "Tu elección:")
            model.addAttribute(
                "computerChoiceLabel",
                "Elección del ordenador:"
            )

            model.addAttribute("statisticsTitle", "Estadísticas")
            model.addAttribute("computerText", "Ordenador")
            model.addAttribute("winsText", "Victorias")

            model.addAttribute("newGameText", "Nueva partida")
            model.addAttribute("backHomeText", "Volver al inicio")


            // Mostrar las elecciones en español
            model.addAttribute(
                "playerChoice",
                translateChoice(playerChoice, true)
            )

            model.addAttribute(
                "computerChoice",
                translateChoice(computerChoice, true)
            )


            // Mostrar el resultado en español
            model.addAttribute(
                "result",
                translateResult(result, true)
            )

        } else {

            model.addAttribute("gameTitle", "Rock Paper Scissors")
            model.addAttribute("welcomeMessage", "Welcome to the game!")
            model.addAttribute(
                "chooseMessage",
                "Enter your name and choose your move."
            )

            model.addAttribute("rockText", "Rock")
            model.addAttribute("paperText", "Paper")
            model.addAttribute("scissorsText", "Scissors")

            model.addAttribute("resultTitle", "Game Result")
            model.addAttribute("playerLabel", "Player:")
            model.addAttribute("yourChoiceLabel", "Your choice:")
            model.addAttribute(
                "computerChoiceLabel",
                "Computer choice:"
            )

            model.addAttribute("statisticsTitle", "Statistics")
            model.addAttribute("computerText", "Computer")
            model.addAttribute("winsText", "Wins")

            model.addAttribute("newGameText", "New Game")
            model.addAttribute("backHomeText", "Back to Home")


            // En inglés no necesitamos traducir las elecciones
            model.addAttribute(
                "playerChoice",
                playerChoice.toString()
            )

            model.addAttribute(
                "computerChoice",
                computerChoice.toString()
            )


            // Mostrar resultado en inglés
            model.addAttribute(
                "result",
                translateResult(result, false)
            )
        }

        return "game"
    }


    // Traducir las elecciones
    private fun translateChoice(
        choice: GameService.Choice,
        spanish: Boolean
    ): String {

        if (!spanish) {
            return choice.toString()
        }

        return when (choice) {
            GameService.Choice.ROCK -> "PIEDRA"
            GameService.Choice.PAPER -> "PAPEL"
            GameService.Choice.SCISSORS -> "TIJERA"
        }
    }


    // Traducir el resultado
    private fun translateResult(
        result: String,
        spanish: Boolean
    ): String {

        return if (spanish) {

            when (result) {
                "PLAYER" -> "¡Has ganado!"
                "COMPUTER" -> "¡Has perdido!"
                "DRAW" -> "¡Empate!"
                else -> ""
            }

        } else {

            when (result) {
                "PLAYER" -> "You win!"
                "COMPUTER" -> "You lose!"
                "DRAW" -> "It's a draw!"
                else -> ""
            }
        }
    }
}