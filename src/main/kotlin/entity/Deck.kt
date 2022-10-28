package entity

import kotlin.random.Random

/**
 * Data structure that holds [SchwimmenCard] objects and provides stack-like
 * access to them (with e.g. [putOnTop], [extract]).
 *
 * @param [random] can be provided to ensure deterministic behavior of [shuffle]
 */
class Deck(private val random: Random = Random) {

    /**
     * The actual backing data structure. Game Deck as a list.
     */
    private var cards: MutableList<SchwimmenCard> = mutableListOf()

    /**
     * sets [cards] list
     *
     * @param drawCards the list of cards to set
     */
    fun setCards(drawCards: MutableList<SchwimmenCard>) { cards = drawCards }

    /**
     * the amount of cards in this stack
     */
    val size: Int get() = cards.size

    /**
     * Returns `true` if the stack is empty, `false` otherwise.
     */
    val empty: Boolean get() = cards.isEmpty()

    /**
     * Shuffles the cards in this stack
     */
    fun shuffle() {
        cards.shuffle(random)
    }

    /**
     * Removes all elements from the [cards] list
     */
    fun clear() {
        cards.clear()
    }

    /**
     * Extracts [amount] cards from this stack.
     *
     * @param amount the number of cards to extract; defaults to 1 if omitted.
     *
     * @throws IllegalArgumentException if not enough cards on deck to extract the desired amount.
     */
    fun extract(amount: Int = 1): MutableList<SchwimmenCard> {
        require(amount in 1 .. cards.size) {"can't extract $amount cards from $cards"}
        return MutableList(amount) {cards.removeFirst()}
    }

    /**
     * Extract three cards from this stack. Convenience wrapper for [extract] with
     * amount parameter equal to 3
     */
    fun extractThreeCards(): MutableList<SchwimmenCard> = extract(3)

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [extract] if you want the card also to be removed.
     */
    fun peek() : SchwimmenCard = cards.first()

    /**
     * provides a view of the full stack contents without changing it. Use [extract]
     * for actually extracting cards from this stack.
     */
    fun peekAll(): List<SchwimmenCard> = cards.toList()


    /**
     * puts a given list of cards on top of this card stack, so that
     * the last element of the passed parameter [cards] will be on top of
     * the stack afterwards.
     */
    fun putOnTop(cards: List<SchwimmenCard>) {
        cards.forEach(this.cards::add)
    }

    /**
     * puts the given card on top of this card stack
     */
    fun putOnTop(card: SchwimmenCard) {
        cards.add(card)
    }

    override fun toString(): String = cards.toString()
}
