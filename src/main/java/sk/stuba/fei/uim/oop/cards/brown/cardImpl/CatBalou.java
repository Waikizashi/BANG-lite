package sk.stuba.fei.uim.oop.cards.brown.cardImpl;

import sk.stuba.fei.uim.oop.cards.Card;
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
    public void performAction(Player currentPlayer, Game game) {
        Player targetPlayer = game.chooseTargetPlayer(currentPlayer);
        if (targetPlayer != null) {
            if (game.chooseHandOrTable(targetPlayer)) {
                Card discardedCard = targetPlayer.discardRandomCardFromHand();
                System.out.println("Discarded card from the target player's hand: " + discardedCard.getName());
            } else {
                BlueCard discardedCard = (BlueCard) targetPlayer.discardRandomCardFromTable();
                System.out.println("Discarded card from the target player's table: "
                        + ((discardedCard != null) ? discardedCard.getName() : "No cards available"));
            }
        }
    }
}
