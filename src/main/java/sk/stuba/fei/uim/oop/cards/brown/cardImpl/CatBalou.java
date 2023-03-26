package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public class CatBalou extends BrownCard {
    private static final String NAME = "Cat Balou";
    private static final String DESCRIPTION = "Discard a card from an opponent's hand or from the table.";

    public CatBalou() {
        super(NAME, DESCRIPTION);
    }

    @Override
    public boolean performAction(Player currentPlayer, Game game) {
        Player targetPlayer = game.chooseTargetPlayer(currentPlayer);
        if (targetPlayer != null) {
            if (game.chooseHandOrTable(targetPlayer)) {
                var discardedCard = targetPlayer.discardRandomCardFromHand();

                if (discardedCard != null) {
                    System.out.println("Discarded card from the target player's hand: " + discardedCard.getName());
                    return true;
                } else {
                    System.out.println("No cards available in target player's hand");
                    return false;
                }
            } else {
                var discardedCard = (BlueCard) targetPlayer.discardRandomCardFromTable();

                if (discardedCard != null) {
                    System.out.println("Discarded card from the target player's hand: " + discardedCard.getName());
                    return true;
                } else {
                    System.out.println("No cards available on target player's table");
                    return false;
                }
            }
        }
        return false;
    }
}
