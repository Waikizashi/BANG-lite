package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Missed extends BrownCard {
    private static final String NAME = "Missed";
    private static final String DESCRIPTION = "Avoid the effect of a Bang card when targeted.";

    public Missed() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void performAction(Player currentPlayer, Game game) {
        // The action of the Missed card is handled by the Game class when a player is
        // targeted by a Bang card.
        // No action is needed here, as the card is played automatically in response to
        // a Bang card.
    }
}
