package entity

/**
 * Entity class that represents a game state of "Schwimmen".
 *
 * @param players is a list containing all [SchwimmenPlayer] objects that were specified when starting the game.
 * The list may only contain 2-4 players!
 * @param passCounter is a counter that counts how many times a player has passed. Necessary for changing the
 * cards on the table or to end the game.
 * @param tableCards is a list containing three [SchwimmenCard] objetcs, that will be displayed in the middle of the table.
 * @param drawCards is a list containing all 32 cards that will be used in the game.
 */
data class SchwimmenGame(val players: MutableList<SchwimmenPlayer>,
                         var passCounter: Int = 0,
                         var tableCards: MutableList<SchwimmenCard>,
                         var drawCards: Deck) {

    /**
     * The currently active player.
     */
    var currentPlayer : SchwimmenPlayer = players[0]

    /**
     * The amount of knock.
     */
    var knockCounter : Int = 0

}