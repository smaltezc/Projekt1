package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import service.RootService
import entity.SchwimmenGame
import entity.SchwimmenPlayer
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 * as well as the score. Also, there are two buttons: one for starting a new game and one for
 * quitting the program.
 */
class GameFinishedMenuScene(private val rootService: RootService) : MenuScene(400, 540), Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 30)
    )

    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 125).apply {
        text = ""
        font = Font(size=18, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
        visual = ImageVisual("buttonBG.png")
    }
    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 165).apply {
        text = ""
        font = Font(size=18, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
        visual = ImageVisual("buttonBG.png")
    }
    private val p3Score = Label(width = 300, height = 35, posX = 50, posY = 205).apply {
        text = ""
        font = Font(size=18, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
        visual = ImageVisual("buttonBG.png")
    }
    private val p4Score = Label(width = 300, height = 35, posX = 50, posY = 245).apply {
        text = ""
        font = Font(size=18, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
        visual = ImageVisual("buttonBG.png")
    }
    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 365).apply {
        text = ""
        font = Font(size=18, color = Color.cyan, fontStyle = Font.FontStyle.ITALIC)
        visual = ImageVisual("buttonBG.png")
    }

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 300, text = "Quit").apply {
        visual = ColorVisual(Color(221,136,136))
    }

    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 300, text = "New Game").apply {
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        background = ColorVisual(Color(255, 148, 254))
        opacity = .9
        addComponents(headlineLabel, p1Score, p2Score, p3Score, p4Score, gameResult, newGameButton, quitButton)
    }

    private fun SchwimmenPlayer.scoreString(): String {
        return "${this.playerName} scored ${this.checkHandScore(this.handCards)} points."
    }
    private fun SchwimmenGame.gameResultString(): String {
        val listOfPlayerScores = emptyList<Double>().toMutableList()
        when(players.size){
            2 -> {
                listOfPlayerScores.add(players[0].checkHandScore(players[0].handCards))
                listOfPlayerScores.add(players[1].checkHandScore(players[1].handCards))
            }
            3 -> {
                listOfPlayerScores.add(players[0].checkHandScore(players[0].handCards))
                listOfPlayerScores.add(players[1].checkHandScore(players[1].handCards))
                listOfPlayerScores.add(players[2].checkHandScore(players[2].handCards))
            }
            4 -> {
                listOfPlayerScores.add(players[0].checkHandScore(players[0].handCards))
                listOfPlayerScores.add(players[1].checkHandScore(players[1].handCards))
                listOfPlayerScores.add(players[2].checkHandScore(players[2].handCards))
                listOfPlayerScores.add(players[3].checkHandScore(players[3].handCards))
            }
        }
        var winner = ""
        var max = 0.0
        for(i in 0 until listOfPlayerScores.size){
            if(listOfPlayerScores[i] >= max){
                max = listOfPlayerScores[i]
                winner = players[i].playerName
            }
        }
        var winnerCount = 0
        for(p in listOfPlayerScores){
            if (p == max) winnerCount++
        }
        return when {
            max > 0 && winnerCount == 1 -> "$winner wins the game."
            else -> "Draw. No winner."
        }
    }

    override fun refreshAfterGameEnd() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }

        when(game.players.size){
            2 -> {
                p1Score.text = game.players[0].scoreString()
                p2Score.text = game.players[1].scoreString()
                p3Score.isVisible = false
                p4Score.isVisible = false
            }
            3 -> {
                p1Score.text = game.players[0].scoreString()
                p2Score.text = game.players[1].scoreString()
                p3Score.text = game.players[2].scoreString()
                p3Score.isVisible = true
                p4Score.isVisible = false
            }
            4 -> {
                p1Score.text = game.players[0].scoreString()
                p2Score.text = game.players[1].scoreString()
                p3Score.text = game.players[2].scoreString()
                p4Score.text = game.players[3].scoreString()
                p3Score.isVisible = true
                p4Score.isVisible = true
            }
        }
        gameResult.text = game.gameResultString()
    }
}