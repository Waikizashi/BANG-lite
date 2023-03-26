package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Bang extends BrownCard {
    private static final String NAME = "Bang";
    private static final String DESCRIPTION = "Attack another player, reducing their lives by 1.";

    public Bang() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void performAction(Player currentPlayer, Game game) {
        Player targetPlayer = game.chooseTargetPlayer(currentPlayer);
        if (targetPlayer != null) {
            if (game.handleBarrel(targetPlayer)) {
                System.out.println("The target player avoided the Bang using their Barrel!");
            } else if (game.playMissed(targetPlayer)) {
                System.out.println("The target player avoided the Bang using a Missed card!");
            } else {
                System.out.println("The target player was hit by the Bang and lost a life!");
                targetPlayer.reduceLife(1);
            }
        }
    }
}
