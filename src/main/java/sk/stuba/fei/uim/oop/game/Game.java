package sk.stuba.fei.uim.oop.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Barrel;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Dynamite;
import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Missed;

import static sk.stuba.fei.uim.oop.utility.KeyboardInput.readInt;
import static sk.stuba.fei.uim.oop.utility.KeyboardInput.readString;;

public class Game {
    private Deck deck;
    private List<Player> players;
    private int currentPlayerIndex;

    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public void startGame() {

        System.out.println("Enter the number of players (2-4): ");
        int numberOfPlayers = readInt();

        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter the name for player " + (i + 1) + ":");
            String playerName = readString();
            Player player = new Player(playerName);
            players.add(player);

            for (int j = 0; j < 4; j++) {
                player.addCardToHand(deck.draw());
            }
        }

        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn!");

            currentPlayer.addCardToHand(deck.draw());
            currentPlayer.addCardToHand(deck.draw());

            // perform actions with the cards
            boolean playerPassed = false;
            while (!playerPassed && currentPlayer.getHand().size() > 0) {

                System.out.println(currentPlayer.getName() + "'s hand: ");
                currentPlayer.getHand().stream().forEach(item -> System.out.println(item + "\n"));
                System.out.println("Choose a card to play by index (0-" + (currentPlayer.getHand().size() - 1)
                        + "), or enter something else to pass:");
                int chosenCardIndex = readInt();
                // add checking played cards
                if (chosenCardIndex >= 0 && chosenCardIndex < currentPlayer.getHand().size()) {
                    // checkup is last card is "MissedCard"
                    // checkup is last card is "Barrel" but it has already played
                    Card chosenCard = currentPlayer.getCardFromHand(chosenCardIndex);
                    var cardPlayed = false;
                    currentPlayer.removeCardFromHand(chosenCardIndex);

                    // move before removing to check if the card was played
                    if (chosenCard instanceof BrownCard) {
                        ((BrownCard) chosenCard).performAction(currentPlayer, this);
                    } else if (chosenCard instanceof BlueCard) {
                        currentPlayer.addCardToTable((BlueCard) chosenCard);
                    }
                } else {
                    playerPassed = true;
                }
            }

            currentPlayer.discardExcessCards();

            // move to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        System.out.println("Game over!");
    }

    public boolean isGameOver() {
        return players.size() <= 1; // add logic for removing players from this list when they have no lives
    }

    public Card drawCard() {
        return deck.draw();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player chooseTargetPlayer(Player currentPlayer) {
        System.out.println("Choose a target player:");
        List<Player> availablePlayers = new ArrayList<>(players);
        availablePlayers.remove(currentPlayer);

        for (int i = 0; i < availablePlayers.size(); i++) {
            System.out.println(i + ": " + availablePlayers.get(i).getName());
        }

        int chosenPlayerIndex = -1;

        while (chosenPlayerIndex < 0 || chosenPlayerIndex >= availablePlayers.size()) {
            System.out.print("Enter the index of the target player (0-" + (availablePlayers.size() - 1) + "): ");
            chosenPlayerIndex = readInt();
        }

        return availablePlayers.get(chosenPlayerIndex);
    }

    public boolean handleBarrel(Player targetPlayer) {
        BlueCard barrelCard = targetPlayer.findCardOnTable(Barrel.class);

        if (barrelCard != null) {
            Random random = new Random();
            int barrelEffectChance = random.nextInt(4);
            return barrelEffectChance == 0;
        }

        return false;
    }

    public boolean playMissed(Player targetPlayer) {
        BrownCard missedCard = (BrownCard) targetPlayer.findCardInHand(Missed.class);

        if (missedCard != null) {
            targetPlayer.removeCardFromHand(missedCard);
            Deck.discard(missedCard);
            return true;
        }

        return false;
    }

    public boolean playBang(Player targetPlayer) {
        boolean bangAvoided = false;

        Card barrelCard = targetPlayer.findCardOnTable(Barrel.class);
        if (barrelCard != null) {
            bangAvoided = handleBarrel(targetPlayer);
        }

        // if the target player does not have a Barrel card or the handleBarrel false
        if (!bangAvoided) {
            Card missedCard = targetPlayer.findCardInHand(Missed.class);
            if (missedCard != null) {
                bangAvoided = playMissed(targetPlayer);
            }
        }

        // if the target player doest have a Missed card or the playMissed returns false
        return bangAvoided;// chekup logic

    }

    public boolean chooseHandOrTable(Player targetPlayer) {
        String choice = "";
        boolean answer = false;

        while (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
            System.out.println("Choose between target player's hand or table (type 'hand' or 'table'):");
            choice = readString();

            if (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
                System.out.println("Invalid input. Please type 'hand' or 'table'.");
            }
        }

        if (choice.equals("hand")) {
            answer = true;
        }

        return answer;
    }
}
