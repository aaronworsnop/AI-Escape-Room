package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.MouseClick;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Class responsible for controlling the Start Screen of the game. */
public class StartScreenController {
  @FXML AnchorPane anchorPane;
  // 0 is easy 1 is medium 2 is hard
  private int selectedLevel = 0;
  private int selectedTime = 2;

  private Boolean onLevel = true;
  private Boolean onTime = false;

  @FXML Label screenStage;

  @FXML Label lblExit;

  @FXML Label lblEasy;
  @FXML Label lblMedium;
  @FXML Label lblHard;

  @FXML Label lbl2Min;
  @FXML Label lbl4Min;
  @FXML Label lbl6Min;

  @FXML Label timeSummary;
  @FXML Label levelSummary;
  @FXML Label timeSummaryVal;
  @FXML Label levelSummaryVal;

  @FXML Label lblStart;

  private final DropShadow dropShadow = new DropShadow();
  private final DropShadow startDropShadow = new DropShadow();

  /** Initializes the ScreenStartController, setting focus on anchorPane. */
  public void initialize() {
    anchorPane.requestFocus();
    System.out.println("AnchorPane focused: " + anchorPane.isFocused());

    // Button drop shadow
    dropShadow.setColor(Color.web("#007aec"));
    dropShadow.setRadius(5.0);

    // Start button drop shadow
    startDropShadow.setColor(Color.web("#c31212"));
    startDropShadow.setRadius(10.0);
  }

  /**
   * Handles mouse click events for selecting levels.
   *
   * @param event MouseEvent object.
   */
  @FXML
  private void levelClicked(MouseEvent event) {
    chooseLevel();
  }

  /**
   * Handles mouse click events for selecting time.
   *
   * @param event The mouse event object.
   */
  @FXML
  private void timeClicked(MouseEvent event) {
    chooseTime();
  }

  /**
   * Handles mouse click events for the "Back" button.
   *
   * @param event The mouse event object.
   */
  @FXML
  private void lblExitClicked(MouseEvent event) {
    goBack();
  }

  /**
   * Updates the exit button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lblExitEntered(MouseEvent event) {
    setEntered(lblExit);
  }

  /**
   * Updates the exit button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lblExitExited(MouseEvent event) {
    setExited(lblExit);
  }

  /**
   * Updates the easy button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lblEasyEntered(MouseEvent event) {
    setEntered(lblEasy);
    selectedLevel = 0;
  }

  /**
   * Updates the medium button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lblEasyExited(MouseEvent event) {
    setExited(lblEasy);
  }

  /**
   * Updates the medium button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lblMediumEntered(MouseEvent event) {
    setEntered(lblMedium);
    selectedLevel = 1;
  }

  /**
   * Updates the hard button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lblMediumExited(MouseEvent event) {
    setExited(lblMedium);
  }

  /**
   * Updates the hard button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lblHardEntered(MouseEvent event) {
    setEntered(lblHard);
    selectedLevel = 2;
  }

  /**
   * Updates the hard button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lblHardExited(MouseEvent event) {
    setExited(lblHard);
  }

  /**
   * Updates the 2 minutes button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lbl2MinEntered(MouseEvent event) {
    setEntered(lbl2Min);
    selectedTime = 2;
  }

  /**
   * Updates the 2 minutes button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lbl2MinExited(MouseEvent event) {
    setExited(lbl2Min);
  }

  /**
   * Updates the 4 minutes button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lbl4MinEntered(MouseEvent event) {
    setEntered(lbl4Min);
    selectedTime = 4;
  }

  /**
   * Updates the 4 minutes button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lbl4MinExited(MouseEvent event) {
    setExited(lbl4Min);
  }

  /**
   * Updates the 6 minutes button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lbl6MinEntered(MouseEvent event) {
    setEntered(lbl6Min);
    selectedTime = 6;
  }

  /**
   * Updates the 6 minutes button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lbl6MinExited(MouseEvent event) {
    setExited(lbl6Min);
  }

  /**
   * Updates the start button when the mouse hovers over it.
   *
   * @param event MouseEvent for hovering over the button.
   */
  @FXML
  private void lblStartEntered(MouseEvent event) {
    lblStart.setEffect(startDropShadow);
    lblStart.setTextFill(Color.web("#c31212"));
    lblStart.setStyle(
        "-fx-border-radius: 5px; -fx-border-color: #c31212; -fx-background-radius: 5px;"
            + " -fx-background-color: black; -fx-padding: 7px;");
  }

  /**
   * Updates the start button when the mouse leaves its area.
   *
   * @param event MouseEvent for leaving the button.
   */
  @FXML
  private void lblStartExited(MouseEvent event) {
    lblStart.setEffect(null);
    lblStart.setTextFill(Color.WHITE);
    lblStart.setStyle(
        "-fx-border-radius: 5px; -fx-border-color: #bfbfbf; -fx-background-radius: 5px;"
            + " -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 7px;");
  }

  /**
   * Helper method to update the UI when hovering over buttons.
   *
   * @param label The label to update.
   */
  private void setEntered(Label label) {
    label.setEffect(dropShadow);
    label.setTextFill(Color.WHITE);
    label.setStyle(
        "-fx-border-radius: 5px; -fx-border-color: white; -fx-background-radius: 5px;"
            + " -fx-background-color: black; -fx-padding: 7px;");
  }

  /**
   * Helper method to update the UI when leaving buttons.
   *
   * @param label The label to update.
   */
  private void setExited(Label label) {
    label.setEffect(null);
    label.setTextFill(Color.web("#bfbfbf"));
    label.setStyle(
        "-fx-border-radius: 5px; -fx-border-color: #bfbfbf; -fx-background-radius: 5px;"
            + " -fx-background-color: black; -fx-padding: 7px;");
  }

  /**
   * Initiates the game when the "Start" button is clicked.
   *
   * @param event The mouse event object.
   * @throws IOException if an I/O error occurs.
   */
  @FXML
  private void lblStartClicked(MouseEvent event) throws IOException {
    new MouseClick().play();
    GameState.timer.setTimeSecondsProperty(selectedTime * 60);

    // Start the timer.
    GameState.timer.start();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    App.setUserInterface(AppUi.UIOVERLAY);
    double additionalWidth = stage.getWidth() - stage.getScene().getWidth();
    double additionalHeight = stage.getHeight() - stage.getScene().getHeight();
    stage.setWidth(1100 + additionalWidth);
    stage.setHeight(600 + additionalHeight);
  }

  /** Helper method to navigate back in the options. */
  private void goBack() {
    new MouseClick().play();
    if (onTime) {
      lblEasy.setVisible(true);
      lblMedium.setVisible(true);
      lblHard.setVisible(true);
      lbl2Min.setVisible(false);
      lbl4Min.setVisible(false);
      lbl6Min.setVisible(false);
      onLevel = true;
      onTime = false;
      screenStage.setText("Difficulty Select");
      lblExit.setText("Exit");
    } else if (onLevel) {
      System.exit(0);
    } else {
      lbl2Min.setVisible(true);
      lbl4Min.setVisible(true);
      lbl6Min.setVisible(true);
      timeSummary.setVisible(false);
      levelSummary.setVisible(false);
      timeSummaryVal.setVisible(false);
      levelSummaryVal.setVisible(false);
      lblStart.setVisible(false);
      onTime = true;
      screenStage.setText("Time Select");
    }
  }

  /** Helper method to set the selected level and update the UI accordingly. */
  private void chooseLevel() {
    new MouseClick().play();
    // makes levels not visible but time visible

    onLevel = false;
    onTime = true;
    lblEasy.setVisible(false);
    lblMedium.setVisible(false);
    lblHard.setVisible(false);
    lbl2Min.setVisible(true);
    lbl4Min.setVisible(true);
    lbl6Min.setVisible(true);

    lblExit.setText("Back");
    screenStage.setText("Time Select");
    switch (selectedLevel) {
      case 0:
        GameState.hints.set("\u221E");
        GameState.difficulty = "easy";
        break;
      case 1:
        GameState.hints.set("5");
        GameState.difficulty = "medium";
        break;
      case 2:
        GameState.hints.set("0");
        GameState.difficulty = "hard";
        break;
    }
  }

  /** Helper method to set the selected time and update the UI accordingly. */
  private void chooseTime() {
    new MouseClick().play();
    // makes levels not visible but time visible

    onTime = false;
    lbl2Min.setVisible(false);
    lbl4Min.setVisible(false);
    lbl6Min.setVisible(false);
    lblStart.setVisible(true);
    timeSummary.setVisible(true);
    levelSummary.setVisible(true);
    timeSummaryVal.setVisible(true);
    levelSummaryVal.setVisible(true);

    screenStage.setText("Ready?");
    String difficultyText =
        GameState.difficulty.substring(0, 1).toUpperCase() + GameState.difficulty.substring(1);
    levelSummaryVal.setText(difficultyText);
    timeSummaryVal.setText(selectedTime + " minutes");
    GameState.time = selectedTime;
    System.out.println("Level: " + selectedLevel);
    System.out.println("Time: " + selectedTime);
  }
}