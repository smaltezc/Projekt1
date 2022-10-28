package service

import entity.SchwimmenCard

/**
 * Sealed class to distinguish between the 4 possible actions of player in the game:
 * [Pass], [ChangeOne], [ChangeAll], [Knock]
 *
 */
sealed class Turn {

    /**
     * @see [PlayerActionService]
     */
    object Pass : Turn()

    /**
     * swaps the given [SchwimmenCard]s
     *
     * @param handCard chosen by the player
     * @param tableCard chosen by the player
     *
     * @see [PlayerActionService]
     */
    data class ChangeOne(val handCard: SchwimmenCard, val tableCard: SchwimmenCard) : Turn()

    /**
     * swaps the player hand [SchwimmenCard]s and table [SchwimmenCard]s
     * @see [PlayerActionService]
     */
    object ChangeAll : Turn()

    /**
     * @see [PlayerActionService]
     */
    object Knock : Turn()
}

