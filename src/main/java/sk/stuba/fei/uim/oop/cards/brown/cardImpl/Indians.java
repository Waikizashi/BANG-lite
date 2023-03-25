package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class Indians extends BrownCard {
    private static final String NAME = "Indians";
    private static final String DESCRIPTION = "Cause an Indian attack on all players (except the player who played it). Each player must use a Bang card or lose one life.";

    public Indians() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public void performAction(Player currentPlayer, Game game) {
        for (Player targetPlayer : game.getPlayers()) {
            if (!targetPlayer.equals(currentPlayer)) {
                System.out.println("Indian attack on player: " + targetPlayer.getName());
                if (game.playBang(targetPlayer)) {
                    System.out.println("The target player used a Bang card to defend against the Indian attack!");
                } else {
                    targetPlayer.setLives(targetPlayer.getLives() - 1);
                    System.out.println("The target player couldn't defend against the Indian attack and lost a life!");
                }
            }
        }
    }
}
