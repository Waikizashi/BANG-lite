package sk.stuba.fei.uim.oop.game;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.blue.BlueCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private int lives;
    private List<Card> hand;
    private List<BlueCard> cardsOnTable;

    public Player(String name) {
        this.name = name;
        this.lives = 4;
        this.hand = new ArrayList<>();
        this.cardsOnTable = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void reduceLife(int amount) {
        lives -= amount;

        if (lives <= 0) {
            // set the player s lives to 0
            // add call a method to handle the player s death
            // game.handleDead or some return to call it
            lives = 0;
            System.out.println("Player" + this.name + "is dead !");
        }

        System.out.println("Player - " + this.name + " now has: " + lives + " lives");
    }

    public List<Card> getHand() {
        return hand;
    }

    // public void drawCards(Deck deck, int numCards) {
    // for (int i = 0; i < numCards; i++) {
    // hand.add(deck.draw());
    // }
    // }

    public void playCard(Card card, Game game) {
        card.performAction(this, game);
        hand.remove(card);
    }

    public void discardCard(Card card, Deck deck) {
        hand.remove(card);
        // deck.discard(card);
        Deck.discard(card);
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public BlueCard findCardOnTable(Class<?> cardClass) {
        for (BlueCard card : cardsOnTable) {
            if (cardClass.isInstance(card)) {
                return card;
            }
        }
        return null;
    }

    public void addCardToTable(BlueCard chosenCard) {
        // check if the player already has a card of the same type on the table
        boolean hasSameCard = false;
        for (BlueCard cardOnTable : cardsOnTable) {
            if (cardOnTable.getClass().equals(chosenCard.getClass())) {
                hasSameCard = true;
                break;
            }
        }

        // if the player doesn't have the same card on the table, add the card to the
        // player s table
        if (!hasSameCard) {
            cardsOnTable.add(chosenCard);
        } else {
            System.out.println("You cannot have two of the same card on the table.");
        }
    }

    public Card findCardInHand(Class<?> cardClass) {
        for (Card card : hand) {
            if (cardClass.isInstance(card)) {
                return card;
            }
        }
        return null;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(int cardIndex) {
        if (cardIndex >= 0) {
            hand.remove(cardIndex);
        }
    }

    public void removeCardFromHand(Card cardToRemove) {
        int cardIndex = hand.indexOf(cardToRemove);

        if (cardIndex >= 0) {
            hand.remove(cardIndex);
        }
    }

    public Card getCardFromHand(int chosenCardIndex) {
        if (chosenCardIndex >= 0 && chosenCardIndex < hand.size()) {
            return hand.get(chosenCardIndex);
        } else {
            System.err.println("Invalid card index.");
            return null;
        }
    }

    private static final Random random = new Random();

    public Card discardRandomCardFromHand() {
        if (hand.isEmpty()) {
            return null;
        }

        // randomly select a card from the player s hand
        int randomIndex = random.nextInt(hand.size());
        Card selectedCard = hand.get(randomIndex);

        hand.remove(randomIndex);

        return selectedCard;
    }

    public Card discardRandomCardFromTable() {
        if (cardsOnTable.isEmpty()) {
            return null;
        }

        // randomly select a card from the player s table
        int randomIndex = random.nextInt(cardsOnTable.size());
        Card selectedCard = cardsOnTable.get(randomIndex);

        // remove the selected card from the player s table
        cardsOnTable.remove(randomIndex);

        return selectedCard;
    }

    public void discardExcessCards() {
        while (hand.size() > lives) {
            Random random = new Random();
            int randomCardIndex = random.nextInt(hand.size());
            Card discardedCard = hand.remove(randomCardIndex);
            Deck.discard(discardedCard); // remove static
        }
    }
}
