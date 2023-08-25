package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.GameState;

public class ScreenStartController {
  @FXML private ImageView easy;
  @FXML private ImageView medium;
  @FXML private ImageView hard;
  @FXML AnchorPane anchorPane;
  // 0 is easy 1 is medium 2 is hard
  private int selected = 0;

  public void initialize() {
    anchorPane.requestFocus();
    System.out.println("AnchorPane focused: " + anchorPane.isFocused());
  }

  @FXML
  private void levelClick(MouseEvent event) {
    choseLevel();
  }

  @FXML
  private void overEasy(MouseEvent event) {
    removeSelected();
    selected = 0;
    onSelect();
  }

  @FXML
  private void overMedium(MouseEvent event) {
    removeSelected();
    selected = 1;
    onSelect();
  }

  @FXML
  private void overHard(MouseEvent event) {
    removeSelected();
    selected = 2;
    onSelect();
  }

  private void removeSelected() {
    switch (selected) {
      case 0:
        easy.setImage(new Image("/images/easyBlue.png"));
        break;
      case 1:
        medium.setImage(new Image("/images/mediumBlue.png"));
        break;
      case 2:
        hard.setImage(new Image("/images/hardBlue.png"));
        break;
    }
  }

  private void onSelect() {
    switch (selected) {
      case 0:
        easy.setImage(new Image("/images/easyGreen.png"));
        break;
      case 1:
        medium.setImage(new Image("/images/mediumGreen.png"));
        break;
      case 2:
        hard.setImage(new Image("/images/hardGreen.png"));
        break;
    }
  }

  @FXML
  public void onKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.UP) {
      removeSelected();
      if (selected-- < 0) {
        selected = 2;
      }
      onSelect();
    } else if (event.getCode() == KeyCode.DOWN) {
      removeSelected();
      if (selected++ > 2) {
        selected = 0;
      }
      onSelect();
    } else if (event.getCode() == KeyCode.ENTER) {
      choseLevel();
    }
  }

  public void choseLevel() {
    easy.setVisible(false);
    medium.setVisible(false);
    hard.setVisible(false);
    switch (selected) {
      case 0:
        GameState.hints = "Unlimited";
        break;
      case 1:
        GameState.hints = "5";
        break;
      case 2:
        GameState.hints = "0";
        break;
    }
  }
}
