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
    public boolean performAction(Player currentPlayer, Game game) {
        System.out.println("Card could not be played, it is PASSIVE CARD!");
        return false;
    }
}
