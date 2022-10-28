package entity

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Checks functionality of checkHandScore a certain set of cards. A positive assertion
 * indicates a proper function of [SchwimmenPlayer.checkHandScore].
 */
internal class SchwimmenPlayerTest {



    /**
     * Checks whether the points of a set of cards are correctly calculated.
     *
     * Not all suits are tested! But enough combinations of cards to assert correctness of calculation.
     */

    @Test
    fun checkHandScoreWithSetCards()
    {

        val c1 = SchwimmenCard(CardSuit.SPADES, CardValue.KING)
        val c2 = SchwimmenCard(CardSuit.SPADES, CardValue.QUEEN)
        val c3 = SchwimmenCard(CardSuit.SPADES, CardValue.ACE)

        val c4 = SchwimmenCard(CardSuit.CLUBS, CardValue.KING)
        val c5 = SchwimmenCard(CardSuit.SPADES, CardValue.QUEEN)
        val c6 = SchwimmenCard(CardSuit.HEARTS, CardValue.ACE)
        val c7 = SchwimmenCard(CardSuit.DIAMONDS, CardValue.KING)

        //All cards have the same suit.
        val player = SchwimmenPlayer("Player 1", listOf(c1,c2,c3).toMutableList())

        //All cards have a different suits, but same cards as Player 1.
        val player2 = SchwimmenPlayer("Player 2",listOf(c4,c5,c6).toMutableList())

        //Cards 1 and 2 have the same suit.
        val player3 = SchwimmenPlayer("Player 3", listOf(c1,c5,c6).toMutableList())

        //Cards 1 and 3 have the same suit.
        val player4 = SchwimmenPlayer("Player 4",listOf(c1,c4,c3).toMutableList())

        //Cards 2 and 3 have the same suit.
        val player5 = SchwimmenPlayer("Player 5", listOf(c4,c1,c2).toMutableList())

        //All cards have a different suit, but have the same value.
        val player6 = SchwimmenPlayer("Player 6", listOf(c1,c4,c7).toMutableList())


        assertEquals(31.0, player.checkHandScore(player.handCards))
        assertEquals(11.0, player2.checkHandScore(player2.handCards))
        assertEquals(20.0, player3.checkHandScore(player3.handCards))
        assertEquals(21.0, player4.checkHandScore(player4.handCards))
        assertEquals(20.0, player5.checkHandScore(player5.handCards))
        assertEquals(30.5, player6.checkHandScore(player6.handCards))

    }


}