package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.Items.BlueWire;
import nz.ac.auckland.se206.Items.GreenWire;
import nz.ac.auckland.se206.Items.Inventory;
import nz.ac.auckland.se206.Items.Object;
import nz.ac.auckland.se206.Items.RedWire;
import nz.ac.auckland.se206.SceneManager.Puzzle;
import nz.ac.auckland.se206.SceneManager.Rooms;
import nz.ac.auckland.se206.controllers.GameMasterActions;
import nz.ac.auckland.se206.gpt.GameMaster;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether the riddle room is active or not */
  public static boolean riddleRoomActive = false;

  @FXML public static Rectangle riddleGlow;

  public static void setRiddleGlow() {
    if (riddleRoomActive) {
      riddleGlow.setVisible(true);
    } else {
      riddleGlow.setVisible(false);
    }
  }

  public static List<Boolean> candleOrder = new ArrayList<Boolean>();

  public static StringProperty hints = new SimpleStringProperty("\u221E");

  public static int time = 2;
  public static String difficulty = "easy";

  public static boolean escaped = false;
  public static String escapeMessage = "You made it out! Good job...";
  public static boolean lastMessageWasFromPlayer = false;

  public static CountdownTimer timer = CountdownTimer.getInstance(GameState.time * 60);
  public static Inventory inventory = new Inventory();
  public static HashMap<ImageView, Object> currentRoomItems = new HashMap<ImageView, Object>();
  public static GameMaster gameMaster = new GameMaster();
  public static String name = "user";
  public static SharedChat chat = new SharedChat();
  public static GameMasterActions gameMasterActions;
  // Room Control
  public static SimpleObjectProperty<Rooms> currentRoom =
      new SimpleObjectProperty<SceneManager.Rooms>(Rooms.MAINROOM);
  public static SimpleObjectProperty<Puzzle> currentPuzzle =
      new SimpleObjectProperty<SceneManager.Puzzle>(Puzzle.NONE);
  public static Map<Puzzle, BooleanProperty> puzzleSolved = new HashMap<Puzzle, BooleanProperty>();

  // Padlock Game
  public static String padlockAnswer = "YoMama";

  public static RedWire redWire = new RedWire();
  public static GreenWire greenWire = new GreenWire();
  public static BlueWire blueWire = new BlueWire();

  public static int numInventorySlots = 0;

  public static int wallCount = 3;

  public static String[] clueFirst = {
    "help", "hint", "what", "how", "where", "who", "why", "can", "do", "stuck", "lost", "tell",
    "show", "give", "need", "find", "solve", "figure", "unlock", "explain", "Im", "I'm", "I", "need"
  };

  public static String[] clueSecond = {
    "key",
    "lock",
    "door",
    "safe",
    "code",
    "map",
    "box",
    "book",
    "flashlight",
    "mirror",
    "painting",
    "clock",
    "compass",
    "notebook",
    "candles",
    "chessboard",
    "cards",
    "symbols",
    "dice",
    "magnifier",
    "password",
    "riddle",
    "sequence",
    "timer",
    "clue",
    "pattern",
    "combination",
    "lever",
    "switch",
    "cipher",
    "equation",
    "maze",
    "colors",
    "numbers",
    "letters",
    "compartment",
    "trapdoor",
    "light",
    "sound",
    "next",
    "proceed",
    "hints",
    "hint",
    "do",
    "scroll",
    "stuck"
  };

  public static void resetGame() {
    wallCount = 3;
    candleOrder.clear(); // Assuming you want to empty the list
    escaped = false;
    timer = CountdownTimer.getInstance(time * 60); // Assuming getInstance resets the timer
    currentRoom.set(Rooms.MAINROOM);
    currentPuzzle.set(Puzzle.NONE);
  }
}
