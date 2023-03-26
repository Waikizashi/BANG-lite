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
    private Random random;

    public Player(String name) {
        this.name = name;
        lives = 4;
        hand = new ArrayList<>();
        cardsOnTable = new ArrayList<>();
        random = new Random();

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

    public void reduceLife(int amount, Game game) {
        lives -= amount;

        if (lives <= 0) {
            // set the player s lives to 0
            // add call a method to handle the player s death
            // game.handleDead or some return to call it
            lives = 0;
            System.out.println("Player - " + this.name + " lost his last life and now he is dead!");
            game.removeDeadPlayer(this);
        }

        System.out.println("Player - " + this.name + " now has: " + lives + " lives");
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<? extends Card> getTable() {
        return cardsOnTable;
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
        deck.discard(card);
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

    public boolean addCardToTable(BlueCard chosenCard) {
        // check if the player already has a card of the same type on the table
        // boolean hasSameCard = false;
        for (BlueCard cardOnTable : cardsOnTable) {
            if (cardOnTable.getClass().equals(chosenCard.getClass())) {
                // hasSameCard = true;
                // break;
                return false;
            }
        }

        // if the player doesn't have the same card on the table, add the card to the
        // player s table
        // if (!hasSameCard) {
        cardsOnTable.add(chosenCard);
        return true;
        // } else {
        // System.out.println("You cannot have two of the same card on the table.");
        // }
    }

    public boolean removeCardFromTable(Card cardToRemove) {
        return cardsOnTable.remove(cardToRemove);
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

    public void discardExcessCards(Deck deck) {
        while (hand.size() > lives) {
            int randomCardIndex = random.nextInt(hand.size());
            Card discardedCard = hand.remove(randomCardIndex);
            deck.discard(discardedCard); // remove static
        }
    }
}
