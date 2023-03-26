package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Stagecoach extends BrownCard {
    private static final String NAME = "Stagecoach";
    private static final String DESCRIPTION = "Draw two cards from the deck.";

    public Stagecoach() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        Card drawnCard1 = game.drawCard();
        Card drawnCard2 = game.drawCard();

        if (drawnCard1 != null && drawnCard2 != null) {
            currentPlayer.addCardToHand(drawnCard1);
            currentPlayer.addCardToHand(drawnCard2);
            System.out.println("The current player drew two cards using the Stagecoach card!");
        } else {
            System.out.println("There are not enough cards left in the deck!");
        }
        return true;
    }
}
