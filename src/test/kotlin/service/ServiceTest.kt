package service

import kotlin.test.*

/**
 * Class that provides tests for [GameService] and [PlayerActionService] (both at the same time,
 * as their functionality is not easily separable) by basically playing through some sample games.
 * [TestRefreshable] is used to validate correct refreshing behavior even though no GUI
 * is present.
 */
class ServiceTest {

    private val twoPlayers = listOf("Bob", "Alice").toMutableList()
    private val threePlayers = listOf("Alice", "Alex", "Bob").toMutableList()
    private val fourPlayers = listOf("Alex", "Mia","Bob", "Alice").toMutableList()

    /**
     * Tests the default case of game ending: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testIsGameEnded(){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertNull(mc.currentGame)
        mc.gameService.startNewGame(threePlayers)
        assertNotNull(mc.currentGame)

        assertFalse(testRefreshable.refreshAfterGameEndCalled)
        repeat(3){
            mc.playerActionService.turn(Turn.Knock)
        }
        assertTrue(testRefreshable.refreshAfterGameEndCalled)
        assertEquals(8,mc.currentGame!!.knockCounter)
        assertEquals(0,mc.currentGame!!.drawCards.size)
        assertEquals(true, mc.gameService.isGameEnded())

    }

    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testTurn(){
        testTurnWithDifferentPlayerAmount(twoPlayers)
        testTurnWithDifferentPlayerAmount(threePlayers)
        testTurnWithDifferentPlayerAmount(fourPlayers)
    }
    private fun testTurnWithDifferentPlayerAmount(players: MutableList<String>){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        when(players.size){
            2 -> {
                assertNull(mc.currentGame)
                mc.gameService.startNewGame(players)
                assertNotNull(mc.currentGame)

                // changeOne
                val player = mc.currentGame!!.currentPlayer
                val tempOneHandCard = player.handCards[1]
                val tempOneTableCard = mc.currentGame!!.tableCards[1]
                assertEquals("Bob", mc.currentGame!!.currentPlayer.playerName)

                assertFalse(testRefreshable.refreshAfterTurnCalled)
                mc.playerActionService.turn(Turn.ChangeOne(tempOneHandCard, tempOneTableCard ))
                assertTrue(testRefreshable.refreshAfterTurnCalled)

                assertEquals("Alice", mc.currentGame!!.currentPlayer.playerName)
                assertEquals(tempOneTableCard, player.handCards[1])
                assertEquals(tempOneHandCard, mc.currentGame!!.tableCards[1])
            }
            3 -> {
                assertNull(mc.currentGame)
                mc.gameService.startNewGame(players)
                assertNotNull(mc.currentGame)

                // changeOne
                val player = mc.currentGame!!.currentPlayer
                val tempOneHandCard = player.handCards[1]
                val tempOneTableCard = mc.currentGame!!.tableCards[1]
                assertEquals("Alice", mc.currentGame!!.currentPlayer.playerName)

                assertFalse(testRefreshable.refreshAfterTurnCalled)
                mc.playerActionService.turn(Turn.ChangeOne(tempOneHandCard, tempOneTableCard ))
                assertTrue(testRefreshable.refreshAfterTurnCalled)

                assertEquals("Alex", mc.currentGame!!.currentPlayer.playerName)
                assertEquals(tempOneTableCard, player.handCards[1])
                assertEquals(tempOneHandCard, mc.currentGame!!.tableCards[1])
            }
            4 -> {
                assertNull(mc.currentGame)
                mc.gameService.startNewGame(players)
                assertNotNull(mc.currentGame)

                // changeOne
                val player = mc.currentGame!!.currentPlayer
                val tempOneHandCard = player.handCards[1]
                val tempOneTableCard = mc.currentGame!!.tableCards[1]
                assertEquals("Alex", mc.currentGame!!.currentPlayer.playerName)

                assertFalse(testRefreshable.refreshAfterTurnCalled)
                mc.playerActionService.turn(Turn.ChangeOne(tempOneHandCard, tempOneTableCard ))
                assertTrue(testRefreshable.refreshAfterTurnCalled)

                assertEquals("Mia", mc.currentGame!!.currentPlayer.playerName)
                assertEquals(tempOneTableCard, player.handCards[1])
                assertEquals(tempOneHandCard, mc.currentGame!!.tableCards[1])
            }
        }

    }


    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testChangeOne(){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertNull(mc.currentGame)
        mc.gameService.startNewGame(threePlayers)
        assertNotNull(mc.currentGame)

        val player = mc.currentGame!!.currentPlayer

        val tempOneHandCard = player.handCards[1]
        val tempOneTableCard = mc.currentGame!!.tableCards[1]

        assertFalse(testRefreshable.refreshAfterTurnCalled)
        mc.playerActionService.turn(Turn.ChangeOne(tempOneHandCard, tempOneTableCard ))
        assertTrue(testRefreshable.refreshAfterTurnCalled)

        assertEquals(tempOneTableCard, player.handCards[1])
        assertEquals(tempOneHandCard, mc.currentGame!!.tableCards[1])
    }

    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testChangeAll(){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertNull(mc.currentGame)
        mc.gameService.startNewGame(threePlayers)
        assertNotNull(mc.currentGame)
        val player = mc.currentGame!!.currentPlayer

        val temph = player.handCards
        val tempt = mc.currentGame!!.tableCards

        assertFalse(testRefreshable.refreshAfterTurnCalled)
        mc.playerActionService.turn(Turn.ChangeAll)

        assertEquals(tempt, player.handCards)
        assertEquals(temph, mc.currentGame!!.tableCards)

    }

    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testPass(){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
        assertNull(mc.currentGame)
        mc.gameService.startNewGame(threePlayers)
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        assertNotNull(mc.currentGame)

        assertEquals(20, mc.currentGame!!.drawCards.size)
        var j = 1
        assertFalse(testRefreshable.refreshAfterTurnCalled)
        repeat(5) {
            mc.playerActionService.turn(Turn.Pass)

            assertEquals(j % 3, mc.currentGame!!.passCounter)

            j++
        }
        assertEquals(17, mc.currentGame!!.drawCards.size)
    }

    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */
    @Test
    fun testStartNewGame() {
        testWithDifferentPlayerAmount(twoPlayers)
        testWithDifferentPlayerAmount(threePlayers)
        testWithDifferentPlayerAmount(fourPlayers)
    }

    private fun testWithDifferentPlayerAmount(players: MutableList<String>){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
        assertNull(mc.currentGame)
        mc.gameService.startNewGame(players)
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        assertNotNull(mc.currentGame)

        when(players.size){
            2 -> {
                for (i in 0 until players.size){
                    assertEquals(3, mc.currentGame!!.players[i].handCards.size)
                    assertEquals(3, mc.currentGame!!.tableCards.size)
                    assertEquals(23, mc.currentGame!!.drawCards.size)
                }
            }
            3 -> {
                for (i in 0 until players.size){
                    assertEquals(3, mc.currentGame!!.players[i].handCards.size)
                    assertEquals(3, mc.currentGame!!.tableCards.size)
                    assertEquals(20, mc.currentGame!!.drawCards.size)
                }

            }
            4 -> {
                for (i in 0 until players.size){
                    assertEquals(3, mc.currentGame!!.players[i].handCards.size)
                    assertEquals(3, mc.currentGame!!.tableCards.size)
                    assertEquals(17, mc.currentGame!!.drawCards.size)
                }
            }
        }

    }

}