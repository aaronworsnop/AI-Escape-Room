package nz.ac.auckland.se206.controllers.minigames;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.Puzzle;

/** Controller for the Pipe Connecting Mini-game. */
public class PipeConnectingController {
  @FXML private AnchorPane gridAnchor;

  @FXML private GridPane grid;

  private int gridXSize, gridYSize;
  private double gridCellSize, rectWidth, rectHeight;
  private int[][] mapSetup;
  private List<Point> inlets;
  private int[][] mapRotations;
  private Pane[][] gridPanes;

  /** Represents a point in the grid. */
  private class Point {
    private int xValue;
    private int yValue;

    /**
     * Constructor to initialize the Point.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Point(int xValue, int yValue) {
      this.xValue = xValue;
      this.yValue = yValue;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Point point = (Point) obj;
      return xValue == point.xValue && yValue == point.yValue;
    }

    @Override
    public int hashCode() {
      int result = 17;
      result = 31 * result + xValue;
      result = 31 * result + yValue;
      return result;
    }
  }

  /** Initializes the grid based on the game's difficulty. */
  @FXML
  public void initialize() {
    setGamesDifficulty();
    initializeDataStructures();
    generateMapSetup();
    createGrid();
  }

  /** Sets the size of the grid based on the game's difficulty. */
  private void setGamesDifficulty() {
    // Was variable but is now constant
    gridXSize = 4;
    gridYSize = 3;
    gridCellSize = 80;

    rectWidth = gridCellSize / 4;
    rectHeight = gridCellSize / 2 + rectWidth / 2;
  }

  /** Resets the datastructures to default values */
  private void initializeDataStructures() {
    mapRotations = new int[gridXSize][gridYSize];
    gridPanes = new Pane[gridXSize][gridYSize];
    inlets = new ArrayList<Point>();
  }

  /** Creates the grid represents by mapsetup and the game difficulty */
  private void createGrid() {
    // initiates a random variable
    Random rand = new Random();
    // randomly creates the grid
    gridAnchor.setLayoutX((800 - (gridXSize + 1) * gridCellSize) / 2);
    gridAnchor.setLayoutY((500 - (gridYSize + 1) * gridCellSize) / 2);
    gridAnchor.setPrefSize((gridXSize + 1) * gridCellSize, (gridYSize + 1) * gridCellSize);
    grid.setLayoutX(gridCellSize / 2);
    grid.setLayoutY(gridCellSize / 2);
    // set the size
    grid.setPrefSize(gridXSize * gridCellSize, gridYSize * gridCellSize);
    for (int i = 0; i < gridXSize; i++) {
      grid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(gridCellSize));
    }
    // loops through the grid
    for (int i = 0; i < gridYSize; i++) {
      grid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(gridCellSize));
    }
    // gives the grid a style color
    grid.setStyle("-fx-background-color: #FFFFFF;");

    for (int x = 0; x < gridXSize; x++) {
      for (int y = 0; y < gridYSize; y++) {
        mapRotations[x][y] = rand.nextInt(4);
        Pane pane = createPane(mapSetup[x][y], x, y);
        gridPanes[x][y] = pane;
        grid.add(pane, x, y);
      }
    }
    // create the rectangles
    createInletRectangles();
  }

  /**
   * This method generates a randomized grid map setup. The grid is represented by a 2D integer
   * array with each integer encoding connection directions using binary representation. Bits
   * represent, in order, connections to the North, East, South, and West. For example, 0b0100
   * represents a connection to the East.
   */
  private void generateMapSetup() {
    // Initialize a random number generator
    Random rand = new Random();

    // Initialize the grid based on provided dimensions
    mapSetup = new int[gridXSize][gridYSize];

    // Variable to store the starting point for the grid paths
    Point start = null;

    // List to store inlet points around the grid boundary
    inlets = new ArrayList<Point>();

    // Randomly decide the number of inlets, minimum 2 and maximum 5
    int numInlets = rand.nextInt(4) + 2;
    System.out.println(numInlets);

    // Randomly place inlets on the grid boundary, starting from the top-left and moving clockwise
    for (int i = 0; i < numInlets; i++) {
      int position = rand.nextInt(gridXSize * 2 + gridYSize * 2);

      // Place on top edge
      if (position < gridXSize) {
        mapSetup[position][0] |= 0b1000;
        inlets.add(new Point(position, -1));
        if (start == null) {
          start = new Point(position, 0);
        }

        // Place on right edge
      } else if (position < gridXSize + gridYSize) {
        int x = gridXSize - 1;
        int y = position - gridXSize;
        mapSetup[x][y] |= 0b0100;
        inlets.add(new Point(gridXSize, y));
        if (start == null) {
          start = new Point(x, y);
        }

        // Place on bottom edge
      } else if (position < gridXSize * 2 + gridYSize) {
        int x = gridXSize - 1 - (position - gridXSize - gridYSize);
        int y = gridYSize - 1;
        mapSetup[x][y] |= 0b0010;
        inlets.add(new Point(x, gridYSize));
        if (start == null) {
          start = new Point(x, y);
        }

        // Place on left edge
      } else {
        int x = 0;
        int y = gridYSize - 1 - (position - gridXSize * 2 - gridYSize);
        mapSetup[x][y] |= 0b0001;
        inlets.add(new Point(-1, y));
        if (start == null) {
          start = new Point(x, y);
        }
      }
    }

    // Set up a list of available squares for path generation
    Set<Point> availableSquares = new HashSet<>();
    for (int x = 0; x < gridXSize; x++) {
      for (int y = 0; y < gridYSize; y++) {
        availableSquares.add(new Point(x, y));
      }
    }

    // Start path generation from the start point
    List<Point> activePaths = new ArrayList<>();
    activePaths.add(start);
    availableSquares.remove(start);

    // Define possible directions for path movement
    int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    // Set a limit on the maximum iterations to prevent infinite loops
    int maxIterations = 10000;
    int currentIterations = 0;

    // Generate paths until no more active paths remain or maxIterations is reached
    while (!activePaths.isEmpty() && currentIterations < maxIterations) {
      currentIterations++;

      // Pick a random current point from the active paths
      Point current = activePaths.get(rand.nextInt(activePaths.size()));

      // List to store neighboring points which haven't been visited
      List<Point> unvisitedNeighbors = new ArrayList<>();
      // List to store neighbors with exactly two connections
      List<Point> twoConnectedNeighbors = new ArrayList<>();

      // Identify valid neighbors of the current point
      for (int[] d : directions) {
        Point neighbor = new Point(current.xValue + d[0], current.yValue + d[1]);
        if (isWithinGrid(neighbor)) {
          if (availableSquares.contains(neighbor)) {
            unvisitedNeighbors.add(neighbor);
          } else {
            int connections = Integer.bitCount(mapSetup[neighbor.xValue][neighbor.yValue]);
            if (connections == 2) {
              twoConnectedNeighbors.add(neighbor);
            }
          }
        }
      }

      // Log a warning if maxIterations is reached
      if (currentIterations >= maxIterations) {
        System.out.println("Warning: Maximum iterations reached. Exiting...");
      }

      // If no suitable neighbors are found, remove current point from active paths
      if (unvisitedNeighbors.isEmpty() && twoConnectedNeighbors.isEmpty()) {
        activePaths.remove(current);
        continue;
      }

      // Choose a neighbor to extend the path to
      Point chosenNeighbor;
      if (!unvisitedNeighbors.isEmpty()) {
        chosenNeighbor = unvisitedNeighbors.get(rand.nextInt(unvisitedNeighbors.size()));
        activePaths.add(chosenNeighbor);
        availableSquares.remove(chosenNeighbor);
      } else {
        chosenNeighbor = twoConnectedNeighbors.get(rand.nextInt(twoConnectedNeighbors.size()));
        // Do not continue walking from a thrice-connected cell
        activePaths.remove(current);
      }

      // Update the map to record the path connection
      if (chosenNeighbor.xValue > current.xValue) {
        mapSetup[current.xValue][current.yValue] |= 0b0100;
        mapSetup[chosenNeighbor.xValue][chosenNeighbor.yValue] |= 0b0001;
      } else if (chosenNeighbor.xValue < current.xValue) {
        mapSetup[current.xValue][current.yValue] |= 0b0001;
        mapSetup[chosenNeighbor.xValue][chosenNeighbor.yValue] |= 0b0100;
      } else if (chosenNeighbor.yValue > current.yValue) {
        mapSetup[current.xValue][current.yValue] |= 0b0010;
        mapSetup[chosenNeighbor.xValue][chosenNeighbor.yValue] |= 0b1000;
      } else {
        mapSetup[current.xValue][current.yValue] |= 0b1000;
        mapSetup[chosenNeighbor.xValue][chosenNeighbor.yValue] |= 0b0010;
      }
    }

    // After main path generation, ensure all grid cells have at least two connections
    for (int x = 0; x < gridXSize; x++) {
      for (int y = 0; y < gridYSize; y++) {
        Point cell = new Point(x, y);
        if (Integer.bitCount(mapSetup[x][y]) == 1) {
          // Force a second connection for cells with only one connection
          for (int[] d : directions) {
            Point neighbor = new Point(cell.xValue + d[0], cell.yValue + d[1]);
            if (isWithinGrid(neighbor)
                && Integer.bitCount(mapSetup[neighbor.xValue][neighbor.yValue]) < 3) {
              if (neighbor.xValue > cell.xValue) {
                mapSetup[cell.xValue][cell.yValue] |= 0b0100;
                mapSetup[neighbor.xValue][neighbor.yValue] |= 0b0001;
              } else if (neighbor.xValue < cell.xValue) {
                mapSetup[cell.xValue][cell.yValue] |= 0b0001;
                mapSetup[neighbor.xValue][neighbor.yValue] |= 0b0100;
              } else if (neighbor.yValue > cell.yValue) {
                mapSetup[cell.xValue][cell.yValue] |= 0b0010;
                mapSetup[neighbor.xValue][neighbor.yValue] |= 0b1000;
              } else {
                mapSetup[cell.xValue][cell.yValue] |= 0b1000;
                mapSetup[neighbor.xValue][neighbor.yValue] |= 0b0010;
              }
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Checks if a point is within the grid.
   *
   * @param p Point to check
   * @return true if within grid, false otherwise
   */
  private boolean isWithinGrid(Point p) {
    return p.xValue >= 0 && p.xValue < gridXSize && p.yValue >= 0 && p.yValue < gridYSize;
  }

  /**
   * Creates a pane for a grid cell based on its structure.
   *
   * @param structure Configuration of the pipes
   * @param x x-coordinate
   * @param y y-coordinate
   * @return Pane for the grid cell
   */
  private Pane createPane(int stucture, int x, int y) {
    Pane pane = new Pane();
    pane.setPrefSize(gridCellSize, gridCellSize);
    pane.setOnMouseClicked(this::handlePaneClick);
    pane.setRotate(mapRotations[x][y] * 90);

    double offsetIncrement = (gridCellSize - rectWidth) / 2;
    var children = pane.getChildren();
    // Add pipes
    if ((stucture & 0b1000) != 0) {
      // top
      Rectangle rect = new Rectangle(offsetIncrement, 0, rectWidth, rectHeight);
      rect.setStrokeWidth(0);
      children.add(rect);
    }
    if ((stucture & 0b0100) != 0) {
      // right
      Rectangle rect = new Rectangle(offsetIncrement, offsetIncrement, rectHeight, rectWidth);
      rect.setStrokeWidth(0);
      children.add(rect);
    }
    if ((stucture & 0b0010) != 0) {
      // bottom
      Rectangle rect = new Rectangle(offsetIncrement, offsetIncrement, rectWidth, rectHeight);
      rect.setStrokeWidth(0);
      children.add(rect);
    }
    if ((stucture & 0b0001) != 0) {
      // left
      Rectangle rect = new Rectangle(0, offsetIncrement, rectHeight, rectWidth);
      rect.setStrokeWidth(0);
      children.add(rect);
    }

    return pane;
  }

  /** Creates rectangles to represent inlets in the grid. */
  private void createInletRectangles() {
    for (Point inlet : inlets) {
      // Determine the position of the inlet
      int x = inlet.xValue;
      int y = inlet.yValue;

      double layoutX, layoutY;
      double inletHeight = rectWidth * 1.5;
      boolean horizontal = false;
      if (y == -1) { // Top side
        layoutX = (x + 1) * gridCellSize - 0.5 * rectWidth;
        layoutY = gridCellSize / 2 - inletHeight;
      } else if (x == gridXSize) { // Right side
        layoutX = (gridXSize + 0.5) * gridCellSize;
        layoutY = (y + 1) * gridCellSize - 0.5 * rectWidth;
        horizontal = true;
      } else if (y == gridYSize) { // Bottom side
        layoutX = (x + 1) * gridCellSize - 0.5 * rectWidth;
        layoutY = (gridYSize + 0.5) * gridCellSize;
      } else { // Left side
        layoutX = gridCellSize / 2 - inletHeight;
        layoutY = (y + 1) * gridCellSize - 0.5 * rectWidth;
        horizontal = true;
      }

      // Create the rectangle for the inlet
      double width, height;
      if (horizontal) {
        width = inletHeight;
        height = rectWidth;
      } else {
        width = rectWidth;
        height = inletHeight;
      }
      Rectangle inletRectangle = new Rectangle(width, height);
      inletRectangle.setLayoutX(layoutX);
      inletRectangle.setLayoutY(layoutY);
      inletRectangle.setStrokeWidth(0);

      gridAnchor.getChildren().add(inletRectangle);
    }
  }

  /**
   * Rotates the given cell data by a specified rotation.
   *
   * @param cellData The binary representation of cell's connections.
   * @param rotation Number of times the cell data should be rotated. Positive values rotate
   *     clockwise, negative values rotate counter-clockwise.
   * @return Rotated cell data.
   */
  private int rotateCellData(int cellData, int rotation) {
    rotation = rotation % 4; // since there are only 4 possible states
    if (rotation < 0) rotation += 4; // handle negative rotations

    while (rotation-- > 0) {
      boolean leftMostBit = (cellData & 0b1000) != 0;
      cellData = (cellData << 1) & 0b1111; // left shift and mask to ensure only 4 bits remain
      if (leftMostBit) cellData |= 0b0001; // wrap around
    }

    return cellData;
  }

  /**
   * Checks if a cell and its adjacent cell in a specified direction are connected.
   *
   * @param cell The point representing the current cell's coordinates.
   * @param direction The binary representation of the direction to check.
   * @return True if the cells are connected, otherwise false.
   */
  private boolean areAdjacentCellsConnected(Point cell, int direction) {
    int oppositeDirectionMask;
    Point adjacentCell;

    switch (direction) {
      case 0b1000:
        // Top code so it does the logic
        oppositeDirectionMask = 0b0010;
        adjacentCell = new Point(cell.xValue, cell.yValue - 1);
        break;
      case 0b0100:
        // Right code so it does the logic
        oppositeDirectionMask = 0b0001;
        adjacentCell = new Point(cell.xValue + 1, cell.yValue);
        break;
      case 0b0010:
        // Bottom code so it does the logic
        oppositeDirectionMask = 0b1000;
        adjacentCell = new Point(cell.xValue, cell.yValue + 1);
        break;
      default:
        // Left code so it does the logiccode so it does the logic
        oppositeDirectionMask = 0b0100;
        adjacentCell = new Point(cell.xValue - 1, cell.yValue);
    }

    if (!isWithinGrid(adjacentCell)) {
      // The adjacent cell is outside the grid, so the current cell should be an inlet
      boolean isValidInlet = inlets.contains(adjacentCell);
      if (!isValidInlet) {
        System.out.println(
            "An inlet at position "
                + adjacentCell.xValue
                + ", "
                + adjacentCell.yValue
                + " does not exist!");
      }
      return isValidInlet;
    }

    return (mapSetup[adjacentCell.xValue][adjacentCell.yValue] & oppositeDirectionMask) != 0;
  }

  /**
   * Checks the entire map's grid for connectivity. If any issues with the map's connectivity are
   * found, they will be printed to the console.
   */
  public void checkCompleteness() {
    // loops through and check if completed
    for (int i = 0; i < gridXSize; i++) {
      for (int j = 0; j < gridYSize; j++) {
        Point currentCell = new Point(i, j);
        int cellData = rotateCellData(mapSetup[i][j], mapRotations[i][j]);
        // if all these are true then compelted
        if ((cellData & 0b1000) != 0 && !areAdjacentCellsConnected(currentCell, 0b1000)) return;
        if ((cellData & 0b0100) != 0 && !areAdjacentCellsConnected(currentCell, 0b0100)) return;
        if ((cellData & 0b0010) != 0 && !areAdjacentCellsConnected(currentCell, 0b0010)) return;
        if ((cellData & 0b0001) != 0 && !areAdjacentCellsConnected(currentCell, 0b0001)) return;
      }
    }
    // check the inlet position if its true return
    for (Point inletPosition : inlets) {
      if (!isInletConnected(inletPosition)) return;
    }

    onComplete();
  }

  /**
   * Checks if a specified inlet has any connections.
   *
   * @param inlet The point representing the inlet's coordinates.
   * @return True if the inlet has a connection, otherwise false.
   */
  private boolean isInletConnected(Point inlet) {
    // Define the adjacent point and the bitmask for the direction from the cell to the inlet
    Point adjacentPoint = null;
    int mask = 0;

    // Top side
    if (inlet.yValue == -1) {
      adjacentPoint = new Point(inlet.xValue, 0);
      mask = 0b1000; // Check for the upward pipe from the adjacent cell
    }
    // Right side
    else if (inlet.xValue == gridXSize) {
      adjacentPoint = new Point(gridXSize - 1, inlet.yValue);
      mask = 0b0100; // Check for the rightward pipe from the adjacent cell
    }
    // Bottom side
    else if (inlet.yValue == gridYSize) {
      adjacentPoint = new Point(inlet.xValue, gridYSize - 1);
      mask = 0b0010; // Check for the downward pipe from the adjacent cell
    }
    // Left side
    else if (inlet.xValue == -1) {
      adjacentPoint = new Point(0, inlet.yValue);
      mask = 0b0001; // Check for the leftward pipe from the adjacent cell
    }

    if (adjacentPoint != null) {
      int cellData = mapSetup[adjacentPoint.xValue][adjacentPoint.yValue];
      int rotatedCellData =
          rotateCellData(cellData, mapRotations[adjacentPoint.xValue][adjacentPoint.yValue]);

      // Check if the adjacent cell has a pipe in the direction of the inlet
      return (rotatedCellData & mask) != 0;
    }
    return false;
  }

  /**
   * Event handler for pane clicks. Rotates the clicked pane and checks if the map is complete after
   * every click.
   *
   * @param event The MouseEvent triggered by the click.
   */
  private void handlePaneClick(MouseEvent event) {
    // get the value of the pippuzzle
    if (GameState.puzzleSolved.get(Puzzle.PIPEPUZZLE).getValue()) return;
    Pane pane = (Pane) event.getSource();
    int x = GridPane.getColumnIndex(pane);
    int y = GridPane.getRowIndex(pane);
    // rotates the pipes
    mapRotations[x][y] = (mapRotations[x][y] + 1) % 4;
    pane.setRotate(mapRotations[x][y] * 90);

    checkCompleteness();
  }

  /** Exits the current puzzle and resets the puzzle state to NONE. */
  @FXML
  private void exitPuzzle() {
    System.out.println("Exit");
    GameState.currentPuzzle.setValue(Puzzle.NONE);
  }

  /** Called when the map is found to be complete. Prints a completion message to the console. */
  private void onComplete() {
    GameState.puzzleSolved.get(Puzzle.PIPEPUZZLE).set(true);
    System.out.println("Complete");
    GameState.pipePuzzleSolved = true;
    exitPuzzle();
  }
}
