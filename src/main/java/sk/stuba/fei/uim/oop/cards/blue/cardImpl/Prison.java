package sk.stuba.fei.uim.oop.cards.blue.cardImpl;

import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;
import java.util.Random;

public class Prison extends BlueCard {
    private static final String NAME = "Prison";
    private static final String DESCRIPTION = "Traps a player with a 1 in 4 chance of escaping. If they fail to escape, they skip their turn.";

    public Prison() {
        super(NAME, DESCRIPTION);
    }

    public boolean escape() {
        Random random = new Random();
        int chance = random.nextInt(4);
        return chance == 0;
    }

    @Override
    public void performAction(Player currentPlayer, Game game) {
        Player targetPlayer = game.chooseTargetPlayer(currentPlayer);
        if (targetPlayer != null) {
            targetPlayer.addBlueCardToPlay(this);
        }
    }
}
