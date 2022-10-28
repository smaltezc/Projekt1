package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import service.RootService
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in [GameFinishedMenuScene]. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(400, 540), Refreshable {
    private var playerNumber = 2

    private val gameNameLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "SCHWIMMEN",
        font = Font(size = 32),
    )

    private val p1Label = Label(
        width = 80, height = 35,
        posX = 50, posY = 125,
        text = "Player 1:"
    ).apply {
        visual = ColorVisual.WHITE
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 125,
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    private val p2Label = Label(
        width = 80, height = 35,
        posX = 50, posY = 170,
        text = "Player 2:"
    ).apply {
        visual = ColorVisual.WHITE
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 170,
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val p3Label = Label(
        width = 80, height = 35,
        posX = 50, posY = 215,
        text = "Player 3:"
    ).apply {
        visual = ColorVisual.WHITE
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p3Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 215,
    ).apply {
        isDisabled = true
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val p4Label = Label(
        width = 80, height = 35,
        posX = 50, posY = 260,
        text = "Player 4:"
    ).apply {
        visual = ColorVisual.WHITE
    }

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p4Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 260,
    ).apply {
        isDisabled = true
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val add3Player = Button(
        width = 35, height = 35,
        posX = 75, posY = 215,
    ).apply {
        visual = ImageVisual("plus.png")
        onMouseClicked = {
            isVisible = false
            add4Player.isDisabled = false
            p3Input.opacity = 1.0
            p3Input.isDisabled = false
            playerNumber++
            addComponents(p3Label)
        }
    }

    private val add4Player = Button(
        width = 35, height = 35,
        posX = 75, posY = 260,
    ).apply {
        visual = ImageVisual("plus.png")
        this.isDisabled = true
        onMouseClicked = {
            p4Input.opacity = 1.0
            p4Input.isDisabled = false
            isVisible = false
            playerNumber++
            addComponents(p4Label)
        }
    }

    private val warningLabel = Label(
        width = 40, height = 40,
        posX = 50, posY = 460,
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("warning.png")
    ).apply {
        isVisible = false
    }

    private val warningStr = Label(
        width = 300, height = 40,
        posX = 80, posY = 460,
        text = "minimum 2 players to play!",
        font = Font(size = 20, color = Color.RED, fontStyle = Font.FontStyle.ITALIC),
    ).apply {
        isVisible = false
    }


    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 360,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }


    private val startButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 360,
        text = "Start"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            val players = emptyList<String>().toMutableList()
            if(p1Input.text.trim().isNotEmpty()) players.add(p1Input.text.trim())
            if(p2Input.text.trim().isNotEmpty()) players.add(p2Input.text.trim())
            if (playerNumber > 2) if(p3Input.text.trim().isNotEmpty()) players.add(p3Input.text.trim())
            if (playerNumber > 3) if(p4Input.text.trim().isNotEmpty()) players.add(p4Input.text.trim())

            //Exception here
            if (players.size < 2){
                warningStr.isVisible = true
                warningLabel.isVisible = true
            }
            else{
                rootService.gameService.startNewGame(players)
            }

            p3Input.text = ""
            p4Input.text = ""
            p3Input.isDisabled = true
            add3Player.isVisible = true
            p4Input.isDisabled = true
            add4Player.isVisible = true
            removeComponents(p3Label, p4Label)
        }
    }

    init {
        background = ColorVisual(Color(255, 148, 254))
        opacity = .9
        addComponents(
            gameNameLabel,
            p1Label, p1Input,
            p2Label, p2Input, p3Input,p4Input,
            add3Player, add4Player,
            startButton, quitButton,
            warningLabel, warningStr
        )
    }
}