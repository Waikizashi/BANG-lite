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

import static sk.stuba.fei.uim.oop.utility.ZKlavesnice.readInt;
import static sk.stuba.fei.uim.oop.utility.ZKlavesnice.readString;

public class Game {
    private Deck deck;
    private List<Player> players;
    private int currentPlayerIndex;
    private Random random;

    public Game() {
        deck = new Deck();
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        random = new Random();
    }

    public void startGame() {

        // System.out.println("Enter the number of players (2-4): ");
        int numberOfPlayers = readInt("Enter the number of players (2-4): ");

        for (int i = 0; i < numberOfPlayers; i++) {
            // System.out.println("Enter the name for player " + (i + 1) + ":");
            String playerName = readString("Enter the name for player " + (i + 1) + ":");
            Player player = new Player(playerName);
            players.add(player);

            for (int j = 0; j < 4; j++) {
                player.addCardToHand(deck.draw());
            }
        }

        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn!");
            checkDynamite(currentPlayer);

            // turn start
            currentPlayer.addCardToHand(deck.draw());
            currentPlayer.addCardToHand(deck.draw());

            // perform actions with the cards
            boolean playerPassed = false;
            boolean atListOneCardIsPlayed = false;

            while (!playerPassed && currentPlayer.getHand().size() > 0) {
                System.out.println(currentPlayer.getName() + "'s hand: ");
                currentPlayer.getHand().stream().forEach(item -> System.out.println(item + "\n"));
                // System.out.println("Choose a card to play by index (0-" +
                // (currentPlayer.getHand().size() - 1)
                // + "), or enter something else to pass:");
                int chosenCardIndex = readInt(
                        "Choose a card to play by index (0-" + (currentPlayer.getHand().size() - 1)
                                + "), or enter something else to pass:");
                // add checking played cards
                if (chosenCardIndex >= 0 && chosenCardIndex < currentPlayer.getHand().size()) {
                    // checkup is last card is "MissedCard"
                    // checkup is last card is "Barrel" but it has already played
                    Card chosenCard = currentPlayer.getCardFromHand(chosenCardIndex);
                    var cardPlayed = false;

                    // move before removing to check if the card was played
                    if (chosenCard instanceof BrownCard) {
                        cardPlayed = ((BrownCard) chosenCard).performAction(currentPlayer, this);
                    } else if (chosenCard instanceof BlueCard) {
                        cardPlayed = ((BlueCard) chosenCard).performAction(currentPlayer, this);
                        // currentPlayer.addCardToTable((BlueCard) chosenCard);
                    }

                    if (cardPlayed) {
                        currentPlayer.removeCardFromHand(chosenCard);
                        atListOneCardIsPlayed = true;
                    }

                } else {
                    if (atListOneCardIsPlayed == false) {
                        System.out.println("\n#### You can't skip a turn, you need to play at least one card! ####\n");
                    } else {
                        playerPassed = true;
                    }
                }
                if (isGameOver()) {
                    break;
                }
            }

            currentPlayer.discardExcessCards(deck);

            // move to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        // System.out.println("GAME OVER!" + " PLAYER ::: " + players.get(0).getName() +
        // " ::: WINS!");
        printWinner(players.get(0));
    }

    public boolean isGameOver() {
        return players.size() <= 1; // add logic for removing players from this list when they have no lives
    }

    public void removeDeadPlayer(Player deadPlayer) {
        int currentIndex = players.indexOf(deadPlayer);
        List<Card> toDiscardPile = new ArrayList<>();
        toDiscardPile.addAll(deadPlayer.getHand());
        toDiscardPile.addAll(deadPlayer.getTable());
        deck.discardAll(toDiscardPile);
        players.remove(currentIndex);
    }

    public void printWinner(Player player) {
        int tableWidth = 50;

        String gameTitle = "GAME OVER!";
        String winnerName = player.getName();
        String winnerLine = "Player " + winnerName + " is the winner!";

        String titleSpaces = " ".repeat((tableWidth - gameTitle.length() - 2) / 2);
        String winnerSpaces = " ".repeat((tableWidth - winnerLine.length() - 2) / 2);

        System.out.println("╔" + "═".repeat(tableWidth - 2) + "╗");
        System.out.println("║" + titleSpaces + gameTitle + titleSpaces + "║");
        System.out.println("║" + "-".repeat(tableWidth - 2) + "║");
        System.out.println("║" + winnerSpaces + winnerLine + winnerSpaces + "║");
        System.out.println("╚" + "═".repeat(tableWidth - 2) + "╝");

    }

    public Card drawCard() {
        return deck.draw();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void checkDynamite(Player currentPlayer) {
        Dynamite dynamiteCard = (Dynamite) currentPlayer.findCardInHand(Dynamite.class);

        if (dynamiteCard != null) {
            dynamiteCard.performAction(currentPlayer, this);
            currentPlayer.removeCardFromHand(dynamiteCard);
            boolean exploded = dynamiteCard.explode();

            if (exploded) {
                currentPlayer.reduceLife(3, this);
                currentPlayer.removeCardFromTable(dynamiteCard);
                deck.discard(dynamiteCard);
                System.out.println("Dynamite exploded! " + currentPlayer.getName() + " loses 3 lives.");

            } else {
                Player previousPlayer = getPreviousPlayer(currentPlayer);
                currentPlayer.removeCardFromTable(dynamiteCard);
                previousPlayer.addCardToTable(dynamiteCard);
                System.out.println("Dynamite did not explode. It is passed to " + previousPlayer.getName() + ".");
            }
        }
    }

    private Player getPreviousPlayer(Player currentPlayer) {
        // Find the index of the current player in the players list
        int currentIndex = players.indexOf(currentPlayer);

        // If the current player is not found in the list, return null
        if (currentIndex == -1) {
            return null;
        }
        // Calculate the index of the previous player, wrapping around if necessary
        int previousIndex = (currentIndex - 1 + players.size()) % players.size();

        if (previousIndex < 0) {
            previousIndex = players.size() - 1;
        }
        // Return the previous player
        return players.get(previousIndex);
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
            // System.out.print("Enter the index of the target player (0-" +
            // (availablePlayers.size() - 1) + "): ");
            chosenPlayerIndex = getIntAnswer(availablePlayers.size() - 1);
        }

        return availablePlayers.get(chosenPlayerIndex);
    }

    public boolean handleBarrel(Player targetPlayer) {
        BlueCard barrelCard = targetPlayer.findCardOnTable(Barrel.class);

        if (barrelCard != null) {
            int barrelEffectChance = random.nextInt(4);
            return barrelEffectChance == 0;
        }
        return false;
    }

    public boolean playMissed(Player targetPlayer) {
        BrownCard missedCard = (BrownCard) targetPlayer.findCardInHand(Missed.class);

        if (missedCard != null) {
            targetPlayer.removeCardFromHand(missedCard);
            deck.discard(missedCard);
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
            // System.out.println("Choose between target player's hand or table (type 'hand'
            // or 'table'):");
            choice = getStringAnswer();

            if (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
                System.out.println("Invalid input. Please type 'hand' or 'table'.");
            }
        }
        if (choice.equals("hand")) {
            answer = true;
        }

        return answer;
    }

    private String getStringAnswer() {
        var text = readString("Choose between target player's hand or table (type 'hand' or 'table'):");

        while (true) {

            if (text.equals("table") || text.equals("hand")) {
                break;
            } else {
                text = readString("Invalid input. Please enter either 'table' or 'hand'.");
            }
        }

        return text;
    }

    private int getIntAnswer(int max) {

        int input;

        while (true) {

            try {
                input = readInt("Enter the index of the target player (0-" + max + "): ");

                if (input >= 0 && input <= max) {
                    break;
                } else {
                    System.out.printf("Invalid input. Please enter a number between %d and %d.%n", 0, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return input;
    }
}
