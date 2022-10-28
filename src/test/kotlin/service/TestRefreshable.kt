package service

import entity.SchwimmenCard
import entity.SchwimmenGame
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterStartNewGameCalled: Boolean = false
        private set

    var refreshAfterTurnCalled: Boolean = false
        private set

    var refreshAfterGameEndCalled: Boolean = false
        private set

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterTurnCalled = false
        refreshAfterGameEndCalled = false
    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterTurn(game: SchwimmenGame, handCards: MutableList<SchwimmenCard>,
                                  tableCards: MutableList<SchwimmenCard>) {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterTurn(passCount : Int, newTableCards : MutableList<SchwimmenCard>) {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterTurn() {
        refreshAfterTurnCalled = true
    }

    override fun refreshAfterTurn(game: SchwimmenGame, handCard : SchwimmenCard, tableCard : SchwimmenCard)  {
        refreshAfterTurnCalled = true
    }

    override fun refreshAfterGameEnd() {
        refreshAfterGameEndCalled = true
    }


}