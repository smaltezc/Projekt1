package service

import entity.SchwimmenCard
import entity.SchwimmenGame
import entity.SchwimmenPlayer

/**
 * Service layer class that provides the logic for the two possible actions a player
 * can take in War: drawing from the left stack or drawing from right stack.
 */
@Suppress("NAME_SHADOWING")
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * swaps the given [SchwimmenCard]s
     *
     * @param handCard chosen by the current [player]
     * @param tableCard chosen by the current [player]
     * @param game current game used to get all tableCards
     * @param player current player used to get all handCards
     *
     */
    private fun changeOneCard (handCard: SchwimmenCard, tableCard: SchwimmenCard,
                               game: SchwimmenGame, player: SchwimmenPlayer) {

        var tmp = handCard
        val handCards = player.handCards
        val tableCards = game.tableCards

        // index finding
        for(i in 0 until handCards.size){
            if(player.handCards[i] == handCard){
                tmp = player.handCards[i]
                player.handCards[i] = tableCard
            }
        }

        // index finding
        for(i in 0 until tableCards.size){
            if(game.tableCards[i] == tableCard){
                game.tableCards[i] = tmp
            }
        }
    }

    /**
     * swaps the hand cards and table cards of the current player.
     *
     * @param game current game used to get tableCards
     * @param player current player used to get handCards
     */
    private fun changeAllCards (game: SchwimmenGame, player: SchwimmenPlayer) {

        val tmp = player.handCards
        player.handCards = game.tableCards
        game.tableCards = tmp
    }

    /**
     * If a player does not want to change cards, he can pass. When all players have passed in turn, the three cards
     * are discarded from the center and three new cards are placed in the center from the drawCards.
     *
     * @param game current game used to get tableCards
     */
    private fun pass(game: SchwimmenGame){
        game.passCounter++

        if(game.knockCounter > 0) game.knockCounter++
    }

    /**
     * After a player has knocked, all other players have exactly one more turn. After that, the game is over.
     *
     */
    private fun knock(game : SchwimmenGame){
        game.knockCounter++

        game.passCounter = 0
    }

    /**
     * @param t the possible actions of the current player in a round
     *
     * @see Turn
     */
    fun turn(t: Turn) {
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.currentPlayer

        val handCards = player.handCards
        val tableCards = game.tableCards

        var hC = player.handCards[0]
        var tC = player.handCards[0]

        var turnString = ""

        when(t){
            is Turn.Pass -> {
                this.pass(game)
                turnString = "pass"
            }

            is Turn.Knock -> {
                this.knock(game)
                player.hasKnocked = true
                turnString = "knock"
            }

            is Turn.ChangeAll -> {
                this.changeAllCards(game, player)
                if(game.knockCounter > 0) game.knockCounter++
                turnString = "changeAll"
            }

            is Turn.ChangeOne -> {
                val toSwitchHand = t.handCard
                val toSwitchTable = t.tableCard

                hC = toSwitchHand
                tC = toSwitchTable

                this.changeOneCard(toSwitchHand, toSwitchTable, game, player)
                if(game.knockCounter > 0) game.knockCounter++
                turnString = "changeOne"
            }
        }

        val playerAmount = game.players.size
        var newTableCards = emptyList<SchwimmenCard>().toMutableList()

        //After a player has knocked, all other players have exactly one more turn. After that, the game is over.
        if (game.knockCounter >= playerAmount) this.rootService.gameService.endTheGame()

        val passCount = game.passCounter
        if(game.passCounter >= playerAmount){
            //when all players have passed, there are not enough cards left in the draw pile to reload the center
            // with three cards. In this case, the game ends immediately.
            if(game.drawCards.size < 3) {
                this.rootService.gameService.endTheGame()
            }
            else{
                // discard the tableCards
                game.tableCards.clear()
                // extract 3 new cards from drawCards
                game.tableCards = game.drawCards.extractThreeCards()
                newTableCards = game.tableCards // for refreshAfterTurn Pass
                game.passCounter = 0
            }

        }

        onAllRefreshables {
            when(turnString){
                "pass" -> {refreshAfterTurn(passCount, newTableCards)}
                "knock" -> {refreshAfterTurn()}
                "changeOne" -> { refreshAfterTurn(game, hC, tC) }
                "changeAll" -> { refreshAfterTurn(game, handCards, tableCards) }
            }
        }

        if(!this.rootService.gameService.isGameEnded()){
            val players = game.players

            // set next player
            when(players.size){
                2 -> {
                    when(player){
                        players[0] -> { game.currentPlayer = players[1] }
                        players[1] -> { game.currentPlayer = players[0] }
                    }
                }
                3 -> {
                    when(player){
                        players[0] -> { game.currentPlayer = players[1] }
                        players[1] -> { game.currentPlayer = players[2] }
                        players[2] -> { game.currentPlayer = players[0] }
                    }
                }
                4 -> {
                    when(player){
                        players[0] -> { game.currentPlayer = players[1] }
                        players[1] -> { game.currentPlayer = players[2] }
                        players[2] -> { game.currentPlayer = players[3] }
                        players[3] -> { game.currentPlayer = players[0] }
                    }
                }
            }
        }
        else{
            onAllRefreshables { refreshAfterGameEnd() }
        }
    }
}