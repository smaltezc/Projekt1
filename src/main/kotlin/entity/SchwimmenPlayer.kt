package entity

/**
 * Entity to represent a player in the game "Schwimmen". Besides having a [playerName] and
 * the player just consists of [handCards]
 *
 * @param handCards list of cards that will initially be put on the player's hand deck
 */
class SchwimmenPlayer( val playerName: String,
                       var handCards: MutableList<SchwimmenCard>
) {
    override fun toString(): String =
        "$playerName: H$handCards"

    var hasKnocked : Boolean = false

    /**
     * Checks a specified card score of 3 cards.
     *
     * Checks a list of [SchwimmenCard] objects and calculates the score which would be aquired with a 3 card combination.
     * For more details please check https://sopra.cs.tu-dortmund.de/wiki/sopra/22b/projekt1
     *
     * @return a double value containing the score of a 3 card combination.
     */

    fun checkHandScore(list : List<SchwimmenCard>) : Double
    {
        val suit1 = list[0].suit
        val suit2 = list[1].suit
        val suit3 = list[2].suit

        val card1 = list[0]
        val card2 = list[1]
        val card3 = list[2]

        //All suits are the same. All card values need to be added together.
        if(suit1 == suit2 && suit1 == suit3)
        {
            return card1.cardValue()+card2.cardValue()+card3.cardValue()
        }

        //The suits 1 and 2 are the same and the values of these cards are added together.
        else if (suit1 == suit2 )
        {
            return card1.cardValue()+card2.cardValue()
        }

        //The suits of card 1 and 3 are the same. These cards' values are added together accordingly.
        else if(suit1 == suit3 )
        {
            return card1.cardValue() + card3.cardValue()
        }

        //The suits of card 2 and 3 are the same. These cards' values are added together accordingly.
        else if(suit2 == suit3 )
        {
            return card2.cardValue()+card3.cardValue()
        }

        //All suits are different. The card values are compared. If all 3 cards have the same original enum value,
        //they result in 30.5 points. Otherwise the highest card value will be calculated and returned.
        else
        {
            //All 3 cards have the same enum value.
            if(card1.value == card2.value && card1.value == card3.value)
            {
                return 30.5
            }

            //All suits and values are different and the maximum value is being calculated.
            else
            {

                return if (card1.value > card2.value && card1.value > card3.value) {
                    card1.cardValue()
                }
                else if (card2.value > card3.value) {
                    card2.cardValue()
                }
                else {
                    card3.cardValue()
                }


            }
        }

    }
}