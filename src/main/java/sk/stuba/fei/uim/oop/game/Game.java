package sk.stuba.fei.uim.oop.game;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import sk.stuba.fei.uim.oop.cards.Card;
import sk.stuba.fei.uim.oop.cards.blue.BlueCard;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Barrel;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Dynamite;
import sk.stuba.fei.uim.oop.cards.blue.cardImpl.Prison;
import sk.stuba.fei.uim.oop.cards.brown.BrownCard;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Bang;
import sk.stuba.fei.uim.oop.cards.brown.cardImpl.Missed;

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
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of players (2-4): ");
        int numberOfPlayers = scanner.nextInt();

        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter the name for player " + (i + 1) + ":");
            String playerName = scanner.next();
            Player player = new Player(playerName);
            players.add(player);

            // Draw initial 4 cards for each player
            for (int j = 0; j < 4; j++) {
                player.addCardToHand(deck.drawCard());
            }
        }

        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It's " + currentPlayer.getName() + "'s turn!");

            // Draw 2 cards at the beginning of the turn
            currentPlayer.addCardToHand(deck.drawCard());
            currentPlayer.addCardToHand(deck.drawCard());

            // Perform actions with the cards
            boolean playerPassed = false;
            while (!playerPassed && currentPlayer.getHand().size() > 0) {

                System.out.println(currentPlayer.getName() + "'s hand: ");
                currentPlayer.getHand().stream().forEach(item -> System.out.println(item + "\n"));
                System.out.println("Choose a card to play by index (0-" + (currentPlayer.getHand().size() - 1)
                        + "), or enter -1 to pass:");
                int chosenCardIndex = scanner.nextInt();
                // add checking played cards
                if (chosenCardIndex >= 0 && chosenCardIndex < currentPlayer.getHand().size()) {
                    Card chosenCard = currentPlayer.getCardFromHand(chosenCardIndex);
                    currentPlayer.removeCardFromHand(chosenCardIndex);

                    if (chosenCard instanceof BrownCard) {
                        ((BrownCard) chosenCard).performAction(currentPlayer, this);
                    } else if (chosenCard instanceof BlueCard) {
                        currentPlayer.addCardToTable((BlueCard) chosenCard);
                    }
                } else {
                    playerPassed = true;
                }
            }

            // Discard excess cards
            currentPlayer.discardExcessCards();

            // Move to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        System.out.println("Game over!");
    }

    public boolean isGameOver() {
        return players.size() <= 1; // add logic for remove players from this list
    }

    public Card drawCard() {
        return deck.drawCard();
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

        Scanner scanner = new Scanner(System.in);
        int chosenPlayerIndex = -1;

        while (chosenPlayerIndex < 0 || chosenPlayerIndex >= availablePlayers.size()) {
            System.out.print("Enter the index of the target player (0-" + (availablePlayers.size() - 1) + "): ");
            chosenPlayerIndex = scanner.nextInt();
        }

        return availablePlayers.get(chosenPlayerIndex);
    }

    public boolean handleBarrel(Player targetPlayer) {
        BlueCard barrelCard = targetPlayer.findCardOnTable(Barrel.class);

        if (barrelCard != null) {
            Random random = new Random();
            int barrelEffectChance = random.nextInt(4); // A 1 in 4 chance
            return barrelEffectChance == 0; // The Barrel effect activates if the random number is 0
        }

        return false; // No Barrel card found, the attack is not avoided
    }

    // private List<BrownCard> discardPile;

    public boolean playMissed(Player targetPlayer) {
        BrownCard missedCard = (BrownCard) targetPlayer.findCardInHand(Missed.class);

        if (missedCard != null) {
            targetPlayer.removeCardFromHand(missedCard);
            Deck.discard(missedCard);
            return true; // Missed card was played
        }

        return false; // No Missed card found in the target player's hand
    }

    public boolean playBang(Player targetPlayer) {
        boolean bangAvoided = false;

        // Check if the target player has a Barrel card on the table
        Card barrelCard = targetPlayer.findCardOnTable(Barrel.class);
        if (barrelCard != null) {
            // If the target player has a Barrel card, call the handleBarrel method
            bangAvoided = handleBarrel(targetPlayer);
        }

        // If the target player does not have a Barrel card or the handleBarrel method
        // returns false
        if (!bangAvoided) {
            // Check if the target player has a Missed card in their hand
            Card missedCard = targetPlayer.findCardInHand(Missed.class);
            if (missedCard != null) {
                // If the target player has a Missed card, call the playMissed method
                bangAvoided = playMissed(targetPlayer);
            }
        }

        // If the target player does not have a Missed card or the playMissed method
        // returns false
        return bangAvoided;// chekup logic

    }

    private static final Scanner scanner = new Scanner(System.in);

    public boolean chooseHandOrTable(Player targetPlayer) {
        String choice = "";
        boolean answer = false;

        while (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
            System.out.println("Choose between target player's hand or table (type 'hand' or 'table'):");
            choice = scanner.nextLine().trim().toLowerCase();

            if (!choice.equalsIgnoreCase("hand") && !choice.equalsIgnoreCase("table")) {
                System.out.println("Invalid input. Please type 'hand' or 'table'.");
            }
        }

        if (choice.equals("hand")) {
            answer = true;
        }

        return answer;
    }
    // Add other methods needed for the card actions, such as
    // playBang, chooseHandOrTable, etc.
}
