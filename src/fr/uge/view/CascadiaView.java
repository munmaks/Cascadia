package fr.uge.view;


import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import java.util.Objects;
import java.awt.RenderingHints; // to think if we need it
import java.awt.Graphics2D;
import java.awt.Font;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.data.CascadiaData;
import fr.uge.data.environment.Cell;
import fr.uge.data.environment.Coordinates;
import fr.uge.data.environment.Tile;
import fr.uge.data.environment.TileType;
import fr.uge.data.environment.WildlifeType;


/**
 * The CascadiaView class deals with the display of the game the screen, and
 * with the interpretation of which zones were clicked on by the user.
 *
 * @param xOrigin    Abscissa of the left-hand corner of the area displaying the
 *                   game. Abscissas are counted from left to right.
 * @param yOrigin    Ordinate of the left-hand corner of the area displaying the
 *                   game. Ordinates are counted from top to bottom.
 * @param height     Height of the (square) area displaying the game.
 * @param width      Width of the (square) area displaying the game.
 * @param figureSize Side of the (square) areas displaying individual cells /
 *                   images.
 * @param loader     ImageLoader that dealt with loading the pictures to be
 *                   memorized.
 */
public record CascadiaView(int xOrigin, int yOrigin, int width, int height, int figureSize,
CascadiaData data, ImageLoader loader) {

  private static final Color COLOR_FOREST = Color.GREEN; // new Color(46, 111, 64); // 39, 66, 26
  private static final Color COLOR_WETLAND = new Color(166, 185, 111);
  private static final Color COLOR_MOUNTAIN = Color.GRAY; // new Color(105, 105, 105);
  private static final Color COLOR_RIVER = Color.BLUE;
  private static final Color COLOR_PRAIRIE = Color.ORANGE;

  private static final String ARIAL = "Arial";
  /**
   * Create a new GameView
   *
   * @param xOrigin Abscissa of the left-hand corner of the area displaying the
   *                game. Abscissas are counted from left to right.
   * @param yOrigin Ordinate of the left-hand corner of the area displaying the
   *                game. Ordinates are counted from top to bottom.
   * @param length  Side of the (square) area displaying the game.
   * @param data    CascadiaData storing each piece of information on the current
   *                state of the game to be displayed.
   * @param loader  ImageLoader that dealt with loading the pictures to be
   *                memorized.
   * @return CascadiaView
   */
  public static CascadiaView initGameGraphics(
    int xOrigin, int yOrigin, int width, int height,
    int figureSize, CascadiaData data, ImageLoader loader) {

    Objects.requireNonNull(data);
    Objects.requireNonNull(loader);

    return new CascadiaView(xOrigin, yOrigin, width, height, figureSize, data, loader);
  }

  /**
   * Draws a square at the given position.
   * 
   * @param graphics Graphics engine that will actually display the square.
   * @param x        abscissa of the center of the square.
   * @param y        ordinate of the center of the square.
   * @param size     side of the square.
   * @param color    color of the square.
   */
  private void drawSquare(Graphics2D graphics, float x, float y, int size, Color color) {
    var rectangle = new Rectangle2D.Float(0, 0, 0, 0);
    graphics.setColor(color);
    rectangle.setRect(x - size / 2, y - size / 2, size, size);
    graphics.fill(rectangle);
  }

  /**
   * Draws a rectangle at the given position.
   * @param graphics Graphics engine that will actually display the rectangle.
   * @param x abscissa of the center of the rectangle.
   * @param y ordinate of the center of the rectangle.
   * @param width width of the rectangle.
   * @param height height of the rectangle.
   * @param color color of the rectangle.
   */
  public void drawRectangle(Graphics2D graphics, float x, float y,
      int width, int height, Color color) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(color);

    var rectangle = new Rectangle2D.Float(x, y, width, height);
    graphics.setPaint(Color.ORANGE);
    graphics.setColor(color);
    graphics.fill(rectangle);

  }

  /**
   * Draws a circle at the given position.
   * @param graphics Graphics engine that will actually display the circle.
   * @param x abscissa of the center of the circle.
   * @param y ordinate of the center of the circle.
   * @param size side of the circle.
   * @param color 
   */
  private void drawCircle(Graphics2D graphics, float x, float y, int size, Color color) {
    var ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    graphics.setColor(Color.WHITE);
    graphics.fill(ellipse);

    graphics.setColor(color);
    ellipse.setFrame(x - size / 2, y - size / 2, size, size);
    graphics.fill(ellipse);
  }

  /**
   * Draws a line between two points.
   * @param graphics Graphics engine that will actually display the line.
   * @param x1 
   * @param y1 
   * @param x2
   * @param y2
   * @param color
   */
  public void drawLine(Graphics2D graphics, int x1, int y1, int x2, int y2, Color color) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(color);
    graphics.setColor(color);
    graphics.drawLine(x1, y1, x2, y2);
  }

  /**
   * Draws a round rectangle at the given position.
   * 
   * @param graphics Graphics engine that will actually display the round rectangle.
   * @param x abscissa of the center of the rectangle.
   * @param y ordinate of the center of the rectangle.
   * @param width width of the rectangle.
   * @param height height of the rectangle.
   * @param arcw
   * @param arch
   * @param color color of the rectangle.
   */
  private void drawRoundRectangle(Graphics2D graphics, float x, float y, float width,
      float height, float arcw, float arch, Color color) {

    var qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    var rectangle = new RoundRectangle2D.Float(x, y, width, height, arcw, arch);

    graphics.setPaint(Color.ORANGE);
    graphics.setRenderingHints(qualityHints);
    graphics.setColor(color);
    graphics.fill(rectangle);
  }

  /**
   * Draws an image at the given position.
   * @param graphics Graphics engine that will actually display the image.
   * @param image image to be displayed.
   * @param x abscissa of the center of the image.
   * @param y ordinate of the center of the image.
   * @param dimX width of the image.
   * @param dimY height of the image.
   */
  private void drawImage(Graphics2D graphics, BufferedImage image, float x, float y,
      float dimX, float dimY) {
    var width = image.getWidth();
    var height = image.getHeight();
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
        y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }



  /**
   * Draws the start screen.
   * @param graphics Graphics engine that will actually display the start screen.
   * @param animal Animal to be displayed.
   * @param x abscissa of the center of the image.
   * @param y ordinate of the center of the image.
   * @param size size of the image.
   */
  private void drawAnimal(Graphics2D graphics, WildlifeType animal,
      int x, int y, int size) {
    // If you prefer a switch block, you can still switch on 'animal',
    // but using 'animal.ordinal()' directly is usually enough.
    drawImage(graphics, loader.image(animal.ordinal()), x, y, size, size);
  }

  /**
   * Draws the habitat square.
   * @param graphics Graphics engine that will actually display the habitat square.
   * @param habitatType Habitat type to be displayed.
   * @param x abscissa of the center of the square.
   * @param y ordinate of the center of the square.
   * @param figureSize size of the square.
   */
  private void drawHabitatSquare(Graphics2D graphics, TileType habitatType, int x, int y,
      int figureSize) {
    switch (habitatType) {
    case FOREST->drawSquare(graphics, x, y, figureSize, COLOR_FOREST);
    case WETLAND->drawSquare(graphics, x, y, figureSize, COLOR_WETLAND);
    case MOUNTAIN->drawSquare(graphics, x, y, figureSize, COLOR_MOUNTAIN);
    case RIVER->drawSquare(graphics, x, y, figureSize, COLOR_RIVER);
    case PRAIRIE -> drawSquare(graphics, x, y, figureSize, COLOR_PRAIRIE);}
  }

  /**
   * Draws any animals found on the given cell.
   * @param graphics Graphics engine that will actually display the animals.
   * @param cell Cell to be displayed.
   * @param x abscissa of the center of the cell.
   * @param y ordinate of the center of the cell.
   * @param figureSize size of the cell.
   */
  private void drawMultipleAnimals(Graphics2D graphics,  Cell cell, int x,
      int y, int figureSize) {
    int placement = 0;
    for (var animal :
    cell.getTile().animals()) {
      final int scaledTokenSize = (int) (figureSize / 2.0);
      final int xAnimal = (int) ((x - (figureSize / 2.0) + figureSize / 20.0)
          + placement * (figureSize / 2.5));
      final int yAnimal = (int) (y - (figureSize / 2.0) + figureSize / 4.0);
      placement++;

      drawAnimal(graphics, animal, xAnimal, yAnimal, scaledTokenSize);
    }
  }

  /**
   * Draws the animals on the given cell.
   * @param graphics Graphics engine that will actually display the animals.
   * @param cell Cell to be displayed.
   * @param x abscissa of the center of the cell.
   * @param y ordinate of the center of the cell.
   * @param figureSize size of the cell.
   */
  private void drawAnimalsOnCell(Graphics2D graphics,  Cell cell, int x,
      int y, int figureSize) {
    if (cell.getAnimal() == null) {
      drawMultipleAnimals(graphics, cell, x, y, figureSize);
    } else {
      drawSingleAnimal(graphics, cell.getAnimal(), x, y, figureSize);
    }
  }

  /**
   * Draws a single animal.
   * @param graphics Graphics engine that will actually display the animal.
   * @param animal Animal to be displayed.
   * @param x abscissa of the center of the animal.
   * @param y ordinate of the center of the animal.
   * @param figureSize size of the animal.
   */
  private void drawSingleAnimal(Graphics2D graphics, WildlifeType animal,
      int x, int y, int figureSize) {
    int scaledTokenSize = (int) (figureSize / 1.5);
    int xAnimal = x - figureSize / 3;
    int yAnimal = y - figureSize / 3;

    drawAnimal(graphics, animal, xAnimal, yAnimal, scaledTokenSize);
  }

  /**
   * Draws the environment.
   * @param graphics Graphics engine that will actually display the environment.
   */
  public void drawEnvironment(Graphics2D graphics) {
    Objects.requireNonNull(graphics);
    var listCells = data.getCurrentPlayerCells();

    for (var cell : listCells) {
      if (!cell.isOccupiedByTile()) {
        continue;
      }
      var habitatType = cell.getTile().firstHabitat();
      /* Calculate screen coordinates relative to the center. */
      int x = (int) (((cell.getCoordinates().x() + width / 2 / figureSize)) *
      figureSize) + figureSize / 2;
      int y = (int) (((cell.getCoordinates().y() + height / 2 / figureSize)) *
      figureSize) + figureSize / 2;

      drawHabitatSquare(graphics, habitatType, x, y, figureSize);
      drawAnimalsOnCell(graphics, cell, x, y, figureSize);
    }
  }


  /**
   * Draws the game board.
   * 
   * @param graphics Graphics engine that will actually display the start screen.
   * @param tokenSize size of the token.
   */
  public void showGameBoard(Graphics2D graphics, int tokenSize) {
    Objects.requireNonNull(graphics);
    final float ROW_MULTIPLIER = 1.8f;
    final float CIRCLE_X_OFFSET = 1.05f;
    var baseXOffset = (int) (figureSize / ROW_MULTIPLIER);
    var listOfTiles = data.getGameBoard().getCopyOfTiles();
    var listOfTokens = data.getGameBoard().getCopyOfTokens();

    for (int i = 0; i < 4; ++i) {
      int yPosition = (int) (figureSize * ROW_MULTIPLIER * (i + 1));
      drawTileAndAnimals(graphics, listOfTiles.get(i), baseXOffset, yPosition, figureSize);
      drawWildlifeToken(graphics, listOfTokens.get(i), tokenSize,
          (int) (baseXOffset + figureSize * CIRCLE_X_OFFSET + tokenSize / 5),
          (int) (yPosition + tokenSize / 5));
      if (i != 3) {
        drawLine(graphics, (int) (figureSize / 3), (int) (yPosition + figureSize * 1.4),
            (int) (figureSize / 3 + figureSize * 2.35), (int) (yPosition + figureSize * 1.4),
            Color.BLACK);
      }
    }
  }

  /**
   * Draws the information.
   * @param graphics Graphics engine that will actually display the information.
   * @param tile Tile to be displayed.
   * @param x abscissa of the center of the tile.
   * @param y ordinate of the center of the tile.
   * @param figureSize size of the tile.
   */
  private void drawTileAndAnimals(Graphics2D graphics, Tile tile, int x,
      int y, int figureSize) {
    /* Draw habitat rectangle */
    switch (tile.firstHabitat()){
      case FOREST->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_FOREST);
      case WETLAND->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_WETLAND);
      case MOUNTAIN->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_MOUNTAIN);
      case RIVER->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_RIVER);
      case PRAIRIE -> drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_PRAIRIE);
    }

    int placement = 0;
    for (var animal : tile.animals()) {
      int scaled = figureSize / 2;
      int xAnimal = (int) (x + figureSize / 20.0 + placement * (figureSize / 2.5));
      int yAnimal = y + figureSize / 4;
      placement++;
      drawImage(graphics, loader.image(animal.ordinal()), xAnimal, yAnimal, scaled, scaled);
    }
  }

  /**
   * draw wildlife token.
   * @param graphics Graphics engine that will actually display the wildlife token.
   * @param token WildlifeType to be displayed.
   * @param tokenSize size of the token.
   * @param circleX abscissa of the center of the token.
   * @param circleY ordinate of the center of the token.
   */
  private void drawWildlifeToken(Graphics2D graphics, WildlifeType token,
      int tokenSize, int circleX, int circleY) {
    int scaledTokenSize = (int) (tokenSize / 1.5);
    drawImage(graphics, loader.image(token.ordinal()), circleX, circleY, scaledTokenSize,
        scaledTokenSize);
  }




  /**
   * Draws a rectangle with text.
   * @param graphics Graphics engine that will actually display the rectangle with text.
   */
  public void showPossibleCoordinates(Graphics2D graphics) {
    Objects.requireNonNull(graphics);
    var listOfCells = data.getCurrentPlayerCells();
    for (var cell : listOfCells) {
      if (cell == null) {
        throw new IllegalArgumentException("cell is null in showPossibleCoordinates()");
      }
      if (!cell.isOccupiedByTile()) {
        int x = (int) (((cell.getCoordinates().x() + width / 2 / figureSize)) * figureSize)
            + figureSize / 2;
        int y = (int) (((cell.getCoordinates().y() + height / 2 / figureSize)) * figureSize)
            + figureSize / 2;
        drawSquare(graphics, x, y, figureSize, Color.RED);
      }
    }
  }


  /**
   * get coordinates from pointer event.
   * @param pe PointerEvent
   * @param width width of the screen
   * @param height height of the screen
   * @return Coordinates from the pointer event, or null if not applicable
   */
  private Coordinates getCoordinatesFromPointerEvent(PointerEvent pe, int width, int height) {
    switch (pe.action()) {
      case POINTER_UP -> {
        var location = pe.location();
        checkRange(0, location.x(), width);
        checkRange(0, location.y(), height);
        return new Coordinates(location.y(), location.x());
      }
      default -> {
        return null;
      }
    }
  }


  /**
   * Handles the keyboard event.
   * 
   * @param ke     the keyboard event
   * @param width  the width of the area
   * @param height the height of the area
   * @return the coordinates from the keyboard event, or null if not applicable
   */
  private boolean userWantsLeave(KeyboardEvent ke, int width, int height) {
    if (ke.action() == KeyboardEvent.Action.KEY_RELEASED) {
      if (ke.key() == KeyboardEvent.Key.ESCAPE ||
          ke.key() == KeyboardEvent.Key.Q) {
        return true;
      }
    }
    return false;
  }

  /**
   * Handles user's action, in this case only PointerEvent.
   * If user wants to leave he can leave by pressing ESCAPE or Q.
   * We ignore any other event.
   * @param context 
   * @param width
   * @param height
   * @return
   */
  public Coordinates getCoordinatesFromUser(ApplicationContext context, int width, int height) {
    Objects.requireNonNull(context);
    for (;;){
      var event = context.pollOrWaitEvent(10);
      switch (event) {
        case null -> { continue; /* do nothing */ }
        case PointerEvent pe -> {
          var coords = getCoordinatesFromPointerEvent(pe, width, height);
          if (coords != null) {
            return coords;
          }
        }
        case KeyboardEvent ke -> {
          if (userWantsLeave(ke, width, height)) {
            return null;
          }
        }
        default -> { /* do nothing */ }
      }
    }
  }



  /**
   * Handles the token change if needed.
   *
   * @param context   the application context for rendering and user events
   * @param fontSize  the size of the font used in the UI
   * @return true if the tokens were updated, false otherwise
   */
  public boolean handleTokenChange(ApplicationContext context, int fontSize) {
    Objects.requireNonNull(context, "context cannot be null");

    if (data.tokensMustBeUpdated()) {
      handleImmediateTokenUpdate(context);
      return true;
    }

    if (data.tokensCouldBeUpdated()) {
      return handlePotentialTokenUpdate(context, fontSize);
    }
    return false;
  }

  /**
   * Immediately updates tokens and re-renders the game board.
   * @param context the application context for rendering and user events
   */
  private void handleImmediateTokenUpdate(ApplicationContext context) {
    data.updateTokens();
    context.renderFrame(graphics -> showGameBoard(graphics, figureSize));
  }

  /**
  * Displays a "Click here to change" prompt, waits for user input,
  * and updates tokens if requested. Finally re-renders the game board.
  *
  * @return true if anything changed, false otherwise
  */
  private boolean handlePotentialTokenUpdate(ApplicationContext context, int fontSize) {
    promptTokenChange(context, fontSize);
    var userCoordinates = getCoordinatesFromUser(context, width, height);
    if (userCoordinates == null) {
      System.err.println("Coordinates are invalid (user pressed Q or canceled).");
      return false;
    }

    checkAndUpdateTokens(userCoordinates);
    reRenderGameBoard(context);
    return true;
  }

  /**
   * Renders the "Click here to change" prompt.
   * @param context 
   * @param fontSize
   */
  private void promptTokenChange(ApplicationContext context, int fontSize) {
    context.renderFrame(graphics -> {
        restartRectangleWithText(graphics, "Click here to change",
            figureSize / 2, figureSize / 2, fontSize,
            (int) (figureSize * 3.5), figureSize * 10, figureSize * 8, Color.BLACK);
    });
  }

  /**
   * Checks if the user clicked inside the "update tokens" rectangle and updates tokens if so.
   * @param userCoordinates coordinates of the user
   */
  private void checkAndUpdateTokens(Coordinates userCoordinates) {
    if (insideRectangle(userCoordinates.x(), userCoordinates.y(),
            (int) (figureSize / 5), 0, 
            (int) (figureSize * 3.3), 
            (int) (figureSize * 0.8))) {
        System.out.println("Tokens are now updated");
        data.updateTokens();
    }
  }

  /**
   * Re-renders the game board.
   * @param context the application context for rendering and user events
   */
  private void reRenderGameBoard(ApplicationContext context) {
    context.renderFrame(graphics -> {
        drawRectangle(graphics,
            (int) (figureSize / 6), 0,
            (int) (figureSize * 3.5), 
            (int) (figureSize * 0.9),
            Color.WHITE);

        showGameBoard(graphics, figureSize);
    });
  }



  /**
   * Shows the possible token placement.
   * 
   * @param graphics Graphics engine that will actually display the possible token placement.
   * @param token WildlifeType to be displayed.
   * @param tokenSize size of the token.
   */
  private void showPossibleTokenPlacement(Graphics2D graphics, WildlifeType token, int tokenSize) {
    var listOfCells = data.getCurrentPlayerCells();
    for (var cell : listOfCells) {
      if (cell.isOccupiedByTile() && cell.couldBePlaced(token)) {
        var x = cell.getCoordinates().x();
        var y = cell.getCoordinates().y();
        drawCircle(graphics,
            (int) ((x + width / 2 / figureSize) * figureSize + (figureSize / 8.5)),
            (int) ((y + height / 2 / figureSize) * figureSize + (figureSize / 8.5)),
            (int) (tokenSize / 3),
            Color.WHITE);
      }
    }
  }


  /**
   * Selects the game board index.
   * 
   * @param context the application context for rendering and user events
   * @param environmentSquareWidth width of the environment square.
   * @param tokenSize size of the token.
   * @return the index of the game board couple
   */
  public int selectGameBoardIndex(ApplicationContext context, int environmentSquareWidth, int tokenSize) {
    Objects.requireNonNull(context);
    while (true) {
        var coordinates = getCoordinatesFromUser(context, width, height);
        if (coordinates == null) {
            return -2; /* user wants to leave */
        }
        var choice = indexOfGameBoardCouple(coordinates.x(), coordinates.y(), environmentSquareWidth);
        if (choice != -1) {
            return choice;
        }
    }
}


  /**
   * Handles the user's choice of tile and token.
   * @param context the application context for rendering and user events
   * @param chosenTile chosen tile
   * @return the index of the game board couple
   */
  public boolean handleTilePlacement(ApplicationContext context, Tile chosenTile) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(chosenTile);
    /* player has to place tile correctly */
    do {
      var userCoordinates = getCoordinatesFromUser(context, width, height);
      if (userCoordinates == null) {
        System.err.println("Coordinates are invalid WHEN PLACING TILE"); /* for tests, to delete later */
        return false;
      }
      var chosenCoordinates = new Coordinates(
          (int) (userCoordinates.y() / figureSize - height / 2 / figureSize),
          (int) (userCoordinates.x() / figureSize - width / 2 / figureSize));

      if (data.placeTileIfPossible(chosenTile, chosenCoordinates)) {
        return true;
      }
    } while (true);
  }

  /**
   * Handles the user's choice of token placement.
   * 
   * @param context the application context for rendering and user events
   * @param chosenToken chosen token
   * @param tokenSize size of the token
   * @return true if the token was placed successfully, false otherwise
   */
  public boolean handleTokenPlacement(ApplicationContext context, WildlifeType chosenToken,int tokenSize) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(chosenToken);

    context.renderFrame(graphics -> {
      showPossibleTokenPlacement(graphics, chosenToken, tokenSize);
    });
    var userCoordinates = getCoordinatesFromUser(context, width, height);
    if (userCoordinates == null) {
      System.err.println("Coordinates are invalid WHEN PLACING TOKEN"); /* for tests, to delete later */
      return true;
    }
    var chosenCoordinates = new Coordinates(
        (int) ((userCoordinates.y() / figureSize) - height / 2 / figureSize),
        (int) ((userCoordinates.x() / figureSize) - width / 2 / figureSize));

    var tokenWasPlaced = data.placeAnimalIfPossible(chosenToken, chosenCoordinates);
    return tokenWasPlaced;
  }

  /**
   * Draws text on the screen.
   * @param graphics Graphics engine that will actually display the scoring card.
   * @param text text to be displayed.
   * @param x abscissa of the center of the text.
   * @param y ordinate of the center of the text.
   * @param fontSize size of the font.
   * @param color color of the text.
   */
  private void drawString(Graphics2D graphics, String text, int x, int y, int fontSize, Color color) {
    graphics.setColor(color);
    // https://stackoverflow.com/questions/18249592/how-to-change-font-size-in-drawstring-java
    graphics.setFont(new Font(ARIAL, Font.BOLD, fontSize));
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    graphics.drawString(text, x, y);
  }

  /**
   * checks if the coordinates are inside the rectangle.
   * 
   * @param x abscissa of the center of the rectangle.
   * @param y ordinate of the center of the rectangle.
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return true if the coordinates are inside the rectangle, false otherwise
   */
  private static boolean insideRectangle(int x, int y, int x1, int y1, int x2, int y2) {
    return x >= x1 && x <= x2 && 
           y >= y1 && y <= y2;
  }


  /**
   * Returns the index of the game board couple.
   * @param x abscissa of the center of the rectangle.
   * @param y ordinate of the center of the rectangle.
   * @param environmentSquareWidth width of the environment square.
   * @return the index of the game board couple
   */
  public int indexOfGameBoardCouple(int x, int y, int environmentSquareWidth) {
    for (var index = 0; index < 4; ++index) {
      int[] yOffsets = { figureSize, (int) (figureSize * 3.2f),
          (int) (figureSize * 5f), (int) (figureSize * 6.8f) };
        
        int[] heights = { (int) (figureSize * 2.2f), (int) (figureSize * 1.8f),
          (int) (figureSize * 1.8f), (int) (figureSize * 2.2f) };

      var x1 = figureSize / 3;
      var y1 = yOffsets[index];
      var x2 = x1 + (int) (environmentSquareWidth / 5);
      var y2 = y1 + heights[index];
      if (insideRectangle(x, y, x1, y1, x2, y2)) {
        return index;
      }
    }
    return -1;
  }

  /**
   * Draws the environment rectangle.
   * @param graphics Graphics engine that will actually display the scoring card.
   * @param environmentSquareWidth width of the environment square.
   * @param environmentSquareHeight height of the environment square.
   */
  public void restartEnvironmentRectangle(Graphics2D graphics, 
    int environmentSquareWidth, int environmentSquareHeight) {
    Objects.requireNonNull(graphics);
    drawRoundRectangle(graphics,
        width / 2 - (environmentSquareWidth / 2), // center of the screen - x
        height / 2 - (environmentSquareHeight / 2), // center of the screen - y
        environmentSquareWidth,
        environmentSquareHeight,
        environmentSquareWidth / 10,
        environmentSquareHeight / 10,
        Color.PINK);
  }

  /**
   * Draws the game board rectangle.
   * @param graphics Graphics engine that will actually display the scoring card.
   * @param environmentSquareHeight height of the environment square.
   */
  public void restartGameBoardRectangle(Graphics2D graphics, int environmentSquareHeight) {
    Objects.requireNonNull(graphics);
    drawRoundRectangle(graphics,
        (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35), // to think about coefficient
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.PINK);
  }

  /**
   * restart scoring card rectangle.
   * @param graphics
   * @param environmentSquareHeight
   */
  public void restartScoringCardRectangle(Graphics2D graphics, int environmentSquareHeight) {
    Objects.requireNonNull(graphics);
    drawRoundRectangle(graphics,
        width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35),
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.PINK);
  }

  /**
   * restart rectangle with text.
   * @param graphics Graphics engine that will actually display the scoring card.
   * @param text text to be displayed.
   * @param x abscissa of the center of the text.
   * @param y ordinate of the center of the text.
   * @param fontSize size of the font.
   * @param width_rect width of the rectangle.
   * @param environmentSquareWidth width of the environment square.
   * @param environmentSquareHeight height of the environment square.
   * @param color color of the text.
   */
  public void restartRectangleWithText(
      Graphics2D graphics, String text,
      int x, int y, int fontSize, int width_rect,
      int environmentSquareWidth, int environmentSquareHeight, Color color) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(text);
    Objects.requireNonNull(color);
    drawRoundRectangle(graphics,
        width_rect - (environmentSquareWidth / 3), // center of the screen - x
        0, // center of the screen - y
        environmentSquareWidth / 3,
        environmentSquareHeight / 10,
        environmentSquareWidth / 15,
        environmentSquareHeight / 10,
        Color.RED);

    drawString(graphics, text, x, y, fontSize, color);
  }


  /**
   * Throws exception if the value is not in the required range.
   *
   * @param min   Minimal acceptable value.
   * @param value Value being tested.
   * @param max   Maximal acceptable value.
   */
  private static void checkRange(double min, double value, double max) {
    if (value < min || value > max) {
      throw new IllegalArgumentException("Invalid coordinate: " + value);
    }
  }

  /**
   * Gets row or column number based on the given coordinate and the origin of the
   * display area for this coordinate.
   *
   * @param coord  Coordinate whose row / column we want to know.
   * @param origin Origin coordinate of the display area.
   * @return Row / column index that was pointed at by coord.
   */
  private int indexFromRealCoord(float coord, int origin) {
    return (int) ((coord - origin) / figureSize);
  }

  /**
   * Transforms a real y-coordinate into the index of the corresponding line.
   *
   * @param y a float y-coordinate
   * @return the index of the corresponding line.
   * @throws IllegalArgumentException if the float coordinate does not fit in the
   *                                  game board.
   */
  public int lineFromY(float y) {
    checkRange(yOrigin, y, y + width);
    return indexFromRealCoord(y, yOrigin);
  }

  /**
   * Transforms a real x-coordinate into the index of the corresponding column.
   *
   * @param x a float x-coordinate
   * @return the index of the corresponding column.
   * @throws IllegalArgumentException if the float coordinate does not fit in the
   *                                  game board.
   */
  public int columnFromX(float x) {
    checkRange(xOrigin, x, x + height);
    return indexFromRealCoord(x, xOrigin);
  }



  /**
   * Draws the game board from its data, using an existing
   * @param context {@code ApplicationContext} of the game.
   * @param data   GameData containing the game data.
   * @param view  GameView on which to draw.
   * @param text Text to display on the screen.
   * @param width width of the screen.
   * @param width_rect width of the rectangle.
   * @param figureSize size of the figure. 
   * @param environmentSquareWidth width of the environment square.
   * @param environmentSquareHeight height of the environment square.
   */
  public static void drawInformation(ApplicationContext context, CascadiaData data, CascadiaView view, String text,
      int width, int width_rect, int figureSize, int environmentSquareWidth, int environmentSquareHeight) {
      Objects.requireNonNull(context);
      Objects.requireNonNull(data);
      Objects.requireNonNull(view);
      Objects.requireNonNull(text);
      
      context.renderFrame(graphics -> {
      view.restartRectangleWithText(graphics, text,
      (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
      width_rect, width, environmentSquareWidth,
      environmentSquareHeight, Color.BLACK);
    });
  }

  /**
   * Draws the start for every player.
   * @param context {@code ApplicationContext} of the game.
   * @param data GameData containing the game data.
   * @param view GameView on which to draw.
   * @param width width of the screen.
   * @param height height of the screen.
   * @param width_rect width of the rectangle.
   * @param figureSize size of the figure.
   * @param environmentSquareWidth width of the environment square.
   * @param environmentSquareHeight height of the environment square.
   */
  public static void drawStart(ApplicationContext context, CascadiaData data, CascadiaView view,
      int width, int height, int width_rect, int figureSize, int environmentSquareWidth, int environmentSquareHeight) { 
    Objects.requireNonNull(context);
    Objects.requireNonNull(data);
    Objects.requireNonNull(view);
    context.renderFrame(graphics -> {
      view.drawRectangle(graphics, 0, 0, width, height, Color.WHITE); /* clear the screen */
      view.restartEnvironmentRectangle(graphics, environmentSquareWidth, environmentSquareHeight);
      view.restartGameBoardRectangle(graphics, environmentSquareHeight);
      view.restartScoringCardRectangle(graphics, environmentSquareHeight);
      view.drawEnvironment(graphics);
      view.showGameBoard(graphics, figureSize);
    });

    drawInformation(context, data, view, data.getCurrentPlayer().getName() + " Game Board", width, width_rect, figureSize,
        environmentSquareWidth, environmentSquareHeight);
  }

}
