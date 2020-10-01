package a3.quinzical.frontend.controllers;

import a3.quinzical.backend.database.GameDatabase;
import a3.quinzical.frontend.helper.ScreenSwitcher;
import a3.quinzical.frontend.helper.ScreenType;

// JavaFX dependencies.
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;

// Java dependencies.
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * This is the controller class for the GameFinished screen.
 * @author Shrey Tailor, Jason Wang
 */
public class GameFinishedController implements Initializable {

    @FXML Label winningsPlaceholder;

    private GameDatabase _db = GameDatabase.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        winningsPlaceholder.setText("You have won a total of $" + _db.getWinning() + ".");
    }

    /**
     * This is the handler used when the "Back" button is pressed.
     */
    @FXML
    public void handleBackButton() {
        ScreenSwitcher.getInstance().setScreen(ScreenType.MAIN_MENU);
        ScreenSwitcher.getInstance().setTitle("Main Menu");
    }

    /**
     * This is the handler used when the "Reset" button is pressed.
     */
    @FXML
    public void handleResetButton() {
        String message = "Are you sure you want to reset the game?";

        // Creating an alert message asking the user to confirm.
        Alert resetAlert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        resetAlert.setHeaderText(null);
        resetAlert.showAndWait();

        // If the user confirmed, then kill the current instance of the GameDatabase.
        if (resetAlert.getResult() == ButtonType.YES) {
            GameDatabase.kill();

            // Show the GameModule grid after being reset.
            try {
                ScreenSwitcher.getInstance().addScreen(ScreenType.GAME_MODULE, FXMLLoader.load(getClass().getResource("./../fxml/GameModule.fxml")));
            } catch (IOException error) {  };
            ScreenSwitcher.getInstance().setScreen(ScreenType.GAME_MODULE);
        }
    }

}