package quinzical.frontend.controllers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import quinzical.backend.Progression;
import javafx.scene.control.ProgressBar;
import quinzical.frontend.helper.Speaker;
import quinzical.frontend.helper.ScreenType;
import quinzical.backend.database.GameDatabase;
import quinzical.frontend.helper.ScreenSwitcher;

// Java dependencies.
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;

// JavaFX dependencies.
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;

/**
 * This class is the controller class for the MainMenu screen.
 * @author Shrey Tailor, Jason Wang
 */
public class MainMenuController implements Initializable {

    @FXML Button exitButton;
    @FXML Button statsButton;
    @FXML Label xpLevelLabel;
    @FXML Button settingsButton;
    @FXML Button gameModuleButton;
    @FXML ProgressBar progressBar;
    @FXML Button practiceModuleButton;

    private final Speaker speaker = Speaker.init();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Getting the current XP from the Progression class.
        Progression progression = Progression.getInstance();
        int totalPoints = progression.getEXP();
        int quotient = (totalPoints / 1000) + 1;
        int remainder = totalPoints % 1000;

        // Setting the XP bar, such that it shows the current accumulated XP.
        xpLevelLabel.setText(String.valueOf(quotient));
        progressBar.setProgress((double) remainder/1000);
    }

    @FXML
    private void handlePracticeModuleButton() {
        ScreenSwitcher switcher = ScreenSwitcher.getInstance();
        switcher.switchTo(ScreenType.PRACTICE_MODULE);
        switcher.setTitle("Practice Module");
    }

    @FXML
    private void handleGameModuleButton() {
        /*
         * Routing performed dynamically depending on whether there already exists progress in the
         * current game or not, and other factors.
         */
        ScreenSwitcher switcher = ScreenSwitcher.getInstance();

        // If there exists progress of the user...
        if (GameDatabase.singletonExist()) {
            // If there are questions remaining in the progress...
            if (GameDatabase.getInstance().getRemainingClues() > 0) {
                switcher.switchTo(ScreenType.GAME_MODULE);
            } else {
                switcher.switchTo(ScreenType.GAME_FINISHED);
            }
        } else {
            switcher.switchTo(ScreenType.CHOOSE_CATEGORIES);
        }

        switcher.setTitle("Game Module");
    }

    @FXML
    private void handleSettingsButton() {
        int WIDTH = 600;
        int HEIGHT = 400;

        // Creating and configuring the new stage for Settings.
        Stage settingsStage = new Stage();
        settingsStage.setResizable(false);
        settingsStage.setTitle("Settings");

        try {
            URL url = getClass().getClassLoader().getResource("quinzical/resources/fxml/Settings.fxml");
            settingsStage.setScene(new Scene(FXMLLoader.load(url), WIDTH, HEIGHT));
        } catch (IOException error) {
            System.out.println("There has been a problem with the Settings screen. Please contact the developer!");
            error.printStackTrace();
        }

        // Centering the Settings on screen, when opened.
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        settingsStage.setX((screen.getWidth() - WIDTH) / 2);
        settingsStage.setY((screen.getHeight() - HEIGHT) / 2);

        // These extra settings are necessary to block interactions on the main stage.
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initOwner(ScreenSwitcher.getInstance().getStage().getScene().getWindow());

        // If the screen was closed before the voice stops speaking, then we are stopping synthesis.
        settingsStage.setOnCloseRequest(event -> {
            speaker.kill();
        });
        settingsStage.show();
    }

    @FXML
    private void handleExitButton() {
        ScreenSwitcher.getInstance().exit();
    }

    @FXML
    private void handleStatsButton() {
        ScreenSwitcher switcher = ScreenSwitcher.getInstance();
        switcher.switchTo(ScreenType.STATS);
        switcher.setTitle("Statistics");
    }
}
