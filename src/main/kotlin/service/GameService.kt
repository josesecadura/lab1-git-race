/*
 * Service para el juego de Piedra, Papel o Tijera
 *
 * Autor: Jose Secadura Del Olmo NIP: 815327
 */

package es.unizar.webeng.hello.service

import org.springframework.stereotype.Service

@Service
class GameService {

    enum class Choice {
        ROCK, PAPER, SCISSORS
    }

    private var playerWins: Int = 0
    private var computerWins: Int = 0

    // Elegir al azar entre piedra, papel o tijera
    fun getComputerChoice(): Choice {
        return Choice.entries.random()
    }

    // Obtener las estadísticas de la partida
    fun getStats(): Pair<Int, Int> {
        return Pair(playerWins, computerWins)
    }

    // Reiniciar las estadísticas
    fun resetStats() {
        playerWins = 0
        computerWins = 0
    }

    // Determinar el ganador de la partida
    fun determineWinner(
        playerChoice: Choice,
        computerChoice: Choice
    ): String {

        return when {

            playerChoice == computerChoice -> {
                "DRAW"
            }

            (playerChoice == Choice.ROCK &&
                    computerChoice == Choice.SCISSORS) ||

            (playerChoice == Choice.PAPER &&
                    computerChoice == Choice.ROCK) ||

            (playerChoice == Choice.SCISSORS &&
                    computerChoice == Choice.PAPER) -> {

                playerWins++
                "PLAYER"
            }

            else -> {
                computerWins++
                "COMPUTER"
            }
        }
    }
}