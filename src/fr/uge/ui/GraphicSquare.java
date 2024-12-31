package fr.uge.ui;

import fr.uge.core.Player;
import fr.uge.core.GameBoard;
import fr.uge.environment.Coordinates;
import fr.uge.environment.WildlifeType;
import fr.uge.environment.TileType;
import fr.uge.environment.Tile;
import fr.uge.core.Game;
import fr.uge.core.TurnManager;
import fr.uge.util.Constants;

import java.io.IO;
import java.util.List;
import java.util.Objects;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints; // to think if we need it
// import java.awt.Rectangle;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.PointerEvent;
import java.awt.Font;
import java.awt.Graphics2D;

class Area {
  private Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, 0, 0);

  void draw(ApplicationContext context, float x, float y) {
    context.renderFrame(graphics -> {
      // hide the previous rectangle
      graphics.setColor(Color.ORANGE);
      graphics.fill(ellipse);

      // show a new ellipse at the position of the pointer
      graphics.setColor(Color.MAGENTA);
      ellipse = new Ellipse2D.Float(x - 20, y - 20, 40, 40);
      graphics.fill(ellipse);
    });
  }
}

class RectangleSquare {

  private final Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);

  void draw(ApplicationContext context, float x, float y, int size, Color color) {
    context.renderFrame(graphics -> {
      // hide the previous rectangle
      graphics.setColor(Color.WHITE);
      graphics.fill(rectangle);

      // show a new rectangle at the position of the pointer
      // MAGENTA (TEST)
      // graphics.setColor(Color.MAGENTA);

      // CYAN
      // graphics.setColor(Color.CYAN);

      // BLUE
      // graphics.setColor(Color.BLUE);

      // YELLOW
      // graphics.setColor(Color.YELLOW);

      // GREEN (FOREST)
      // graphics.setColor(Color.GREEN);

      // LIGHT GRAY (MOUNTAIN)
      // graphics.setColor(Color.LIGHT_GRAY);

      // WHITE (MOUNTAIN)
      graphics.setColor(color);

      rectangle.setRect(x - size / 2, y - size / 2, size, size);
      graphics.fill(rectangle);
    });
  }
}

// class HexagonalPolygon {

// private final int[] xPoints = new int[6];
// private final int[] yPoints = new int[6];

// void draw(ApplicationContext context, float x, float y) {
// context.renderFrame(graphics -> {
// // hide the previous rectangle
// graphics.setColor(Color.ORANGE);
// graphics.fillPolygon(xPoints, yPoints, 6);

// // show a new rectangle at the position of the pointer
// graphics.setColor(Color.MAGENTA);
// for (var i = 0; i < 6; i++) {
// xPoints[i] = (int) (x + 20 * Math.cos(i * Math.PI / 3));
// yPoints[i] = (int) (y + 20 * Math.sin(i * Math.PI / 3));
// }
// graphics.fillPolygon(xPoints, yPoints, 6);
// });
// }
// }

class HexagonalPointyTop {
  private final int[] xPoints = new int[6];
  private final int[] yPoints = new int[6];

  // private final Coordinates[] coordinates = new Coordinates[6];
  void draw(ApplicationContext context, float x, float y, int size) {
    context.renderFrame(graphics -> {
      // hide the previous hexagon
      // graphics.setColor(Color.ORANGE);
      graphics.fillPolygon(xPoints, yPoints, 6);

      // show a new hexagon at the position of the pointer
      graphics.setColor(Color.MAGENTA);
      for (var i = 0; i < 6; i++) {
        xPoints[i] = (int) (x + size * Math.cos(i * Math.PI / 3 + Math.PI / 6));
        yPoints[i] = (int) (y + size * Math.sin(i * Math.PI / 3 + Math.PI / 6));
      }
      graphics.fillPolygon(xPoints, yPoints, 6);
    });
  }
}

public class GraphicSquare {

  private static final Color COLOR_FOREST = Color.GREEN; // new Color(46, 111, 64); // 39, 66, 26
  private static final Color COLOR_WETLAND = new Color(166, 185, 111);
  private static final Color COLOR_MOUNTAIN = Color.GRAY; // new Color(105, 105, 105);
  private static final Color COLOR_RIVER = Color.BLUE;
  private static final Color COLOR_PRAIRIE = Color.ORANGE;

  // private int sizeOfTile;
  // private int sizeOfToken;
  // private int spaceBetweenTileAndToken;
  // private int spaceBetweenCouples;
  // private int sizeOfScoringCard;
  // private int sizeOfPlayerBoard; /* optional */

  private int heightShift = 0; /* that's count when user wants to move his screen into left / right */
  private int widthShift = 0; /* that's count when user wants to move his screen into up / down */

  private GraphicSquare() {
    throw new IllegalStateException("No instance of GraphicSquare");
  }

  private void makeCalculations(int width, int height) {
    var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
    var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */

    var coeffSizeToken = 0.05; /* to test */
    var coeffSizeScoringCard = 0.1; /* to test */
    var coeffSizePlayerBoard = 0.1; /* to test */

    // this.sizeOfTile = (int) (coeffSizeSquare * Math.min(width, height));
    // this.sizeOfToken = (int) (coeffSizeToken * Math.min(width, height));
    // this.spaceBetweenTileAndToken = 10; /* to determine later based on width and
    // height */
    // this.spaceBetweenCouples = 10; /* to determine later based on width and
    // height */
    // this.sizeOfScoringCard = 10; /* to determine later based on width and height
    // */
    // this.sizeOfPlayerBoard = 10; /* optional */
  }

  private static void drawSquare(ApplicationContext context, float x, float y, int size, Color color) {
    var rectangle = new Rectangle2D.Float(0, 0, 0, 0);

    context.renderFrame(graphics -> {
      // hide the previous rectangle
      // graphics.setColor(Color.WHITE);
      // graphics.fill(rectangle);

      // show a new rectangle at the position of the pointer
      graphics.setColor(color);
      rectangle.setRect(x - size / 2, y - size / 2, size, size);
      graphics.fill(rectangle);
    });
  }

  // to think if we need it
  // https://stackoverflow.com/questions/25975637/2-smooth-round-corners-of-rectangle-in-java-with-red-background
  private static void drawRectangle(
      ApplicationContext context,
      int x, int y,
      int width, int height,
      Color color) {

    var rectangle = new Rectangle2D.Float(x, y, width, height);
    // var rectangle = new RoundRectangle2D.Float(x, y, width, height, height / 10,
    // height / 10);
    // rectangle.setArcWidth(20);
    // rectangle.setArcHeight(20);
    context.renderFrame(graphics -> {
      graphics.setPaint(Color.ORANGE);
      // hide the previous rectangle
      // graphics.setColor(Color.WHITE);
      // graphics.fill(rectangle);

      // show a new rectangle at the position of the pointer
      graphics.setColor(color);
      // rectangle.setRect(x - width / 2, y - height / 2, width, height);
      graphics.fill(rectangle);

    });
  }

  private static void drawCircle(ApplicationContext context, float x, float y, int size, Color color) {
    var ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    context.renderFrame(graphics -> {
      // hide the previous rectangle
      graphics.setColor(Color.WHITE);
      graphics.fill(ellipse);

      // show a new rectangle at the position of the pointer
      graphics.setColor(color);
      ellipse.setFrame(x - size / 2, y - size / 2, size, size);
      graphics.fill(ellipse);
    });
  }

  public static void drawLine(ApplicationContext context, int x1, int y1, int x2, int y2, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      graphics.drawLine(x1, y1, x2, y2);
    });
  }

  // to think if we need it
  // https://stackoverflow.com/questions/25975637/2-smooth-round-corners-of-rectangle-in-java-with-red-background
  private static void drawRoundRectangle(ApplicationContext context,
      float x, float y,
      float width, float height,
      float arcw, float arch,
      Color color) {

    var qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // var rectangle = new Rectangle2D.Float(0, 0, 0, 0);
    var rectangle = new RoundRectangle2D.Float(x, y, width, height, arcw, arch);
    // rectangle.setArcWidth(20);
    // rectangle.setArcHeight(20);
    context.renderFrame(graphics -> {
      graphics.setPaint(Color.ORANGE);
      graphics.setRenderingHints(qualityHints);
      // hide the previous rectangle
      // graphics.setColor(Color.WHITE);
      // graphics.fill(rectangle);

      // show a new rectangle at the position of the pointer
      graphics.setColor(color);
      // rectangle.setRect(x - width / 2, y - height / 2, width, height);
      graphics.fill(rectangle);

    });
  }

  private static void checkRange(double min, double value, double max) {
    if (value < min || value > max) {
      throw new IllegalArgumentException("Invalid coordinate: " + value);
    }
  }

  // private static void drawAnimal(
  // ApplicationContext context, WildlifeType token, int tokenSize, int x, int y,
  // ImageLoader images) {

  // }
  // )

  private static void drawEnvironment(
      ApplicationContext context, Player player, int width, int height, int figureSize) {
    var listCells = player.getEnvironment().getCells();
    for (var cell : listCells) {
      if (cell.isOccupiedByTile()) {
        var habitatType = cell.getTile().firstHabitat();
        int x = (cell.getCoordinates().x() * figureSize) + width / 2;
        int y = (cell.getCoordinates().y() * figureSize) + height / 2;
        switch (habitatType) {
          case FOREST -> drawSquare(context, x, y, figureSize, COLOR_FOREST);
          case WETLAND -> drawSquare(context, x, y, figureSize, COLOR_WETLAND);
          case MOUNTAIN -> drawSquare(context, x, y, figureSize, COLOR_MOUNTAIN);
          case RIVER -> drawSquare(context, x, y, figureSize, COLOR_RIVER);
          case PRAIRIE -> drawSquare(context, x, y, figureSize, COLOR_PRAIRIE);
        }
        // drawSquare(context, cell.getCoordinates().x(), cell.getCoordinates().y(),
        // figureSize, Color.RED);
        // System.out.println(cell.toString());
      }
    }
  }

  // private void drawToken(ApplicationContext context, WildlifeType token, int
  // tokenSize, float x, float y){

  // }

  /**
   * Draw the game board
   * We still need to improve this code
   */
  private static void showGameBoard(
      ApplicationContext context, GameBoard board, int figureSize, /* int width, int height, */ int tokenSize /*
                                                                                                               * , int
                                                                                                               * margin
                                                                                                               */
  ) {
    final var RECTANGLE_X_OFFSET = 1.8f;
    final var ROW_MULTIPLIER = 1.8f;
    final var CIRCLE_X_OFFSET = 1.05f;

    var baseXOffset = (int) (figureSize / RECTANGLE_X_OFFSET); // common X offset for both shapes

    var listOfTiles = board.getCopyOfTiles();
    var listOfTokens = board.getCopyOfTokens();

    for (var i = 1; i <= 4; ++i) {
      var yPosition = (int) (figureSize * ROW_MULTIPLIER * i);

      // calculate circle position relative to rectangle
      var halfFigureSize = figureSize / 2f;
      var circleX = (int) (baseXOffset + (figureSize * CIRCLE_X_OFFSET) + halfFigureSize); // centered X for circle
      var circleY = (int) (yPosition + halfFigureSize); // centered Y for circle

      var tile = listOfTiles.get(i - 1);

      // draw Habitat tile (rectangle)
      // drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize,
      // COLOR_RIVER);
      switch (tile.firstHabitat()) {
        case FOREST -> drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize, COLOR_FOREST);
        case WETLAND -> drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize, COLOR_WETLAND);
        case MOUNTAIN -> drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize, COLOR_MOUNTAIN);
        case RIVER -> drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize, COLOR_RIVER);
        case PRAIRIE -> drawRectangle(context, baseXOffset, yPosition, figureSize, figureSize, COLOR_PRAIRIE);
      }
      var token = listOfTokens.get(i - 1); // BEAR, ELK, SALMON, HAWK, FOX
      // draw Wildlife token (circle)
      // drawCircle(context, circleX, circleY, tokenSize, Color.RED);
      switch (token) {
        case BEAR -> drawCircle(context, circleX, circleY, tokenSize, Color.RED);
        case ELK -> drawCircle(context, circleX, circleY, tokenSize, Color.BLUE);
        case SALMON -> drawCircle(context, circleX, circleY, tokenSize, Color.GREEN);
        case HAWK -> drawCircle(context, circleX, circleY, tokenSize, Color.YELLOW);
        case FOX -> drawCircle(context, circleX, circleY, tokenSize, Color.CYAN);
      }

      // do not delete this code for now ! it's for internal use and see where the
      // lines are
      if (i != 4) {
        drawLine(context,
            (int) (figureSize / 3),
            (int) (yPosition + figureSize * 1.4), // y (int) (figureSize * 1.8f * i);
            (int) (figureSize / 3 + figureSize * 2.35),
            (int) (yPosition + figureSize * 1.4),
            Color.BLACK);
      }

      /*
       * Previos code used for draw game board
       * // drawRectangle(context,
       * // (int) (figureSize / 1.8),
       * // (int) ((figureSize * 1.8 * i) + (figureSize * 1.8)),
       * // figureSize, figureSize, COLOR_RIVER);
       * 
       * // drawCircle(context,
       * // (int) (figureSize / 1.8 + (figureSize * 1.05)) + (figureSize / 2),
       * // (int) ((figureSize * 1.8 * i) + (figureSize * 1.8)) + (figureSize / 2),
       * // (int) tokenSize, Color.RED);
       */
    }
  }

  private static void showScoringCard(ApplicationContext context,
      int x, int y, int width, int height, int figureSize, boolean isFamilyScoringCard) {
    /*
     * drawRoundRectangle(context,
     * width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
     * figureSize, // height
     * (int) (figureSize * 2.35),
     * environmentSquareHeight,
     * figureSize,
     * environmentSquareHeight / 10,
     * Color.MAGENTA);
     */

    drawRectangle(context, x, y + (height / 4), // x, y
        (int) (width), // width
        (int) (height / 2), // height
        // figureSize, height / 10,
        Color.RED);

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////// 
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////// FROM TERMINAL ////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////// 
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static void showEnvironment(Player player) {

  }

  private static void showPossibleCoordinates(ApplicationContext context, Player player, int width, int height,
      int figureSize) {
    Objects.requireNonNull(player);
    var listOfCells = player.getEnvironment().getCells();
    for (var cell : listOfCells) {
      if (cell == null) {
        throw new IllegalArgumentException("cell is null in showPossibleCoordinates()");
      }
      if (!cell.isOccupiedByTile()) {
        var x = cell.getCoordinates().x() * figureSize + width / 2;
        var y = cell.getCoordinates().y() * figureSize + height / 2;
        drawSquare(context, x, y, figureSize, Color.BLACK);
      }
    }

  }

  private static void showPlayerEnvironmentAndGameBoard(
      ApplicationContext context, Player player, GameBoard board, int width, int height, int figureSize) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(board);
    // showEnvironment(player);
    drawEnvironment(context, player, width, height, figureSize);
    // showPossibleCoordinates(player);
    showPossibleCoordinates(context, player, width, height, figureSize);
    showGameBoard(context, board, figureSize, figureSize);
  }

  private static boolean handleTokenChange(
      ApplicationContext context, Game game, int figureSize) {
    Objects.requireNonNull(game);
    if (game.board().tokensNeedUpdate()) {
      game.board().updateTokens();
      // System.out.println("Tokens were updated, because one token had 4
      // occurences");
      showGameBoard(context, game.board(), figureSize, figureSize);
      return true;
    } else if (game.board().tokensCanBeUpdated()) {

      // restartRectangleWithText(context,
      // "Change Tokens", figureSize / 3, figureSize / 2, 15,
      // figureSize, figureSize, figureSize, figureSize, Color.BLACK);

      // to check coordinates from user
      var userCoordinates = getCoordinatesFromUser(context);
      if (userCoordinates == null) {
        System.err.println("Coordinates are invalid 1"); /* for tests, to delete later */
        return false;
      }
      if (insideRectangle(userCoordinates.x(), userCoordinates.y(), 0, 0, figureSize, figureSize)) {
        // if player wants to change tokens he must click on the rectangle "(Change
        // Tokens)"
        System.out.println("tokens are now updated");
        game.board().updateTokens();
      }

      showGameBoard(context, game.board(), figureSize, figureSize);
      return true;
    }
    return false;
  }

  private static void handleTurnChange(Game game) {
    Objects.requireNonNull(game);
    game.turnManager().changePlayer();
    game.turnManager().nextTurn();
    game.board().setDefaultTokensAreUpdated(); // that means, next person can change tokens (if needed)
  }

  private static int handleUserChoiceTileAndToken() {
    int choice;
    do {
      choice = Integer.parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile, Token)\n"));
    } while (!Constants.isValidChoice(choice));
    return choice;
  }

  private static Coordinates getCoordinatesFromUser(ApplicationContext context) {
    var screenInfo = context.getScreenInfo();
    var width = screenInfo.width();
    var height = screenInfo.height();


    do { 
      var event = context.pollOrWaitEvent(1000);

      switch (event) {
        case null -> {
          continue;
        }
        case PointerEvent e -> {
          var location = e.location();
          checkRange(0, location.x(), width);
          checkRange(0, location.y(), height);
          return new Coordinates(location.x(), location.y());
        }
        case KeyboardEvent ke -> {
          if (ke.key() == KeyboardEvent.Key.Q) {
            return null;
          }
        }
        default -> {
          continue;
        }
      }
    } while (true);
    // return null;
  }

  private static void showPossibleTokenPlacement(
      ApplicationContext context, Player player,
      WildlifeType token, int figureSize, int tokenSize) {
    Objects.requireNonNull(player, "player is null in showPossibleTokenPlacement()");
    Objects.requireNonNull(token, "token is null in showPossibleTokenPlacement()");

    var listOfCells = player.getEnvironment().getCells();

    for (var cell : listOfCells) {
      if (cell.isOccupiedByTile()) {
        if (cell.canBePlaced(token)) {
          var x = cell.getCoordinates().x();
          var y = cell.getCoordinates().y();
          drawCircle(context,
              x * figureSize + (figureSize / 2),
              y * figureSize + (figureSize / 2),
              tokenSize,
              Color.ORANGE);
        }
      }
    }

  }


  private static boolean handleTokenPlacement(
    ApplicationContext context, Player player,
    WildlifeType chosedToken, int width, int height,
    int figureSize, int tokenSize) {

    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedToken);

    /* chosed token from `choice` */
    // System.out.println("Now you need to place the wildlife token: " +
    // chosedToken.toString());
    showPossibleTokenPlacement(context, player, chosedToken, figureSize, tokenSize);

    var userCoordinates = getCoordinatesFromUser(context);
    if (userCoordinates == null) {
      System.err.println("Coordinates are invalid WHEN PLACING TOKEN"); /* for tests, to delete later */
      return false;
    }
    System.err.println("Coordinates (TokenPlacement) : (" + userCoordinates.x() + "," + userCoordinates.y() + ")");
    var coordinatesForMap = new Coordinates(
        (int) ((userCoordinates.y() - width / 2) / figureSize),
        (int) ((userCoordinates.x() - height / 2) / figureSize));
    var currCell = player.getEnvironment().getCell(coordinatesForMap);
    var tokenWasPlaced = player.getEnvironment().placeAnimal(currCell, chosedToken);
    if (!tokenWasPlaced) {
      System.err.println("Token wasn't placed"); /* for tests, to delete later */
    }
    return tokenWasPlaced;
  }

  private static boolean handleTilePlacement(
      ApplicationContext context, Player player,
      Tile chosedTile, int width, int height, int figureSize) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedTile);
    var possibleCoordinates = player.getEnvironment().getPossibleCells();

    /* player has to place tile correctly */
    do {
      var userCoordinates = getCoordinatesFromUser(context);
      if (userCoordinates == null) {
        System.err.println("Coordinates are invalid WHEN PLACING TILE"); /* for tests, to delete later */
        // return false;
        context.dispose();
        throw new IllegalStateException("You have to click on correct coordinates!\n");
      }
      var coordinatesForMap = new Coordinates(

        (int) ((userCoordinates.y() - (width / 2)) / figureSize), // (cell.getCoordinates().x() * figureSize) + (width / 2)
        (int) ((userCoordinates.x() - (height / 2)) / figureSize));
        
      System.err.println("Coordinates (TilePlacement) : (" + coordinatesForMap.x() + "," + coordinatesForMap.y() + ")");
      System.err.println("Possible coordinates: " + possibleCoordinates.toString());

      if (possibleCoordinates.stream().anyMatch(coordinates -> coordinates.equals(coordinatesForMap))) {
        var currCell = player.getEnvironment().getCell(coordinatesForMap);
        if (player.getEnvironment().placeTile(currCell, chosedTile)) {
          // System.out.println("Tile was placed successfully");
          return true;
        }
      }
    } while (true);
  }

  // private static void gameLoopVersionSquare(
  // ApplicationContext context, Game game, int width, int height, int figureSize,
  // int tokenSize) {
  // Objects.requireNonNull(game);
  // while (!game.turnManager().isGameEnd()) {
  // drawRectangle(context, 0, 0, width, height, Color.WHITE); /* clear the screen
  // */
  // var currIndex = game.turnManager().getCurrentPlayerIndex();
  // var currentPlayer = game.getPlayerByIndex(currIndex);
  // showPlayerEnvironmentAndGameBoard(context, currentPlayer, game.board(),
  // width, height, figureSize);

  // // if (true){
  // // IO.readln("test");
  // // context.dispose();
  // // throw new IllegalStateException("You have to click on correct
  // coordinates!\n");
  // // }

  // // handleTokenChange(context, game, figureSize); /* if we need to update
  // tokens */

  // int choice = handleUserChoiceTileAndToken();
  // var chosedTile = game.board().getTile(choice - 1);
  // var chosedToken = game.board().getToken(choice - 1);

  // handleTilePlacement(context, currentPlayer, chosedTile, figureSize);
  // handleTokenPlacement(context, currentPlayer, chosedToken, figureSize,
  // tokenSize);

  // handleTurnChange(game);
  // }

  // }

  // public void playGraphicSquare() {
  // // System.out.println("Welcome to the Cascadia game (Graphic version)!");
  // // System.out.println("We have two players, please introduce yourselves.\n");
  // var firstPlayerName = "Alice"; // to be changed later
  // var secondPlayerName = "Bob"; // to be changed later
  // var familyOrIntermediate = 1; // to be changed later

  // var player1 = new Player(firstPlayerName, Constants.VERSION_SQUARE);
  // var player2 = new Player(secondPlayerName, Constants.VERSION_SQUARE);

  // var listOfPlayers = List.of(player1, player2);
  // var board = new GameBoard(Constants.NB_PLAYERS_SQUARE,
  // Constants.VERSION_SQUARE);
  // var turnManager = new TurnManager(listOfPlayers.size(),
  // Constants.VERSION_SQUARE);
  // Game game = new Game(board, turnManager, listOfPlayers,
  // Constants.VERSION_SQUARE);
  // gameLoopVersionSquare(game);
  // // calculateAndShowScore(game, familyOrIntermediate);
  // }

  private static void drawString(ApplicationContext context, String text, int x, int y, int fontSize, Color color) {
    context.renderFrame(graphics -> {
      graphics.setColor(color);
      // https://stackoverflow.com/questions/18249592/how-to-change-font-size-in-drawstring-java
      // graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
      graphics.setFont(new Font("Arial", Font.BOLD, fontSize));
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      graphics.drawString(text, x, y);
    });
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////// 
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////// FROM TERMINAL ////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////// 
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // private static void memoryGame(ApplicationContext context) {
  // var screenInfo = context.getScreenInfo();
  // var width = screenInfo.width();
  // var height = screenInfo.height();
  // var margin = 50;

  // var images = new ImageLoader("data", "blank.png",
  // "lego1.png", "lego2.png", "lego3.png", "lego4.png",
  // "lego5.png", "lego6.png", "lego7.png", "lego8.png");

  // var data = new SimpleGameData(4, 4);
  // var view = SimpleGameView.initGameGraphics(
  // margin, margin,
  // (int) Math.min(width, height) - 2 * margin,
  // data, images);
  // SimpleGameView.draw(context, data, view);
  // while (true) {
  // if (!gameLoop(context, data, view)) {
  // System.out.println("Thank you for quitting!");
  // context.dispose();
  // return;
  // }
  // }
  // }

  private static String askName(ApplicationContext context) {
    var name = IO.readln("What is your name?");
    context.renderFrame(graphics -> {
      graphics.setColor(Color.WHITE);
      graphics.fill(new Rectangle2D.Float(0, 0, 800, 600));
    });
    return name;
  }

  private static boolean insideRectangle(int x, int y, int x1, int y1, int x2, int y2) {
    return x >= x1 && x <= x2 && y >= y1 && y <= y2;
  }

  // private static int insideGameBoardSquare(int x, int y, int x1, int y1, int
  // x2, int y2, int figureSize) {
  // for (var i = 1; i <= 4; ++i){
  // var x1_board = (int) (figureSize / 3);
  // var y1_board = figureSize * i;
  // var x2_board = x1_board + (int) (figureSize * 2.35);
  // var y2_board = y1_board + figureSize;
  // if (x >= x1_board && x <= x2_board && y >= y1_board && y <= y2_board){
  // return i;
  // }
  // }
  // return -1;
  // }

  private static int indexOfGameBoardCouple(
      ApplicationContext context, int x, int y, int figureSize, int environmentSquareWidth) {
    for (var index = 0; index < 4; ++index) {
      Color[] colors = { Color.GREEN, Color.BLUE, Color.YELLOW, Color.GREEN };
      int[] yOffsets = { figureSize,
          (int) (figureSize * 3.2f),
          (int) (figureSize * 5f),
          (int) (figureSize * 6.8f) };

      int[] heights = { (int) (figureSize * 2.2f),
          (int) (figureSize * 1.8f),
          (int) (figureSize * 1.8f),
          (int) (figureSize * 2.2f) };

      // to see their hitboxes
      // drawRectangle(context, figureSize / 3, yOffsets[index], (int)
      // (environmentSquareWidth / 5), heights[index], colors[index]);

      var x1 = figureSize / 3;
      var y1 = yOffsets[index];
      var x2 = x1 + (int) (environmentSquareWidth / 5);
      var y2 = y1 + heights[index];
      if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
        return index;
      }
    }
    return -1;
  }

  // private static void drawSquares(ApplicationContext context, int width, int
  // height, int figureSize){
  // // draw squares on the screen on every possible position
  // Color[] colors = {
  // Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
  // Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK
  // };
  // for (var i = 0; i <= width / figureSize; ++i) {
  // for (var j = 0; j <= height / figureSize; ++j) {
  // drawSquare(context,
  // i * figureSize + (figureSize / 2),
  // j * figureSize + (figureSize / 2),
  // figureSize,
  // colors[(i + j) % colors.length]);

  // drawString(context, i + ", " + j,
  // i * figureSize + (figureSize / 3),
  // j * figureSize + (figureSize / 2),
  // 15, Color.BLACK);
  // }
  // }
  // }

  private static void drawSquares(ApplicationContext context, int width, int height, int figureSize) {
    // draw squares on the screen on every possible position
    Color[] colors = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
        Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK
    };

    var centerX = width / 2;
    var centerY = height / 2;

    for (var i = 0; i <= width / figureSize; ++i) {
      for (var j = 0; j <= height / figureSize; ++j) {
        drawSquare(context,
            i * figureSize + (figureSize / 2),
            j * figureSize + (figureSize / 2),
            figureSize,
            colors[(i + j) % colors.length]);

        drawString(context, i - (centerX / figureSize) + ", " + (j - (centerY / figureSize)),
            i * figureSize + (figureSize / 3),
            j * figureSize + (figureSize / 2),
            15, Color.BLACK);
      }
    }
  }

  private void drawImage(
      Graphics2D graphics, BufferedImage image,
      float x, float y, float dimX, float dimY) {
    var width = image.getWidth();
    var height = image.getHeight();
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(
        scale, 0, 0, scale, x + (dimX - scale * width) / 2,
        y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }

  private static void restartEnvironmentRectangle(
      ApplicationContext context, int width, int height, int environmentSquareWidth, int environmentSquareHeight) {
    drawRoundRectangle(context,
        width / 2 - (environmentSquareWidth / 2), // center of the screen - x
        height / 2 - (environmentSquareHeight / 2), // center of the screen - y
        environmentSquareWidth,
        environmentSquareHeight,
        environmentSquareWidth / 10,
        environmentSquareHeight / 10,
        Color.PINK);
  }

  private static void restartGameBoardRectangle(
      ApplicationContext context, int figureSize, int environmentSquareHeight) {
    drawRoundRectangle(context,
        (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35), // to think about coefficient
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.CYAN);
  }

  private static void restartScoringCardRectangle(
      ApplicationContext context, int width, int figureSize, int environmentSquareHeight) {
    drawRoundRectangle(context,
        width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35),
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.MAGENTA);
  }

  private static void restartRectangleWithText(
      ApplicationContext context,
      String text, int x, int y, int fontSize,
      int width,
      int environmentSquareWidth, int environmentSquareHeight, Color color) {
    drawRoundRectangle(context,
        width - (environmentSquareWidth / 3), // center of the screen - x
        0, // center of the screen - y
        environmentSquareWidth / 3,
        environmentSquareHeight / 10,
        environmentSquareWidth / 15,
        environmentSquareHeight / 10,
        Color.RED);

    drawString(context, text, x, y, fontSize, color);
  }

  // private static void restartAnimalsOnTiles(
  // ApplicationContext context, int width, int height, int figureSize) {
  // var x1 = width / 2 - (figureSize * 2);
  // var y1 = height / 2 - (figureSize * 2);
  // var x2 = x1 + (figureSize * 4);
  // var y2 = y1 + (figureSize * 4);

  // drawRoundRectangle(context, x1, y1, x2, y2, figureSize / 10, figureSize / 10,
  // Color.PINK);
  // }

  private static void cascadiaSquareGame(ApplicationContext context) {
    var counter = 0;
    var screenInfo = context.getScreenInfo();
    var width = screenInfo.width();
    var height = screenInfo.height();

    // makeCalculations(width, height);
    var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
    var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */
    var coeffSizeToken = 0.08; /* to test */

    var coeffFontSizeInstructions = 0.01328125;

    var figureSize = (int) (coeffSizeSquare * Math.min(width, height));
    var tokenSize = (int) (coeffSizeToken * Math.min(width, height));

    var environmentSquareWidth = width - (figureSize * 6);
    var environmentSquareHeight = height - (figureSize * 2);

    System.out.println("size of the screen (" + width + " x " + height + ")");

    var images = new ImageLoader("img/tokens",
        "bear.png", "elk.png", "hawk.png", "fox.png", "salmon.png");

    var firstPlayerName = "Alice"; // to be changed later
    var secondPlayerName = "Bob"; // to be changed later
    var familyOrIntermediate = 1; // to be changed later

    var player1 = new Player(firstPlayerName, Constants.VERSION_SQUARE);
    var player2 = new Player(secondPlayerName, Constants.VERSION_SQUARE);

    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, Constants.VERSION_SQUARE);
    var turnManager = new TurnManager(listOfPlayers.size(), Constants.VERSION_SQUARE);
    Game game = new Game(board, turnManager, listOfPlayers, Constants.VERSION_SQUARE);
    // gameLoopVersionSquare(context, game, width, height, figureSize, tokenSize);
    // calculateAndShowScore(game, familyOrIntermediate);

    // not really usefull, can be removed later
    // context.renderFrame(graphics -> {
    // graphics.setColor(Color.WHITE);
    // graphics.fill(new Rectangle2D.Float(0, 0, width, height));
    // });

    drawRectangle(context, 0, 0, width, height, Color.WHITE);

    // drawSquares(context, width, height, figureSize);

    /*
     * // ask players for their names
     * drawRoundRectangle(context,
     * width / 2 - (environmentSquareWidth / 4), // center of the screen - x
     * height / 2 - (environmentSquareHeight / 4), // center of the screen - y
     * environmentSquareWidth / 2,
     * environmentSquareHeight / 2,
     * environmentSquareWidth / 15,
     * environmentSquareHeight / 10,
     * Color.RED);
     * drawString(context, "Welcome to the Cascadia game!", width / 2 - 150, height
     * / 2, 20, Color.WHITE);
     * 
     * drawRoundRectangle(context,
     * width / 2 + (environmentSquareWidth / 4), // center of the screen - x
     * height / 2 - (environmentSquareHeight / 4), // center of the screen - y
     * environmentSquareWidth / 4,
     * environmentSquareHeight / 2,
     * environmentSquareWidth / 15,
     * environmentSquareHeight / 10,
     * Color.MAGENTA);
     * drawString(context, "Validate", (int) (width / 2 + (environmentSquareWidth /
     * 3)), height / 2, 20, Color.WHITE);
     */

    var x1 = width / 2 + (environmentSquareWidth / 4);
    var y1 = height / 2 - (environmentSquareHeight / 4);
    var x2 = x1 + environmentSquareWidth / 4;
    var y2 = y1 + environmentSquareHeight / 2;

    // rectangle for player's environment
    // drawRoundRectangle(context,
    // width / 2 - (environmentSquareWidth / 2), // center of the screen - x
    // height / 2 - (environmentSquareHeight / 2), // center of the screen - y
    // environmentSquareWidth,
    // environmentSquareHeight,
    // environmentSquareWidth / 10,
    // environmentSquareHeight / 10,
    // Color.PINK);

    // game board
    // drawRoundRectangle(context,
    // (int) (figureSize / 3), // width
    // figureSize, // height
    // (int) (figureSize * 2.35), // to think about coefficient
    // environmentSquareHeight,
    // figureSize,
    // environmentSquareHeight / 10,
    // Color.ORANGE);

    // scoring cards:
    // drawRoundRectangle(context,
    // width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
    // figureSize, // height
    // (int) (figureSize * 2.35),
    // environmentSquareHeight,
    // figureSize,
    // environmentSquareHeight / 10,
    // Color.MAGENTA);

    showGameBoard(context, game.board(), figureSize, tokenSize);

    var x1_board = (int) (figureSize / 3);
    var y1_board = figureSize;
    var x2_board = x1_board + (int) (figureSize * 2.35);
    var y2_board = y1_board + environmentSquareHeight;

    // showScoringCard(context,
    // width - (int) (figureSize * 2.35) - (int) (figureSize / 3), figureSize, // x,
    // y
    // (int) (figureSize * 2.35), environmentSquareHeight, // width, height
    // figureSize, true
    // ); // figureSize, isFamilyScoringCard

    // var countHeight = height / figureSize;
    // var countWidth = width / figureSize;

    // drawSquare(context, width / 2, height / 2, figureSize, color_forest);
    // drawSquare(context, width / 2 + figureSize, height / 2, figureSize,
    // color_wetland);
    // drawSquare(context, width / 2 + figureSize * 2, height / 2, figureSize,
    // color_mountain);
    // drawSquare(context, width / 2 + figureSize * 3, height / 2, figureSize,
    // color_river);
    // drawSquare(context, width / 2 + figureSize * 4, height / 2, figureSize,
    // color_prairie);

    // drawSquare(context, width / 2, height / 2, figureSize, Color.RED);
    // drawSquare(context, width / 2, height / 2 + figureSize, figureSize,
    // Color.BLUE);
    // drawSquare(context, width / 2 + figureSize, height / 2 + figureSize,
    // figureSize, Color.GREEN);

    // var x1 = width / 2 - (environmentSquareWidth / 2);
    // var y1 = height / 2 - (environmentSquareHeight / 2);
    // var x2 = x1 + environmentSquareWidth;
    // var y2 = y1 + environmentSquareHeight;

    var area = new RectangleSquare();
    // var area = new HexagonalPointyTop();

    // // first couple
    // drawRectangle(context,
    // figureSize / 3,
    // (int)(figureSize),
    // (int) (environmentSquareWidth / 5),
    // (figureSize * 1) + (int)(figureSize * 1.2f), // (int)
    // (environmentSquareHeight / 3),
    // Color.GREEN);

    // // second couple
    // drawRectangle(context,
    // figureSize / 3,
    // (int) (figureSize * 1) + (int)(figureSize * 2.2),
    // (int) (environmentSquareWidth / 5),
    // (figureSize * 1) + (int)(figureSize * 0.8), // (int) (environmentSquareHeight
    // / 3),
    // Color.BLUE);

    // // third couple
    // drawRectangle(context,
    // figureSize / 3,
    // (int) (figureSize * 1.4f * 2) + (int)(figureSize * 2.2),
    // (int) (environmentSquareWidth / 5),
    // (figureSize * 1) + (int)(figureSize * 0.9), // (int) (environmentSquareHeight
    // / 3),
    // Color.YELLOW);

    // // fourth couple
    // drawRectangle(context,
    // figureSize / 3,
    // (int) (figureSize * 1.55f * 3) + (int)(figureSize * 2.2),
    // (int) (environmentSquareWidth / 5),
    // (figureSize * 1) + (int)(figureSize * 1.2), // (int) (environmentSquareHeight
    // / 3),
    // Color.GREEN);

    // gameLoopVersionSquare(context, game, width, height, figureSize, tokenSize);

    // while (!game.turnManager().isGameEnd()) {
    // var currIndex = game.turnManager().getCurrentPlayerIndex();
    // var currentPlayer = game.getPlayerByIndex(currIndex);
    // showPlayerEnvironmentAndGameBoard(context, currentPlayer, game.board(),
    // width, height, figureSize);

    // handleTokenChange(context, game, figureSize); /* if we need to update tokens
    // */

    // int choice = handleUserChoiceTileAndToken();
    // var chosedTile = game.board().getTile(choice - 1);
    // var chosedToken = game.board().getToken(choice - 1);

    // handleTilePlacement(context, currentPlayer, chosedTile, figureSize);
    // handleTokenPlacement(context, currentPlayer, chosedToken, figureSize,
    // tokenSize);

    // handleTurnChange(game);
    // }

    while (!game.turnManager().isGameEnd()) {
      // drawRectangle(context, 0, 0, width, height, Color.WHITE); /* clear the screen
      // */
      restartEnvironmentRectangle(context, width, height, environmentSquareWidth, environmentSquareHeight);
      restartGameBoardRectangle(context, figureSize, environmentSquareHeight);
      restartScoringCardRectangle(context, width, figureSize, environmentSquareHeight);
      restartRectangleWithText(context,
          "Choose from Game Board", (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
          (int) (width * coeffFontSizeInstructions),
          width, environmentSquareWidth, environmentSquareHeight, Color.BLACK);

      var currIndex = game.turnManager().getCurrentPlayerIndex();
      var currentPlayer = game.getPlayerByIndex(currIndex);
      showPlayerEnvironmentAndGameBoard(context, currentPlayer, game.board(), width, height, figureSize);

      // System.err.println("before handleTokenChange");
      /* if we need to update tokens */
      if (handleTokenChange(context, game, figureSize)) {
        System.err.println("Changing Tokens");
        restartRectangleWithText(context,
            "Change Tokens", (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
            (int) (width * coeffFontSizeInstructions),
            width, environmentSquareWidth, environmentSquareHeight, Color.BLACK);
      }
      // System.err.println("after handleTokenChange");

      // while (true) {

      Coordinates coordinates = null;
      System.err.println("before choosing tile and token");
      int choice = 0;
      do {

        coordinates = getCoordinatesFromUser(context);
        if (coordinates == null){
          System.err.println("Coordinates are invalid in game board choice"); /* for tests, to delete later */

          context.dispose();
          return;
        }
        // handleUserChoiceTileAndToken();
        choice = indexOfGameBoardCouple(context, coordinates.x(), coordinates.y(), figureSize, environmentSquareWidth);
      } while (choice == -1
      /*!insideRectangle(coordinates.x(), coordinates.y(), x1_board, y1_board, x2_board, y2_board)*/);
      System.out.println("You are inside game board rectangle");
      System.out.println("You choosed: " + choice);
      System.err.println("after choosing tile and token");

      var chosedTile = game.board().getTile(choice);
      var chosedToken = game.board().getToken(choice);
      System.err.println("tile: " + chosedTile.toString() + " token: " + chosedToken.toString());

      boolean flag;
      do {
        flag = handleTilePlacement(context, currentPlayer, chosedTile, width, height, figureSize);
      } while (!flag);

      System.err.println("Tile was placed successfully");
      // refresh the screen
      showPlayerEnvironmentAndGameBoard(context, currentPlayer, game.board(), width, height, figureSize);

      handleTokenPlacement(context, currentPlayer, chosedToken, width, height, figureSize, tokenSize);

      handleTurnChange(game);
      System.err.println("Next player");
      // var event = context.pollOrWaitEvent(1000);
      // switch (event) {
      // case null -> {
      // continue;
      // }
      // case PointerEvent e -> {
      // var location = e.location();
      // checkRange(0, location.x(), width);
      // checkRange(0, location.y(), height);

      // area.draw(context, location.x(), location.y(), figureSize / 10, Color.BLACK);
      // // if (insideRectangle(location.x(), location.y(), x1, y1, x2, y2)) {
      // // System.out.println("You are inside the validate name rectangle");
      // // context.dispose();
      // // return;
      // // }
      // // System.out.println("You clicked on: (" +
      // // (location.x() / figureSize - (width / 2 / figureSize)) + ", " +
      // // (location.y() / figureSize - (height / 2 / figureSize)) + ")");
      // // context.dispose();
      // // return;
      // // int choice = 0;
      // // do {
      // // // handleUserChoiceTileAndToken();
      // // choice = indexOfGameBoardCouple(context, location.x(), location.y(),
      // figureSize, environmentSquareWidth);
      // // } while (!insideRectangle(location.x(), location.y(), x1_board, y1_board,
      // x2_board, y2_board));
      // System.out.println("You are inside game board rectangle");
      // System.out.println("You choosed: " + choice);

      // // var chosedTile = game.board().getTile(choice);
      // // var chosedToken = game.board().getToken(choice);

      // // handleTilePlacement(context, currentPlayer, chosedTile, figureSize);
      // // handleTokenPlacement(context, currentPlayer, chosedToken, figureSize,
      // tokenSize);

      // // handleTurnChange(game);
      // break;
      // }
      // case KeyboardEvent ke -> {
      // if (ke.key() == KeyboardEvent.Key.Q) {
      // context.dispose();
      // return;
      // }
      // switch (ke.action()) { // KeyboardEvent.Key.Q
      // case Action.KEY_RELEASED -> {
      // counter++;
      // // to show directly the key pressed
      // // drawString(context, "" + ke.key(), width / 2 - 200 + counter * 11, height
      // / 2 + 100, 14, Color.WHITE);
      // }
      // case Action.KEY_PRESSED -> {
      // System.out.println(ke.key());
      // }
      // default -> {}
      // }
      // }
      // }
      // }

    }

  }

  public static void main(String[] args) {
    Application.run(Color.WHITE, GraphicSquare::cascadiaSquareGame);
  }

}
