package sk.stuba.fei.uim.oop.cards.blue.cardImpl;

import java.util.Random;

import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Barrel extends BlueCard {
    private static final String NAME = "Barrel";
    private static final String DESCRIPTION = "Gives a 1 in 4 chance to avoid a BANG card attack.";
    private Random random;

    public Barrel() {
        super(NAME, DESCRIPTION);
        random = new Random();
    }

    public boolean avoidBang() {
        int chance = random.nextInt(4);
        return chance == 0;
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        boolean canBePlayed = currentPlayer.addCardToTable(this);

        if (canBePlayed) {
            return true;
        } else {
            System.out.println("You have already played this card!");
            return false;
        }
    }
}