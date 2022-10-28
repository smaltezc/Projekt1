package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Implementation of the BGW [BoardGameApplication] for the example card game "Schwimmen"
 */
class SopraApplication : BoardGameApplication("SoPra Game"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the gameFinishedMenuScene
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This is where the actual game takes place
    private val gameScene = SchwimmenGameScene(rootService)

    // This menu scene is shown after each finished game (i.e. no more cards to draw)
    private val gameFinishedMenuScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            this@SopraApplication.showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    init {

        // all scenes and the application itself need to
        // react for changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            gameFinishedMenuScene,
            newGameMenuScene
        )

        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer
        val playerNames = listOf("Bob", "Alice", "Veli", "Deli").toMutableList()
        rootService.gameService.startNewGame(playerNames)

        this.showMenuScene(newGameMenuScene, 0)
        this.showGameScene(gameScene)
    }

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(gameFinishedMenuScene)
    }

}

