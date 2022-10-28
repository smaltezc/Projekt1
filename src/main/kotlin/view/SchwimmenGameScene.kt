package view

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.gamecomponentviews.CardView
import service.RootService
import entity.*
import service.CardImageLoader
import service.Turn
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * This is the main thing for the War game. The scene shows the complete table at once.
 * Player 1 "sits" is on the bottom half of the screen, player 2 on the top.
 * Each player has four areas for their cards: left and right draw stack, played cards, and collected
 * cards. Clicking on either draw stack triggers the corresponding player's draw left/right action.
 */
class SchwimmenGameScene(private val rootService: RootService) : BoardGameScene(1920, 1080 ),Refreshable {

    private fun SchwimmenPlayer.playerNameString(): String = this.playerName
    private fun SchwimmenGame.drawCardsSizeString(): String = "${this.drawCards.size}"
    private fun SchwimmenPlayer.handScoreString(): String = "${checkHandScore(this.handCards)}"

    private val drawCardsSize = Label(height = 250, width =  150, posX = 1220, posY = 410 ).apply {
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)))
    }

    private val bottomName = Label(height = 250, width =  570, posX = 680, posY = 800 ).apply {
        text = ""
        alignment = Alignment.TOP_CENTER
        font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)
        visual = CompoundVisual(

            ColorVisual(Color(255, 255, 255, 50)))
    }

    private val rightName = Label(height = 250, width =  570, posX = 1470, posY = 420 ).apply {
        text = ""
        alignment = Alignment.TOP_CENTER
        font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)))
    }.apply {
        rotation = -90.0
    }

    private val topName = Label(height = 250, width =  570, posX = 680, posY = 40 ).apply {
        text = ""
        alignment = Alignment.TOP_CENTER
        font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)))
    }.apply {
        rotation = 180.0
    }

    private val leftName = Label(height = 250, width =  570, posX = -110, posY = 420 ).apply {
        text = ""
        alignment = Alignment.TOP_CENTER
        font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)))
    }.apply {
        rotation = 90.0
    }

    private val bottomKnock = Label(
        width = 100, height = 100,
        posX = 570, posY = 830,
        text = "KNOCKED",
        alignment = Alignment.BOTTOM_CENTER,
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("fist.png")
    )

    private val rightKnock = Label(
        width = 100, height = 100,
        posX = 1700, posY = 850,
        text = "KNOCKED",
        alignment = Alignment.BOTTOM_CENTER,
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("fist.png")
    ).apply { rotation = -90.0}

    private val topKnock = Label(
        width = 100, height = 100,
        posX = 1270, posY = 50,
        text = "KNOCKED",
        alignment = Alignment.BOTTOM_CENTER,
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("fist.png")
    ).apply { rotation = 180.0}

    private val leftKnock = Label(
        width = 100, height = 100,
        posX = 100, posY = 135,
        text = "KNOCKED",
        alignment = Alignment.BOTTOM_CENTER,
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("fist.png")
    ).apply { rotation = 90.0}

    private val tableCard0 = LabeledStackView(posX = 760, posY = 440)
    private val tableCard1 = LabeledStackView(posX = 895, posY = 440)
    private val tableCard2 = LabeledStackView(posX = 1030, posY = 440)
    private val drawCards = LabeledStackView(posX = 1230, posY = 440)

    private val bottomHandCards0 = LabeledStackView(posX = 695, posY = 830)
    private val bottomHandCards1 = LabeledStackView(posX = 895, posY = 830)
    private val bottomHandCards2 = LabeledStackView(posX = 1095, posY = 830)

    private val rightHandCards0 = LabeledStackView(posX = 1700, posY = 640, rotate = -90.0)
    private val rightHandCards1 = LabeledStackView(posX = 1700, posY = 440, rotate = -90.0)
    private val rightHandCards2 = LabeledStackView(posX = 1700, posY = 240, rotate = -90.0)

    private val topHandCards0 = LabeledStackView(posX = 1095, posY = 50, rotate = 180.0)
    private val topHandCards1 = LabeledStackView(posX = 895, posY = 50, rotate = 180.0)
    private val topHandCards2 = LabeledStackView(posX = 695, posY = 50, rotate = 180.0)

    private val leftHandCards0 = LabeledStackView(posX = 100, posY = 240, rotate = 90.0)
    private val leftHandCards1 = LabeledStackView(posX = 100, posY = 440, rotate = 90.0)
    private val leftHandCards2 = LabeledStackView(posX = 100, posY = 640, rotate = 90.0)

    //val as = emptyList<LabeledStackView>().toMutableList()
    private val listOfBottomHandCardsLabel = listOf(bottomHandCards0,bottomHandCards1,bottomHandCards2).toMutableList()
    private val listOfRightHandCardsLabel = listOf(rightHandCards0, rightHandCards1, rightHandCards2).toMutableList()
    private val listOfTopHandCardsLabel = listOf(topHandCards0, topHandCards1, topHandCards2).toMutableList()
    private val listOfLeftHandCardsLabel = listOf(leftHandCards0, leftHandCards1, leftHandCards2).toMutableList()
    private val listOfTableCardsLabel = listOf(tableCard0, tableCard1, tableCard2).toMutableList()
    private val listOfKnockedLabel = listOf(bottomKnock, rightKnock, leftKnock, topKnock).toMutableList()
    private val pNameLabel = listOf(bottomName, rightName, topName, leftName)

    private val scoreCurrentPlayer = Label(height = 100, width =  100, posX = 1300, posY = 950 ).apply {
        font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)
        visual = CompoundVisual(
            ColorVisual(Color(255, 255, 255, 50)))
    }

    private val passButton = Button(
        width = 200, height = 60,
        posX = 500, posY = 360,
        text = "PASS",
        font = Font(size=20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            disableAllActions()
            rootService.playerActionService.turn(Turn.Pass)
            nextPlayerButton.isVisible = true
        }
    }

    private val knockButton = Button(
        width = 200, height = 60,
        posX = 500, posY = 460,
        text = "KNOCK",
        font = Font(size=20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            disableAllActions()
            rootService.playerActionService.turn(Turn.Knock)
            nextPlayerButton.isVisible = true
        }
    }
    // helps view for selected Cards by ChangeOne
    private var indexOfTableCardsLabel = 0
    private var indexOfHandCardsLabel = 0

    private val changeOneButton = Button(
        width = 200, height = 60,
        posX = 500, posY = 560,
        text = "CHANGE ONE",
        font = Font(size=20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            disableAllActions()
            enableAllCards()

            val game = rootService.currentGame
            checkNotNull(game)
            val player = game.currentPlayer

            var tCard : SchwimmenCard
            var hCard : SchwimmenCard

            for (i in 0 until listOfTableCardsLabel.size){
                listOfTableCardsLabel[i].apply {
                    onMouseClicked = {
                        tCard = game.tableCards[i]
                        indexOfTableCardsLabel = i
                        highlightTableCard(listOfTableCardsLabel[i])
                        for (j in 0 until listOfBottomHandCardsLabel.size){
                            listOfBottomHandCardsLabel[j].apply {
                                onMouseClicked = {
                                    hCard = player.handCards[j]
                                    indexOfHandCardsLabel = j
                                    highlightHandCard(listOfBottomHandCardsLabel[j])
                                    rootService.playerActionService.turn(Turn.ChangeOne(hCard, tCard))
                                    nextPlayerButton.isVisible = true
                                } }
                        }
                    } }
            }

            for (i in 0 until listOfBottomHandCardsLabel.size){
                listOfBottomHandCardsLabel[i].apply {
                    onMouseClicked = {
                        hCard = player.handCards[i]
                        indexOfHandCardsLabel = i
                        highlightHandCard(listOfBottomHandCardsLabel[i])
                        for (j in 0 until listOfTableCardsLabel.size){
                            listOfTableCardsLabel[j].apply {
                                onMouseClicked = {
                                    tCard = game.tableCards[j]
                                    indexOfTableCardsLabel = j
                                    highlightTableCard(listOfTableCardsLabel[j])
                                    rootService.playerActionService.turn(Turn.ChangeOne(hCard, tCard))
                                    nextPlayerButton.isVisible = true
                                } }
                        }
                    } }
            }
        }
    }

    private val changeAllButton = Button(
        width = 200, height = 60,
        posX = 500, posY = 660,
        text = "CHANGE ALL",
        font = Font(size=20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            disableAllActions()
            nextPlayerButton.isVisible = true
            rootService.playerActionService.turn(Turn.ChangeAll)
        }
    }


    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<SchwimmenCard, CardView> = BidirectionalMap()

    init {
        // dark green for "Casino table" flair
        background = ColorVisual(108, 168, 59)

        addComponents(
            drawCardsSize,
            bottomName, rightName, topName, leftName,
            tableCard0, tableCard1, tableCard2,
            passButton, knockButton, changeOneButton, changeAllButton,
            drawCards, scoreCurrentPlayer
        )
        for (i in 0 until 3){
            addComponents(listOfBottomHandCardsLabel[i])
            addComponents(listOfRightHandCardsLabel[i])
            addComponents(listOfTopHandCardsLabel[i])
            addComponents(listOfLeftHandCardsLabel[i])
        }

    }

    /**
     * Initializes the complete GUI, i.e. the four stack views (left, right, played,
     * collected) of each player.
     */
    override fun refreshAfterStartNewGame() {
        for(k in listOfKnockedLabel) removeComponents(k)
        removeComponents(nextPlayerButton)
        enableAllActions()

        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()

        drawCardsSize.text = game.drawCardsSizeString()
        drawCardsSize.alignment = Alignment.TOP_CENTER
        drawCardsSize.font = Font(size = 20, color = Color.BLACK, fontStyle = Font.FontStyle.ITALIC)

        cardMap.clear()
        val cardImageLoader = CardImageLoader()

        when(game.players.size){
            2 -> {
                bottomName.text = "Player 1: " + game.players[0].playerNameString()
                topName.text = "Player 2: " + game.players[1].playerNameString()
                rightName.isVisible = false; leftName.isVisible = false
                frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                backCards(game.players[1].handCards, listOfTopHandCardsLabel, cardImageLoader)
                for(i in 0 until 3) {
                    listOfLeftHandCardsLabel[i].isVisible = false
                    listOfRightHandCardsLabel[i].isVisible = false
                }
            }
            3 -> {
                bottomName.text = "Player 1: " + game.players[0].playerNameString()
                rightName.text = "Player 2: " + game.players[1].playerNameString()
                topName.text = "Player 3: " + game.players[2].playerNameString()
                leftName.isVisible = false; rightName.isVisible = true
                frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                backCards(game.players[1].handCards, listOfRightHandCardsLabel, cardImageLoader)
                backCards(game.players[2].handCards, listOfTopHandCardsLabel, cardImageLoader)
                for(i in 0 until 3) {listOfLeftHandCardsLabel[i].isVisible = false}
                for(i in 0 until 3) {listOfRightHandCardsLabel[i].isVisible = true}
            }
            4 -> {
                for(i in pNameLabel.indices){
                    pNameLabel[i].text = "Player ${i+1}: " + game.players[i].playerNameString()
                }
                leftName.isVisible = true; rightName.isVisible = true
                frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                backCards(game.players[1].handCards, listOfRightHandCardsLabel, cardImageLoader)
                backCards(game.players[2].handCards, listOfTopHandCardsLabel, cardImageLoader)
                backCards(game.players[3].handCards, listOfLeftHandCardsLabel, cardImageLoader)
                for(i in 0 until 3) {listOfLeftHandCardsLabel[i].isVisible = true}
                for(i in 0 until 3) {listOfRightHandCardsLabel[i].isVisible = true}
            }
        }
        frontCards(game.tableCards, listOfTableCardsLabel, cardImageLoader)
        initializeStackView(game.drawCards, drawCards, cardImageLoader)
    }

    /**
     *
     */
    private fun refreshAfterChangeAll( game: SchwimmenGame, handCards: MutableList<SchwimmenCard>,
                                       tableCards: MutableList<SchwimmenCard>) {
        cardMap.clear()
        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()

        val cardImageLoader = CardImageLoader()

        frontCards(tableCards, listOfBottomHandCardsLabel, cardImageLoader)
        frontCards(handCards, listOfTableCardsLabel, cardImageLoader)
        addComponents(nextPlayerButton)
    }

    /**
     *
     */
    private fun refreshAfterChangeOne( game: SchwimmenGame, handCard: SchwimmenCard, tableCard: SchwimmenCard) {
        disableAllCards()
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

        when(indexOfTableCardsLabel){
            0 -> { initializeAfterChangeOne(handCard, tableCard0, cardImageLoader) }
            1 -> { initializeAfterChangeOne(handCard, tableCard1, cardImageLoader) }
            2 -> { initializeAfterChangeOne(handCard, tableCard2, cardImageLoader) }
        }

        when(indexOfHandCardsLabel){
            0 -> { initializeAfterChangeOne(tableCard, bottomHandCards0, cardImageLoader) }
            1 -> { initializeAfterChangeOne(tableCard, bottomHandCards1, cardImageLoader) }
            2 -> { initializeAfterChangeOne(tableCard, bottomHandCards2, cardImageLoader) }
        }
        for(t in listOfTableCardsLabel) t.opacity = 1.0
        for(h in listOfBottomHandCardsLabel) h.opacity = 1.0
        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()
        addComponents(nextPlayerButton)
    }

    private fun refreshAfterPass(passCount: Int, newTableCards: MutableList<SchwimmenCard>) {
        val game = rootService.currentGame
        checkNotNull(game)

        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()

        val cardImageLoader = CardImageLoader()

        if(passCount == game.players.size){
            frontCards(newTableCards, listOfTableCardsLabel, cardImageLoader)
            drawCardsSize.text = game.drawCardsSizeString()
        }
        addComponents(nextPlayerButton)
    }

    private fun refreshAfterKnock() {
        val game = rootService.currentGame
        checkNotNull(game)
        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()
        addComponents(bottomKnock)
        addComponents(nextPlayerButton)
    }

    /**
     * forward to [refreshAfterTurn], as there is no difference in behavior between left/right
     */
    override fun refreshAfterTurn(game: SchwimmenGame, handCards: MutableList<SchwimmenCard>,
                                  tableCards: MutableList<SchwimmenCard>) {
        //addComponents(nextPlayerButton)
        refreshAfterChangeAll(game, handCards, tableCards)

    }

    override fun refreshAfterTurn(passCount : Int, newTableCards : MutableList<SchwimmenCard>) {
        //addComponents(nextPlayerButton)
        refreshAfterPass(passCount, newTableCards)

    }

    override fun refreshAfterTurn() {

        refreshAfterKnock()

    }

    override fun refreshAfterTurn(game: SchwimmenGame, handCard : SchwimmenCard, tableCard : SchwimmenCard)  {
        //addComponents(nextPlayerButton)
        refreshAfterChangeOne(game, handCard, tableCard)

    }

    private val nextPlayerButton = Button(
        width = 200, height = 60,
        posX = 1300, posY = 830,
        text = "NEXT PLAYER -->",
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            refreshAfterNextPlayer()
        }
    }

    private fun refreshAfterNextPlayer () {
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.currentPlayer

        scoreCurrentPlayer.text =  "Score: \n" + game.currentPlayer.handScoreString()

        cardMap.clear()
        val cardImageLoader = CardImageLoader()
        removeComponents(bottomKnock)

        // shows the first knocked player
        when(game.players.size){
            2 -> {
                when(game.knockCounter){
                    1 -> {addComponents(topKnock) ; removeComponents(bottomKnock)}
                }
            }
            3 -> {
                when(game.knockCounter){
                    1 -> {addComponents(topKnock) ; removeComponents(bottomKnock, rightKnock)}
                    2 -> {addComponents(rightKnock) ; removeComponents(bottomKnock, topKnock)}
                }
            }
            4 -> {
                when(game.knockCounter){
                    1 -> {addComponents(leftKnock) ; removeComponents(rightKnock, topKnock)}
                    2 -> {addComponents(topKnock) ; removeComponents(rightKnock, leftKnock)}
                    3 -> {addComponents(rightKnock) ; removeComponents(topKnock, leftKnock)}
                }
            }
        }
        when(game.players.size){
            2 -> {
                when(player){
                    game.players[0] -> {
                        bottomName.text = "Player 1: " + game.players[0].playerNameString()
                        topName.text = "Player 2: " + game.players[1].playerNameString()

                        frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfTopHandCardsLabel, cardImageLoader)
                    }
                    game.players[1] -> {
                        bottomName.text = "Player 2: " + game.players[1].playerNameString()
                        topName.text = "Player 1: " + game.players[0].playerNameString()

                        frontCards(game.players[1].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[0].handCards, listOfTopHandCardsLabel, cardImageLoader)
                    }
                }
            }
            3 -> {
                when(player){
                    game.players[0] -> {
                        bottomName.text = "Player 1: " + game.players[0].playerNameString()
                        rightName.text = "Player 2: " + game.players[1].playerNameString()
                        topName.text = "Player 3: " + game.players[2].playerNameString()

                        frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfRightHandCardsLabel, cardImageLoader)
                        backCards(game.players[2].handCards, listOfTopHandCardsLabel, cardImageLoader)
                    }
                    game.players[1] -> {
                        bottomName.text = "Player 2: " + game.players[1].playerNameString()
                        rightName.text = "Player 3: " + game.players[2].playerNameString()
                        topName.text = "Player 1: " + game.players[0].playerNameString()

                        backCards(game.players[0].handCards, listOfTopHandCardsLabel, cardImageLoader)
                        frontCards(game.players[1].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[2].handCards, listOfRightHandCardsLabel, cardImageLoader)
                    }
                    game.players[2] -> {
                        bottomName.text = "Player 3: " + game.players[2].playerNameString()
                        rightName.text = "Player 1: " + game.players[0].playerNameString()
                        topName.text = "Player 2: " + game.players[1].playerNameString()

                        backCards(game.players[0].handCards, listOfRightHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfTopHandCardsLabel, cardImageLoader)
                        frontCards(game.players[2].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                    }
                }
            }
            4 -> {
                when(player){
                    game.players[0] -> {
                        pNameLabel[0].text = "Player ${1}: " + game.players[0].playerNameString()
                        pNameLabel[1].text = "Player ${2}: " + game.players[1].playerNameString()
                        pNameLabel[2].text = "Player ${3}: " + game.players[2].playerNameString()
                        pNameLabel[3].text = "Player ${4}: " + game.players[3].playerNameString()

                        frontCards(game.players[0].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfRightHandCardsLabel, cardImageLoader)
                        backCards(game.players[2].handCards, listOfTopHandCardsLabel, cardImageLoader)
                        backCards(game.players[3].handCards, listOfLeftHandCardsLabel, cardImageLoader)
                    }
                    game.players[1] -> {
                        pNameLabel[0].text = "Player ${2}: " + game.players[1].playerNameString()
                        pNameLabel[1].text = "Player ${3}: " + game.players[2].playerNameString()
                        pNameLabel[2].text = "Player ${4}: " + game.players[3].playerNameString()
                        pNameLabel[3].text = "Player ${1}: " + game.players[0].playerNameString()

                        backCards(game.players[0].handCards, listOfLeftHandCardsLabel, cardImageLoader)
                        frontCards(game.players[1].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[2].handCards, listOfRightHandCardsLabel, cardImageLoader)
                        backCards(game.players[3].handCards, listOfTopHandCardsLabel, cardImageLoader)
                    }
                    game.players[2] -> {
                        pNameLabel[0].text = "Player ${3}: " + game.players[2].playerNameString()
                        pNameLabel[1].text = "Player ${4}: " + game.players[3].playerNameString()
                        pNameLabel[2].text = "Player ${1}: " + game.players[0].playerNameString()
                        pNameLabel[3].text = "Player ${2}: " + game.players[1].playerNameString()

                        backCards(game.players[0].handCards, listOfTopHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfLeftHandCardsLabel, cardImageLoader)
                        frontCards(game.players[2].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                        backCards(game.players[3].handCards, listOfRightHandCardsLabel, cardImageLoader)
                    }
                    game.players[3] -> {
                        pNameLabel[0].text = "Player ${4}: " + game.players[3].playerNameString()
                        pNameLabel[1].text = "Player ${1}: " + game.players[0].playerNameString()
                        pNameLabel[2].text = "Player ${2}: " + game.players[1].playerNameString()
                        pNameLabel[3].text = "Player ${3}: " + game.players[2].playerNameString()

                        backCards(game.players[0].handCards, listOfRightHandCardsLabel, cardImageLoader)
                        backCards(game.players[1].handCards, listOfTopHandCardsLabel, cardImageLoader)
                        backCards(game.players[2].handCards, listOfLeftHandCardsLabel, cardImageLoader)
                        frontCards(game.players[3].handCards, listOfBottomHandCardsLabel, cardImageLoader)
                    }
                }
            }
        }
        enableAllActions()
        removeComponents(nextPlayerButton)
    }
    /**
     * highlights only the hand's selected card
     */
    private fun highlightHandCard(view : LabeledStackView){
        for(h in listOfBottomHandCardsLabel){
            if(h != view) h.opacity = 0.5
            else h.opacity = 1.0
        }
    }

    /**
     * highlights only the table's selected card
     */
    private fun highlightTableCard(view : LabeledStackView){
        for(h in listOfTableCardsLabel){
            if(h != view) h.opacity = 0.5
            else h.opacity = 1.0
        }
    }
    // for Buttons
    private fun enableAllActions(){
        passButton.isDisabled = false
        knockButton.isDisabled = false
        changeOneButton.isDisabled = false
        changeAllButton.isDisabled = false
    }
    private fun disableAllActions(){
        passButton.isDisabled = true
        knockButton.isDisabled = true
        changeOneButton.isDisabled = true
        changeAllButton.isDisabled = true
    }

    // for table and hand Cards
    private fun enableAllCards(){
        for(t in listOfTableCardsLabel){
            t.isDisabled = false
        }
        for(h in listOfBottomHandCardsLabel){
            h.isDisabled = false
        }
    }
    private fun disableAllCards(){
        for(t in listOfTableCardsLabel){
            t.isDisabled = true
        }
        for(h in listOfBottomHandCardsLabel){
            h.isDisabled = true
        }
    }

    /**
     * clears [stackView], adds a new [CardView] for each
     * element of [stack] onto it, and adds the newly created view/card pair
     * to the global [cardMap].
     */
    private fun initializeStackView(stack: Deck, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        stack.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun initializeAfterChangeOne(card: SchwimmenCard, stackView: LabeledStackView,
                                         cardImageLoader: CardImageLoader) {
        stackView.clear()
        val cardView = CardView(
            height = 200,
            width = 130,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value))
        )
        stackView.add(cardView)
    }

    private fun frontCards(card: MutableList<SchwimmenCard>, stackView: MutableList<LabeledStackView>,
                           cardImageLoader: CardImageLoader) {
        for(i in 0 until card.size){
            stackView[i].clear()
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card[i].suit, card[i].value))
            )
            stackView[i].add(cardView)
        }
    }

    private fun backCards(card: MutableList<SchwimmenCard>, stackView: MutableList<LabeledStackView>,
                          cardImageLoader: CardImageLoader) {
        for(i in 0 until card.size){
            stackView[i].clear()
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(card[i].suit, card[i].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView[i].add(cardView)
        }
    }
}