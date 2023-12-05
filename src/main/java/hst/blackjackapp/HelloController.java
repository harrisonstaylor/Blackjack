package hst.blackjackapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label startText;


    @FXML
    protected void startGame() {
        startText.setText("You have 100 tokens!");
    }
}