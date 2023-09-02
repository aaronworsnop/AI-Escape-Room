package nz.ac.auckland.se206;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;

/** A singleton class that manages the shared chat functionality in the application. */
public class SharedChat {
  private static SharedChat instance;

  /**
   * Retrieves the singleton instance of SharedChat.
   *
   * @return The singleton instance.
   */
  public static SharedChat getInstance() {
    if (instance == null) {
      instance = new SharedChat();
    }
    return instance;
  }

  private final StringProperty text;

  /** Constructs a new SharedChat instance with an initial welcome message. */
  private SharedChat() {
    this.text =
        new SimpleStringProperty(
            "Grand Wizard: Welcome To My Dungeon Click On The Hints Button If You Are Stuck!!!"
                + "\n"
                + "\n");
  }

  /**
   * Retrieves the StringProperty for text messages.
   *
   * @return The StringProperty for text messages.
   */
  public StringProperty getTextProperty() {
    return text;
  }

  /**
   * Retrieves the text message content.
   *
   * @return The text message content as a string.
   */
  public final String getText() {
    return getTextProperty().get();
  }

  /**
   * Sets the text message content.
   *
   * @param text The new text message content.
   */
  public final void setText(String text) {
    getTextProperty().set(text);
  }

  /** Resets the instance fields to their initial values. (to be implemented) */
  public void restart() {
    this.text.set(
        "Grand Wizard: Welcome To My Dungeon Click On The Hints Button If You Are Stuck!!!\n\n");
  }

  /**
   * Handles the send operation for the chat. Checks for hints and clues, updates the game state,
   * and triggers asynchronous actions for waiting for a response.
   *
   * @param textField The TextField containing the user message.
   * @param room The room context for the message.
   */
  public void onSend(TextField textField, String room) {
    String msg = textField.getText();

    if (msg.trim().isEmpty()) {
      return;
    }
    msg = msg.toLowerCase();
    textField.clear();
    for (String keyWords : GameState.clueList) {
      if (msg.contains(keyWords)) {
        if (GameState.hints.get().equals("\u221E")) {
          break;
        } else if (Integer.parseInt(GameState.hints.get()) == 0) {
          msg = "Tell the player they have no more hints left";
        } else {
          GameState.hints.set(Integer.toString(Integer.parseInt(GameState.hints.get()) - 1));
          msg = "I have " + GameState.hints.get() + " hints left " + msg;
        }
        break;
      }
    }
    GameState.gameMaster.addMessage(room, "user", msg);
    GameState.gameMaster.runContext(room);

    String message = GameState.name + ": " + msg;
    this.setText(this.getText() + message + "\n\n");

    Task<Void> waitForResponseTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            GameState.gameMaster.waitForContext(room);
            return null;
          }
        };

    waitForResponseTask.setOnSucceeded(
        e -> {
          this.setText(
              GameState.name
                  + ": "
                  + this.getText()
                  + GameState.gameMaster.getLastResponse(room).getContent()
                  + "\n\n");
        });

    new Thread(waitForResponseTask).start();
  }
}
