package service

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenCard
import entity.Deck
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertSame

/**
 * Test cases for [Deck]
 */
class DeckTest {

    private val c1 = SchwimmenCard(CardSuit.SPADES, CardValue.ACE)
    private val c2 = SchwimmenCard(CardSuit.CLUBS, CardValue.JACK)
    private val c3 = SchwimmenCard(CardSuit.HEARTS, CardValue.QUEEN)

    /**
     * Ensure stack behavior (i.e., if cards put on top are extracted/peeked next)
     * Also tests if putting a list on top results in the last card of this list
     * being on top of the stack afterwards.
     */
    @Test
    fun testOrder() {

        val stack = Deck()

        stack.putOnTop(listOf(c1, c2, c3))

        assertSame(stack.peek(), c1)
        assertSame(stack.extract().first(), c1)
        assertSame(stack.peek(), c2)
        assertEquals(stack.size, 2)

        stack.putOnTop(c1)
        assertSame(stack.peek(), c2)
        assertEquals(stack.size, 3)

    }

    /**
     * Test if shuffle works
     */
    @Test
    fun testShuffle() {
        val stack = Deck(random = Random(42))
        stack.putOnTop(listOf(c3, c2, c1))
        stack.shuffle()
        assertEquals(listOf(c2,c3,c1), stack.extract(3))
        assertEquals(0, stack.size)
    }

    /**
     * Test if extracting from an empty stack throws an exception
     */
    @Test
    fun testExtractFail() {
        val stack = Deck()
        assertFails { stack.extract() }
        stack.putOnTop(listOf(c1, c2, c3))
        stack.extractThreeCards()
        assertFails { stack.extract() }
    }

}