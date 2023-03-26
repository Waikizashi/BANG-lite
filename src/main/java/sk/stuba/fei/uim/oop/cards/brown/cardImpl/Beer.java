package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Beer extends BrownCard {
    private static final String NAME = "Beer";
    private static final String DESCRIPTION = "Add one life to the currently playing player.";

    public Beer() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        currentPlayer.setLives(currentPlayer.getLives() + 1);
        System.out.println("The current player gained one life by using a Beer card!");
        System.out.println("Player - " + currentPlayer.getName() + " now has: " + currentPlayer.getLives() + " lives");
        return true;
    }
}