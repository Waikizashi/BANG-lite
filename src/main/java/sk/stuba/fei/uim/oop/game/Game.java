package sk.stuba.fei.uim.oop.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Barrel;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Dynamite;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Prison;
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
        int numberOfPlayers = readInt("Enter the number of players (2-4): ");
        while (numberOfPlayers < 2 || numberOfPlayers > 4) {
            numberOfPlayers = readInt("allow only (2-4) players: ");
        }

        for (int i = 0; i < numberOfPlayers; i++) {
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

            currentPlayer.addCardToHand(deck.draw());
            currentPlayer.addCardToHand(deck.draw());

            boolean playerPassed = checkPrison(currentPlayer);
            boolean atListOneCardIsPlayed = false;

            while (!playerPassed && currentPlayer.getHand().size() > 0) {
                System.out.println(currentPlayer.getName() + "'s hand: ");
                currentPlayer.getHand().stream().forEach(item -> System.out.println(item + "\n"));
                int chosenCardIndex = readInt(
                        "Choose a card to play by index (0-" + (currentPlayer.getHand().size() - 1)
                                + "), or enter something else to pass:");
                if (chosenCardIndex >= 0 && chosenCardIndex < currentPlayer.getHand().size()) {
                    Card chosenCard = currentPlayer.getCardFromHand(chosenCardIndex);
                    var cardPlayed = false;
                    if (chosenCard instanceof BrownCard) {
                        cardPlayed = ((BrownCard) chosenCard).performAction(currentPlayer, this);
                    } else if (chosenCard instanceof BlueCard) {
                        cardPlayed = ((BlueCard) chosenCard).performAction(currentPlayer, this);
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

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        printWinner(players.get(0));
    }

    public boolean isGameOver() {
        return players.size() <= 1;
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

    public boolean checkPrison(Player currentPlayer) {
        Prison prisonCard = (Prison) currentPlayer.findCardOnTable(Prison.class);

        if (prisonCard != null) {
            boolean escaped = prisonCard.escape();

            if (escaped) {
                System.out.println(" Player " + currentPlayer.getName() + " escape from prison!");
            } else {
                System.out.println(" failed to escape from prison and player " + currentPlayer.getName()
                        + " skips turn.");
            }

            currentPlayer.removeCardFromTable(prisonCard);
            deck.discard(prisonCard);

            return !escaped;
        }

        return false;
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
        int currentIndex = players.indexOf(currentPlayer);

        if (currentIndex == -1) {
            return null;
        }
        int previousIndex = (currentIndex - 1 + players.size()) % players.size();

        if (previousIndex < 0) {
            previousIndex = players.size() - 1;
        }
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
        if (!bangAvoided) {
            Card missedCard = targetPlayer.findCardInHand(Missed.class);
            if (missedCard != null) {
                bangAvoided = playMissed(targetPlayer);
            }
        }
        return bangAvoided;
    }

    public boolean chooseHandOrTable(Player targetPlayer) {
        String choice = "";
        boolean answer = false;

        while (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
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
