package view

import entity.SchwimmenCard
import entity.SchwimmenGame
import service.AbstractRefreshingService

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}

    /**
     * perform refreshes that are necessary after a player has changed the tableCards and handCards
     */
    fun refreshAfterTurn(game: SchwimmenGame, handCards : MutableList<SchwimmenCard>,
                         tableCards : MutableList<SchwimmenCard>) {}

    /**
     * perform refreshes that are necessary after a player has changed one tableCard and one handCard
     */
    fun refreshAfterTurn(game: SchwimmenGame, handCard : SchwimmenCard, tableCard : SchwimmenCard) {}

    /**
     * perform refreshes that are necessary after a player has passed
     */
    fun refreshAfterTurn(passCount : Int, newTableCards : MutableList<SchwimmenCard>) {}

    /**
     * perform refreshes that are necessary after a player has knocked
     */
    fun refreshAfterTurn() {}

    /**
     * perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterGameEnd() {}

}