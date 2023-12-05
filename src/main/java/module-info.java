module hst.blackjackapp {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens hst.blackjackapp to javafx.fxml;
    exports hst.blackjackapp;
}