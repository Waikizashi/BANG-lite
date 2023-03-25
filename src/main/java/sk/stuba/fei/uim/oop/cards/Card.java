package sk.stuba.fei.uim.oop.cards;

import sk.stuba.fei.uim.oop.game.Game;
import sk.stuba.fei.uim.oop.game.Player;

public abstract class Card {
    private String name;
    private String description;

    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Abstract method to be implemented in each specific card class
    public abstract void performAction(Player currentPlayer, Game game);

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
