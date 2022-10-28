package service

import entity.*

/**
 * Service layer class that provides the logic for actions not directly
 * related to a single player.
 */
class GameService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * Starts a new game (overwriting a currently active one, if it exists)
     *
     * @param playerNames list of names of Players
     * @param allCards optional list of cards to play with.
     */
    fun startNewGame(
        playerNames: MutableList<String>,
        allCards: MutableList<SchwimmenCard> = defaultRandomCardList().toMutableList()
    ) {
        initializePlayers(playerNames, allCards)

        onAllRefreshables {refreshAfterStartNewGame() }

    }

    /**
     * Initializes the players with handCards, tableCards and drawCards.
     *
     * @param playerNames list of names of Players. (also used to get the player Amount)
     * @param allCards optional list of cards to play with. If provided, each player will
     *      receive 3 cards (on their hand cards). If parameter is not provided, a random
     *      deck of 32 cards will be used.
     *
     * @throws IllegalStateException if player Amount is invalid
     */
    private fun initializePlayers(playerNames: MutableList<String>,
                                  allCards: MutableList<SchwimmenCard> = defaultRandomCardList().toMutableList()
    ){

        val playerAmount = playerNames.size

        val drawCards = Deck()
        drawCards.setCards(allCards)

        val tableCards = drawCards.extractThreeCards()

        val p1 = SchwimmenPlayer(playerNames[0], drawCards.extractThreeCards())
        val p2 = SchwimmenPlayer(playerNames[1], drawCards.extractThreeCards())


        when (playerAmount) {
            //initialize 2 players
            2 -> {
                val listOfPlayer : MutableList<SchwimmenPlayer> = listOf(p1,p2).toMutableList()

                val game = SchwimmenGame(listOfPlayer, 0, tableCards, drawCards)

                rootService.currentGame = game
            }//initialize 3 players
            3 -> {
                val p3 = SchwimmenPlayer(playerNames[2], drawCards.extractThreeCards())

                val listOfPlayer : MutableList<SchwimmenPlayer> = listOf(p1,p2,p3).toMutableList()

                val game = SchwimmenGame(listOfPlayer, 0, tableCards, drawCards)

                rootService.currentGame = game
            }//initialize 4 players
            4 -> {
                val p3 = SchwimmenPlayer(playerNames[2], drawCards.extractThreeCards())
                val p4 = SchwimmenPlayer(playerNames[3], drawCards.extractThreeCards())

                val listOfPlayer : MutableList<SchwimmenPlayer> = listOf(p1,p2,p3,p4).toMutableList()

                val game = SchwimmenGame(listOfPlayer, 0, tableCards, drawCards)

                rootService.currentGame = game
            }
            else -> {
                throw IllegalStateException("PlayerAmount must be between 2 and 4. $playerAmount is invalid here.")
            }
        }
    }

    /**
     * ends the game
     */
    fun endTheGame(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}

        game.knockCounter = 8
        game.drawCards.clear()

    }

    /**
     * Checks if the game is over, which is the case when drawCards are empty
     * and [SchwimmenGame.knockCounter] is 8.
     *
     * @return True, if drawCards empty and knockCounter 8
     */
    fun isGameEnded() : Boolean {

        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}

        val drawCardsEmpty = game.drawCards.empty
        var isKnockCounterEight = false
        if(game.knockCounter == 8) isKnockCounterEight = true

        return drawCardsEmpty && isKnockCounterEight
    }

    /**
     * Creates a shuffled 32 cards list of all four suits and cards
     * from 7 to Ace
     */
    private fun defaultRandomCardList() = MutableList(32) { index ->
        SchwimmenCard(
            CardSuit.values()[index / 8],
            CardValue.values()[(index % 8) + 5]
        )
    }.shuffled()

}