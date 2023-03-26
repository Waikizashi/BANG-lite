package sk.stuba.fei.uim.oop.game;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Barrel;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Dynamite;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Prison;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Bang;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Beer;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.CatBalou;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Indians;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Missed;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Stagecoach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> deck;
    private List<Card> discardPile; // remove static potomusto dolbojeb

    public Deck() {
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        // Add Blue Cards
        deck.add(new Barrel());
        deck.add(new Barrel());
        deck.add(new Dynamite());
        deck.add(new Prison());
        deck.add(new Prison());
        deck.add(new Prison());

        // Add Brown Cards
        for (int i = 0; i < 30; i++) {
            deck.add(new Bang());
        }
        for (int i = 0; i < 15; i++) {
            deck.add(new Missed());
        }
        for (int i = 0; i < 8; i++) {
            deck.add(new Beer());
        }
        for (int i = 0; i < 6; i++) {
            deck.add(new CatBalou());
        }
        for (int i = 0; i < 4; i++) {
            deck.add(new Stagecoach());
        }
        for (int i = 0; i < 2; i++) {
            deck.add(new Indians());
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        if (deck.isEmpty()) {
            deck.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }
        return deck.remove(0);
    }

    // public Card draw() {
    // if (!deck.isEmpty()) {
    // Card drawnCard = deck.get(0);
    // deck.remove(0);
    // return drawnCard;
    // }

    // return null; // Deck is empty
    // }

    public void discard(Card card) { // same remove static, add object to Game
        discardPile.add(card);
    }

    public void discardAll(List<Card> cards) {
        discardPile.addAll(cards);
    }
}
