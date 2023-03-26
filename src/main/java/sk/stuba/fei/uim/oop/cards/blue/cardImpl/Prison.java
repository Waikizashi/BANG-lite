package sk.stuba.fei.uim.oop.cards.blue.cardImpl;

import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;
import java.util.Random;

public class Prison extends BlueCard {
    private static final String NAME = "Prison";
    private static final String DESCRIPTION = "Traps a player with a 1 in 4 chance of escaping. If they fail to escape, they skip their turn.";
    private Random random;

    public Prison() {
        super(NAME, DESCRIPTION);
        random = new Random();
    }

    public boolean escape() {
        int chance = random.nextInt(4);
        return chance == 0;
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        var targetPlayer = game.chooseTargetPlayer(currentPlayer);

        if (targetPlayer == null) {
            return false;
        }
        boolean canBePlayed = targetPlayer.addCardToTable(this);

        if (canBePlayed) {
            return true;
        } else {
            System.out.println("This card cannot be applied to the player " + targetPlayer.getName());
            return false;
        }
    }

}
