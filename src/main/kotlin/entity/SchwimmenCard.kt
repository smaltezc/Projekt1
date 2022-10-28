package entity

/**
 * Data class for the single typ of game elements that the game "Schwimmen" knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */
data class SchwimmenCard(val suit: CardSuit, val value: CardValue) {

    override fun toString() = "$suit$value"

    /**
     * compares two [SchwimmenCard]s according to the [Enum.ordinal] value of their [CardSuit]
     * (i.e., the order in which the suits are declared in the enum class)
     */
    operator fun compareTo(other: SchwimmenCard) = this.value.ordinal - other.value.ordinal

    /**
     * Assigns a certain value to the [CardValue].
     * @returns the corresponding value of the card.
     */
    fun cardValue(): Double
    {
        return when (this.value) {
            CardValue.SEVEN -> 7.0
            CardValue.EIGHT -> 8.0
            CardValue.NINE -> 9.0
            CardValue.TEN -> 10.0
            CardValue.JACK -> 10.0
            CardValue.QUEEN -> 10.0
            CardValue.KING -> 10.0
            CardValue.ACE -> 11.0

            else -> {
                0.0
            }
        }
    }
}