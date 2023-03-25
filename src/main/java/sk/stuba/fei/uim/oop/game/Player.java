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
    private List<BlueCard> inPlay;
    private List<BlueCard> cardsOnTable = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.lives = 4;
        this.hand = new ArrayList<>();
        this.inPlay = new ArrayList<>();
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
        // Subtract the specified amount from the player's current lives
        lives -= amount;

        // Check if the player's lives are below or equal to 0
        if (lives <= 0) {
            // Set the player's lives to 0 and call a method to handle the player's death
            lives = 0;
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<BlueCard> getInPlay() {
        return inPlay;
    }

    public void drawCards(Deck deck, int numCards) {
        for (int i = 0; i < numCards; i++) {
            hand.add(deck.draw());
        }
    }

    public void playCard(Card card, Game game) {
        card.performAction(this, game);
        hand.remove(card);
    }

    public void discardCard(Card card, Deck deck) {
        hand.remove(card);
        deck.discard(card);
    }

    public void addBlueCardToPlay(BlueCard card) {
        inPlay.add(card);
    }

    public void removeBlueCardFromPlay(BlueCard card) {
        inPlay.remove(card);
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
        return null; // No card of the specified class found on the table
    }

    public void addCardToTable(BlueCard chosenCard) {
        // Check if the player already has a card of the same type on the table
        boolean hasSameCard = false;
        for (BlueCard cardOnTable : cardsOnTable) {
            if (cardOnTable.getClass().equals(chosenCard.getClass())) {
                hasSameCard = true;
                break;
            }
        }

        // If the player doesn't have the same card on the table, add the card to the
        // player's table
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
        return null; // No card of the specified class found in the player's hand
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(int cardIndex) {// (Card cardToRemove) {
        // Find the index of the specified card in the player's hand
        // int cardIndex = hand.indexOf(cardToRemove);

        // If the index is valid, remove the card from the player's hand
        if (cardIndex >= 0) {
            hand.remove(cardIndex);
        }
    }

    public void removeCardFromHand(Card cardToRemove) {
        // Find the index of the specified card in the player's hand
        int cardIndex = hand.indexOf(cardToRemove);

        // If the index is valid, remove the card from the player's hand
        if (cardIndex >= 0) {
            hand.remove(cardIndex);
        }
    }

    public Card getCardFromHand(int chosenCardIndex) {
        // Check if the index is valid (within the range of the player's hand size)
        if (chosenCardIndex >= 0 && chosenCardIndex < hand.size()) {
            // If the index is valid, return the card at that index from the player's hand
            return hand.get(chosenCardIndex);
        } else {
            // If the index is not valid, return null or throw an exception
            System.err.println("Invalid card index.");
            return null;
        }
    }

    private static final Random random = new Random();

    public Card discardRandomCardFromHand() {
        if (hand.isEmpty()) {
            return null; // If the player's hand is empty, return null
        }

        // Randomly select a card from the player's hand
        int randomIndex = random.nextInt(hand.size());
        Card selectedCard = hand.get(randomIndex);

        // Remove the selected card from the player's hand
        hand.remove(randomIndex);

        // Return the discarded card
        return selectedCard;
    }

    public Card discardRandomCardFromTable() {
        if (cardsOnTable.isEmpty()) {
            return null; // If the player's table is empty, return null
        }

        // Randomly select a card from the player's table
        int randomIndex = random.nextInt(cardsOnTable.size());
        Card selectedCard = cardsOnTable.get(randomIndex);

        // Remove the selected card from the player's table
        cardsOnTable.remove(randomIndex);

        // Return the discarded card
        return selectedCard;
    }

    public void discardExcessCards() {
        // Check the number of cards in the player's hand and compare it to the player's
        // lives
        while (hand.size() > lives) {
            // If the number of cards in the player's hand is greater than the player's
            // lives, discard the excess cards
            Random random = new Random();
            int randomCardIndex = random.nextInt(hand.size());
            Card discardedCard = hand.remove(randomCardIndex);
            Deck.discard(discardedCard);
            // Add the discarded card to the discard pile (You should have a discard pile
            // implemented in your Game or Deck class)
            // For example: game.addToDiscardPile(discardedCard);
        }
    }
}
