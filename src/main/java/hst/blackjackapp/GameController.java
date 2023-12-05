package hst.blackjackapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

    @FXML
    private Label gameStatusLabel;

    @FXML
    private Label tokenLabel;

    @FXML
    private Button hitButton;

    @FXML
    private Button standButton;

    @FXML
    private Button splitButton;

    @FXML
    private Button doubleButton;

    @FXML
    private TextField betTextField;

    @FXML
    private Label playerCardsLabel;

    @FXML
    private Label playerTotalLabel;

    @FXML
    private Label dealerCardsLabel;

    @FXML
    private Label dealerTotalLabel;

    @FXML
    private Button betButton;

    @FXML
    private Label betTag;


    @FXML
    private Label finish;

    @FXML
    private Button newGame;

    @FXML
    private Button endGame;

    private int tokens = 100;
    private int playerAceCount = 0;
    private int dealerAceCount = 0;
    private int bet;

    private ArrayList<Card> player;
    private int playerTotal;
    private ArrayList<Card> dealer;
    private int dealerTotal;

    private ArrayList<Card> playerSecond;

    private boolean split = false;
    private boolean doubleT = false;

    private CardList deck;
    private boolean playerTurnDone;

    private HashMap<String, Integer> cardValues = new HashMap<>();

    @FXML
    public void initialize() {
        cardValues.put("Two", 2);
        cardValues.put("Three", 3);
        cardValues.put("Four", 4);
        cardValues.put("Five", 5);
        cardValues.put("Six", 6);
        cardValues.put("Seven", 7);
        cardValues.put("Eight", 8);
        cardValues.put("Nine", 9);
        cardValues.put("Ten", 10);
        cardValues.put("Jack", 10);
        cardValues.put("Queen", 10);
        cardValues.put("King", 10);
        cardValues.put("Ace", 11);
        startNewGame();
    }

    private void startNewGame() {
        gameStatusLabel.setText("New game of Blackjack!");
        tokenLabel.setText("Your token value is "+tokens);
        betTag.setVisible(true);
        betButton.setVisible(true);
        betTextField.setVisible(true);
        playerCardsLabel.setVisible(false);
        dealerCardsLabel.setVisible(false);
        playerTotalLabel.setVisible(false);
        dealerTotalLabel.setVisible(false);

        // Initialize the deck and deal cards
        deck = new CardList();
        player = new ArrayList<>();
        dealer = new ArrayList<>();

        playerTurnDone=false;
        Card pCard1 = deck.getRandomCard();
        Card pCard2 = deck.getRandomCard();
        player.add(pCard1);
        player.add(pCard2);

        Card dCard1 = deck.getRandomCard();
        Card dCard2 = deck.getRandomCard();
        dealer.add(dCard1);
        dealer.add(dCard2);

        dealerTotalLabel.setVisible(false);

        standButton.setVisible(false);
        hitButton.setVisible(false);
        splitButton.setVisible(false);
        doubleButton.setVisible(false);
        endGame.setVisible(false);
        finish.setVisible(false);
        finish.setText("");
        newGame.setVisible(false);


//
//        // Check if the bet is within valid bounds
//        if (bet <= 0 || bet > tokens) {
//            System.out.println("Invalid bet. Please enter a bet between 1 and " + tokens);
//            return;
//        }
//
//        // Deduct the bet amount from tokens
//        tokens -= bet;
//
//        playerTotal = 0;
//        playerAceCount = 0;
//        dealerAceCount = 0;
//
//        for (Card c : player) {
//            playerTotal += cardValues.get(c.getVal());
//            if (c.getVal().equals("Ace")) playerAceCount++;
//        }
//
//        if (pCard1.getVal().equals(pCard2.getVal()) && bet <= (tokens / 2)) split = true;
//        if (bet <= (tokens / 2)) doubleT = true;
//
//        // Call updateGameView after initializing player
//        updateGameView();
    }



    private int placeBets() {
        int bet = -1;

        try {
            String input = betTextField.getText().trim();

            // Check if the input is not empty
            if (!input.isEmpty()) {
                // Attempt to parse the bet from the TextField
                bet = Integer.parseInt(input);

                // Check if the bet is within valid bounds
                if (bet < 0 || bet > tokens) {
                    System.out.println("Invalid bet. Please enter a bet between 0 and " + tokens);
                    betTag.setText("Invalid bet. Please enter a bet between 0 and " + tokens);
                    return -1; // Return -1 to indicate an invalid bet
                }
            } else {
                System.out.println("Please enter a bet.");
                return -1; // Return -1 to indicate an invalid bet
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Print the stack trace for debugging
            System.out.println("Invalid input. Please enter a numeric value for your bet.");
            betTag.setText("Invalid input. Please enter a numeric value for your bet.");
            return -1; // Return -1 to indicate an invalid bet
        }

        return bet;
    }




    private void updateGameView() {
        playerCardsLabel.setVisible(true);
        dealerCardsLabel.setVisible(true);
        playerTotalLabel.setVisible(true);


        updatePlayerView();
        updateDealerView();

        // Determine game state and update button visibility
        //hitButton.setDisable(playerTotal >= 21 || playerTotal == 0);
//        standButton.setDisable(playerTotal >= 21 || playerTotal == 0);
//        splitButton.setDisable(true); // Handle split logic if needed

        // Only enable the double button if it's still the player's turn and they have enough tokens
        doubleButton.setDisable(playerTotal >= 21 || tokens < bet);
    }




    private void updatePlayerView() {
        StringBuilder playerCardsText = new StringBuilder("Your cards:\n");


        for (Card c : player) {
            playerCardsText.append(c.getVal()).append(" of ").append(c.getSuit()).append("\n");

        }

        playerCardsLabel.setText(playerCardsText.toString());
        playerTotalLabel.setText("Your total is " + playerTotal);
    }

    private void updateDealerView() {
        StringBuilder dealerCardsText = new StringBuilder("Dealer's cards:\n");

        if (playerTurnDone) {
            for (Card c : dealer) {
                dealerCardsText.append(c.getVal()).append(" of ").append(c.getSuit()).append("\n");
            }
        } else {
            dealerCardsText.append(dealer.get(0).getVal()).append(" of ").append(dealer.get(0).getSuit()).append("\n");
        }

        dealerCardsLabel.setText(dealerCardsText.toString());
        // You may choose to display the dealer's total or not based on your design
        //dealerTotalLabel.setText("Dealer's total is " + dealerTotal);
    }

    @FXML
    private void handleHit(ActionEvent event) {
        split = false;
        Card newCard = deck.getRandomCard();
        player.add(newCard);
        playerTotal += cardValues.get(newCard.getVal());
        if (newCard.getVal().equals("Ace")) playerAceCount++;
        System.out.println("New Card: \n" + newCard.getVal() + " of " + newCard.getSuit());
        if (playerTotal>21 && playerAceCount>0){
            playerTotal-=10;
            playerAceCount--;
        }

        updateGameView();

        if (playerTotal>21&&playerAceCount==0){
            playerBusts();
        }
    }


    private void playerBusts(){
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        splitButton.setVisible(false);
        finish.setVisible(true);
        finish.setText("Bust! You have lost "+bet+" tokens!");
        newGame.setVisible(true);
        endGame.setVisible(true);
    }

    @FXML
    private void startNew(ActionEvent event){
        dealerTotalLabel.setVisible(false);

        startNewGame();
    }

    @FXML
    private void close(ActionEvent event){
        Platform.exit();
    }


    @FXML
    private void handleStand(ActionEvent event) {
        playerTurnDone=true;
        updateGameView();
        dealerGoes();
    }


    private void dealerGoes() {
        dealerTotalLabel.setVisible(true);
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        splitButton.setVisible(false);

        // Update UI for dealer's initial cards
        StringBuilder dealerCardsText = new StringBuilder("Dealer's cards:\n");
        for (Card c : dealer) {
            dealerCardsText.append(c.getVal()).append(" of ").append(c.getSuit()).append("\n");
        }
        dealerCardsLabel.setText(dealerCardsText.toString());

        // Update UI for dealer's initial total
        dealerTotalLabel.setText("Dealer's total is " + dealerTotal);

        while (dealerTotal < 17) {

            Card card = deck.getRandomCard();
            dealer.add(card);
            dealerTotal += cardValues.get(card.getVal());
            if (card.getVal().equals("Ace")) dealerAceCount++;
            if (dealerTotal > 21 && dealerAceCount > 0) {
                dealerAceCount--;
                dealerTotal -= 10;
            } else if (dealerTotal > 21) {
                // Update UI for dealer's cards after bust
                updateDealerView();
                dealerTotalLabel.setText("Dealer busted with " + dealerTotal);
                System.out.println("Dealer busted!\n");
                scoreComp(playerTotal, 0);
                break;
            }

            // Update UI for each card drawn by the dealer
            dealerCardsText.append(card.getVal()).append(" of ").append(card.getSuit()).append("\n");
            dealerCardsLabel.setText(dealerCardsText.toString());

            // Update UI for dealer's total after each card drawn
            dealerTotalLabel.setText("Dealer's total is " + dealerTotal);


        }

        if (dealerTotal <= 21) {
            // Update UI for final dealer's cards and total
            updateDealerView();
            scoreComp(playerTotal, dealerTotal);
        }
    }


    private void scoreComp(int x, int y){


        newGame.setVisible(true);
        endGame.setVisible(true);
        if (x==y && player.size()==2 && dealer.size()==2 && x==21){
            finish.setText("Counter Blackjack! What are the odds! You get your tokens back");
            tokens+=bet;
        } else if (x==21&&player.size()==2){
            finish.setText("Blackjack!! You have won "+bet*1.5+" tokens!");
            tokens+=2.5*bet;
        } else if (y==21&&dealer.size()==2){
            finish.setText("Dealer Blackjack!! You have lost "+bet+" tokens!");
        } else if (x==y){
            finish.setText("Push! You get your tokens back");
            tokens+=bet;
        } else if (x>y){
            finish.setText("Nice! You have won "+bet+" tokens!");
            tokens+=2*bet;
        } else if (x<y){
            finish.setText("Wow! You have lost "+bet+" tokens!");
        }



        tokenLabel.setText("Your token value is "+tokens);
        finish.setVisible(true);
        newGame.setVisible(true);
        endGame.setVisible(true);

    }

    @FXML
    private void handleSplit(ActionEvent event) {
        // ... (implement split logic)
        updateGameView();
    }

    @FXML
    private void handleDouble(ActionEvent event) {
        tokens-=bet;
        bet*=2;
        tokenLabel.setText("Your token value is "+tokens);

        handleHit(event);
        playerTurnDone=true;
        dealerGoes();


    }

    @FXML
    private void handlePlaceBet(ActionEvent event) {
        // Attempt to place the bet
        bet = placeBets();



        // Check if the bet is within valid bounds
        if (bet < 0 || bet > tokens) {
            System.out.println("Invalid bet. Please enter a bet between 1 and " + tokens);
            return;
        }

        // Deduct the bet amount from tokens
        tokens -= bet;
        tokenLabel.setText("Your token value is "+tokens);

        playerTotal = 0;
        dealerTotal=0;
        playerAceCount = 0;
        dealerAceCount = 0;

        for (Card c : player) {
            playerTotal += cardValues.get(c.getVal());
            if (c.getVal().equals("Ace")) playerAceCount++;
        }

        for (Card c : dealer) {
            dealerTotal += cardValues.get(c.getVal());
            if (c.getVal().equals("Ace")) dealerAceCount++;
        }

        if (player.get(0).getVal().equals(player.get(1).getVal()) && bet <= (tokens / 2)) split = true;
        if (bet <= (tokens / 2)) doubleT = true;

        // Call updateGameView after initializing player
        updateGameView();
        if(playerTotal==21 || dealerTotal==21){
            betTextField.setVisible(false);
            betButton.setVisible(false);
            betTag.setVisible(false);
            scoreComp(playerTotal,dealerTotal);
        }
        else{
            betTextField.setVisible(false);
            betButton.setVisible(false);
            betTag.setVisible(false);
            hitButton.setVisible(true);
            standButton.setVisible(true);
            if (bet<=tokens) doubleButton.setVisible(true);
            gameStatusLabel.setVisible(false);
        }
    }


    // ... (add other methods as needed)

    private class Card {

        private String val;
        private String suit;

        public String getVal() {
            return val;
        }

        public String getSuit() {
            return suit;
        }

        public Card(String val, String suit) {
            this.val = val;
            this.suit = suit;
        }
    }

    private class CardList {

        private ArrayList<Card> cards;

        String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};
        String[] values = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

        public CardList() {
            scramble();
        }

        private void scramble() {
            cards = new ArrayList<>();
            for (String suit : suits) {
                for (String val : values) {
                    cards.add(new Card(val, suit));
                }
            }
        }

        public Card getRandomCard() {
            // removes and retrieves card at random location within cards
            return cards.remove((int) (Math.random() * cards.size()));
        }
    }
}
