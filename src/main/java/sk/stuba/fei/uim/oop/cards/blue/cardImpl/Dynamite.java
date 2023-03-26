package sk.stuba.fei.uim.oop.cards.blue.cardImpl;

import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

import java.util.Random;

public class Dynamite extends BlueCard {
    private static final String NAME = "Dynamite";
    private static final String DESCRIPTION = "Dynamite has a 1 in 8 chance of exploding. If it explodes, the player loses 3 lives.";
    private Random random;

    public Dynamite() {
        super(NAME, DESCRIPTION);
        random = new Random();
    }

    public boolean explode() {
        int chance = random.nextInt(8);
        return chance == 0;
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        currentPlayer.addCardToTable(this);

        System.out.println("Dynamite detected!!!");
        return false;
    }
}
