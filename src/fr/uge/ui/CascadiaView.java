package fr.uge.ui;


import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IO;

import java.util.List;
import java.util.Objects;
import java.awt.RenderingHints; // to think if we need it
import java.awt.Graphics2D;
import java.awt.Font;

// import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
// import com.github.forax.zen.KeyboardEvent.Action;
// import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.core.Game;
import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.environment.Cell;
import fr.uge.environment.Coordinates;
import fr.uge.environment.Tile;
import fr.uge.environment.TileType;
import fr.uge.environment.WildlifeType;
// import fr.uge.util.Constants;
import fr.uge.util.Constants;

// final class HexagonalPointyTop {
// private final int[] xPoints = new int[6];
// private final int[] yPoints = new int[6];

// // private final Coordinates[] coordinates = new Coordinates[6];
// void draw(ApplicationContext context, float x, float y, int size) {
// context.renderFrame(graphics -> {
// // hide the previous hexagon
// // graphics.setColor(Color.ORANGE);
// graphics.fillPolygon(xPoints, yPoints, 6);

// // show a new hexagon at the position of the pointer
// graphics.setColor(Color.RED);
// for (var i = 0; i < 6; i++) {
// xPoints[i] = (int) (x + size * Math.cos(i * Math.PI / 3 + Math.PI / 6));
// yPoints[i] = (int) (y + size * Math.sin(i * Math.PI / 3 + Math.PI / 6));
// }
// graphics.fillPolygon(xPoints, yPoints, 6);
// });
// }
// }

/**
 * The CascadiaView class deals with the display of the game the screen, and
 * with the interpretation of which zones were clicked on by the user.
 *
 * @author vincent
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

  /////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////
  //////////////////::::::: code added from GraphicSquare.java :::::://////////////////
  /////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////

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
    // hide the previous rectangle
    // graphics.setColor(Color.WHITE);
    // graphics.fill(rectangle);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    rectangle.setRect(x - size / 2, y - size / 2, size, size);
    graphics.fill(rectangle);
  }

  public void drawRectangle(Graphics2D graphics, float x, float y, int width, int height,
      Color color) {

    var rectangle = new Rectangle2D.Float(x, y, width, height);
    // var rectangle = new RoundRectangle2D.Float(x, y, width, height, height / 10,
    // height / 10);
    // rectangle.setArcWidth(20);
    // rectangle.setArcHeight(20);
    graphics.setPaint(Color.ORANGE);
    // hide the previous rectangle
    // graphics.setColor(Color.WHITE);
    // graphics.fill(rectangle);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    // rectangle.setRect(x - width / 2, y - height / 2, width, height);
    graphics.fill(rectangle);

  }

  private void drawCircle(Graphics2D graphics, float x, float y, int size, Color color) {
    var ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // hide the previous rectangle
    graphics.setColor(Color.WHITE);
    graphics.fill(ellipse);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    ellipse.setFrame(x - size / 2, y - size / 2, size, size);
    graphics.fill(ellipse);

  }

  public void drawLine(Graphics2D graphics, int x1, int y1, int x2, int y2, Color color) {
    graphics.setColor(color);
    graphics.drawLine(x1, y1, x2, y2);
    
  }

  private void drawRoundRectangle(Graphics2D graphics, float x, float y, float width,
      float height, float arcw, float arch, Color color) {

    var qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    // var rectangle = new Rectangle2D.Float(0, 0, 0, 0);
    var rectangle = new RoundRectangle2D.Float(x, y, width, height, arcw, arch);
    // rectangle.setArcWidth(20);
    // rectangle.setArcHeight(20);

    graphics.setPaint(Color.ORANGE);
    graphics.setRenderingHints(qualityHints);
    // hide the previous rectangle
    // graphics.setColor(Color.WHITE);
    // graphics.fill(rectangle);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    // rectangle.setRect(x - width / 2, y - height / 2, width, height);
    graphics.fill(rectangle);

  }

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
   * Draws one animal image using the provided ImageLoader.
   */
  private void drawAnimal(Graphics2D graphics, ImageLoader images, WildlifeType animal,
      int x, int y, int size) {
    // If you prefer a switch block, you can still switch on 'animal',
    // but using 'animal.ordinal()' directly is usually enough.
    drawImage(graphics, images.image(animal.ordinal()), x, y, size, size);
  }

  /**
   * Draws the habitat square with the specified habitat type.
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
   * Draws multiple animals from the tile.
   */
  private void drawMultipleAnimals(Graphics2D graphics, ImageLoader images, Cell cell, int x,
      int y, int figureSize) {
    int placement = 0;
    for (var animal :
    cell.getTile().animals()) {
      final int scaledTokenSize = (int) (figureSize / 2.0);
      final int xAnimal = (int) ((x - (figureSize / 2.0) + figureSize / 20.0)
          + placement * (figureSize / 2.5));
      final int yAnimal = (int) (y - (figureSize / 2.0) + figureSize / 4.0);
      placement++;

      drawAnimal(graphics, images, animal, xAnimal, yAnimal, scaledTokenSize);
    }
  }

  /**
   * Draws any animals found on the given cell.
   */
  private void drawAnimalsOnCell(Graphics2D graphics, ImageLoader images, Cell cell, int x,
      int y, int figureSize) {
    if (cell.getAnimal() == null) {
      // Multiple animals from the tile
      drawMultipleAnimals(graphics, images, cell, x, y, figureSize);
    } else {
      // Single animal
      drawSingleAnimal(graphics, images, cell.getAnimal(), x, y, figureSize);
    }
  }

  /**
   * Draws a single animal if the cell has exactly one animal.
   */
  private void drawSingleAnimal(Graphics2D graphics, ImageLoader images, WildlifeType animal,
      int x, int y, int figureSize) {
    int scaledTokenSize = (int) (figureSize / 1.5);
    int xAnimal = x - figureSize / 3;
    int yAnimal = y - figureSize / 3;

    drawAnimal(graphics, images, animal, xAnimal, yAnimal, scaledTokenSize);
  }

  public void drawEnvironment(Graphics2D graphics) {
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
      drawAnimalsOnCell(graphics, loader, cell, x, y, figureSize);
    }
  }

  // private static void drawEnvironment(Graphics2D graphics, Player player, int
  // width, int height,
  // int figureSize) {
  // var listCells = player.getEnvironment().getCells();

  // var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png",
  // "fox.png",
  // "salmon.png");

  // for (var cell : listCells) {
  // if (cell.isOccupiedByTile()) {
  // var habitatType = cell.getTile().firstHabitat();
  // var x = (int) (((cell.getCoordinates().x() + width / 2 / figureSize)) *
  // figureSize)
  // + figureSize / 2;
  // var y = (int) (((cell.getCoordinates().y() + height / 2 / figureSize)) *
  // figureSize)
  // + figureSize / 2;

  // switch (habitatType) {
  // case FOREST->drawSquare(graphics, x, y, figureSize, COLOR_FOREST);
  // case WETLAND->drawSquare(graphics, x, y, figureSize, COLOR_WETLAND);
  // case MOUNTAIN->drawSquare(graphics, x, y, figureSize, COLOR_MOUNTAIN);
  // case RIVER->drawSquare(graphics, x, y, figureSize, COLOR_RIVER);
  // case PRAIRIE -> drawSquare(graphics, x, y, figureSize, COLOR_PRAIRIE);
  // }

  // int placement = 0;
  // if (cell.getAnimal() == null) {
  // for (var animal :
  // cell.getTile().animals()) {

  // int scaledTokenSize = (int) (figureSize / 2);
  // var xAnimal = (int) ((x - (figureSize / 2) + figureSize / 20)
  // + placement * (figureSize / 2.5));
  // var yAnimal = (int) (y - (figureSize / 2) + figureSize / 4); // + (placement
  // *
  // // figureSize / 5)
  // placement++;

  // switch (animal) {
  // case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);}

  // }

  // }else {
  // var animal = cell.getAnimal();
  // int scaledTokenSize = (int) (figureSize / 1.5);
  // var xAnimal = (int) (x - figureSize / 3);
  // var yAnimal = (int) (y - figureSize / 3);

  // switch (animal) {
  // case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);
  // case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal,
  // scaledTokenSize, scaledTokenSize);}
  // ;}
  // // drawSquare(graphics, cell.getCoordinates().x(), cell.getCoordinates().y(),
  // // figureSize, Color.RED);
  // // System.out.println(cell.toString());
  // }
  // }
  // }

  //
  //
  //

  public void showGameBoard(Graphics2D graphics, int tokenSize) {
    final float RECTANGLE_X_OFFSET = 1.8f;
    final float ROW_MULTIPLIER = 1.8f;
    final float CIRCLE_X_OFFSET = 1.05f;
    var baseXOffset = (int) (figureSize / RECTANGLE_X_OFFSET);

    var listOfTiles = data.getGameBoard().getCopyOfTiles();
    var listOfTokens = data.getGameBoard().getCopyOfTokens();

    for (int i = 0; i < 4; i++) {
      int yPosition = (int) (figureSize * ROW_MULTIPLIER * (i + 1));
      drawTileAndAnimals(graphics, listOfTiles.get(i), baseXOffset, yPosition, figureSize);
      drawWildlifeToken(graphics, listOfTokens.get(i), tokenSize,
          (int) (baseXOffset + figureSize * CIRCLE_X_OFFSET + tokenSize / 5),
          (int) (yPosition + tokenSize / 5));
      if (i != 3) { // draw a line for separation
        drawLine(graphics, (int) (figureSize / 3), (int) (yPosition + figureSize * 1.4),
            (int) (figureSize / 3 + figureSize * 2.35), (int) (yPosition + figureSize * 1.4),
            Color.BLACK);
      }
    }
  }

  private void drawTileAndAnimals(Graphics2D graphics, Tile tile, int x,
      int y, int figureSize) {
    // Draw habitat rectangle
    switch (tile.firstHabitat()) {
    case FOREST->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_FOREST);
    case WETLAND->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_WETLAND);
    case MOUNTAIN->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_MOUNTAIN);
    case RIVER->drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_RIVER);
    case PRAIRIE -> drawRectangle(graphics, x, y, figureSize, figureSize, COLOR_PRAIRIE);
    }

    // Draw Animals
    int placement = 0;
    for (var animal :
    tile.animals()) {
      int scaled = figureSize / 2;
      int xAnimal = (int) (x + figureSize / 20.0 + placement * (figureSize / 2.5));
      int yAnimal = y + figureSize / 4;
      placement++;
      drawImage(graphics, loader.image(animal.ordinal()), xAnimal, yAnimal, scaled, scaled);
    }
  }

  private void drawWildlifeToken(Graphics2D graphics, WildlifeType token,
      int tokenSize, int circleX, int circleY) {
    int scaledTokenSize = (int) (tokenSize / 1.5);
    drawImage(graphics, loader.image(token.ordinal()), circleX, circleY, scaledTokenSize,
        scaledTokenSize);
  }

  // another class showGameBoard
  // private static void showGameBoard(Graphics2D graphics, GameBoard board, int
  // figureSize,
  // int tokenSize /* int width, int height, int margin */
  // ) {
  // final var RECTANGLE_X_OFFSET = 1.8f;
  // final var ROW_MULTIPLIER = 1.8f;
  // final var CIRCLE_X_OFFSET = 1.05f;

  // var baseXOffset = (int) (figureSize / RECTANGLE_X_OFFSET); // common X offset
  // // for both shapes

  // var listOfTiles = board.getCopyOfTiles();
  // var listOfTokens = board.getCopyOfTokens();

  // var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png",
  // "fox.png",
  // "salmon.png");

  // for (var i = 1; i <= 4; ++i) {
  // var yPosition = (int) (figureSize * ROW_MULTIPLIER * i);

  // var scaledTokenSize = (int) (tokenSize / 1.5);
  // // calculate circle position relative to rectangle
  // // var halfFigureSize = figureSize / 2f;
  // // var halfTokenSize = tokenSize / 2f;
  // // var circleX = (int) (baseXOffset + (figureSize * CIRCLE_X_OFFSET) +
  // // halfFigureSize); // centered X for circle
  // // var circleY = (int) (yPosition + halfFigureSize); // centered Y for circle
  // var circleX = (int) (baseXOffset + (figureSize * CIRCLE_X_OFFSET) + tokenSize
  // / 5); // centered
  // // X
  // // for
  // // circle
  // var circleY = (int) (yPosition + tokenSize / 5); // centered Y for circle

  // var tile = listOfTiles.get(i - 1);

  // // draw Habitat tile (rectangle)
  // switch (tile.firstHabitat()) {
  // case FOREST->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
  // figureSize,
  // COLOR_FOREST);
  // case WETLAND->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
  // figureSize,
  // COLOR_WETLAND);
  // case MOUNTAIN->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
  // figureSize,
  // COLOR_MOUNTAIN);
  // case RIVER->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
  // figureSize,
  // COLOR_RIVER);
  // case PRAIRIE -> drawRectangle(graphics, baseXOffset, yPosition, figureSize,
  // figureSize,
  // COLOR_PRAIRIE);
  // }

  // int placement = 0;
  // for (var animal :
  // tile.animals()) {
  // var scale = (int) (figureSize / 2);
  // var xAnimal = (int) ((baseXOffset + figureSize / 20) + placement *
  // (figureSize / 2.5));
  // var yAnimal = (int) (yPosition + figureSize / 4); // + (placement *
  // figureSize / 5)
  // placement++;

  // switch (animal) {
  // case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal, scale,
  // scale);
  // case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal, scale,
  // scale);
  // case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal, scale,
  // scale);
  // case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal, scale,
  // scale);
  // case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal,
  // yAnimal, scale,
  // scale);}
  // ;}

  // var token = listOfTokens.get(i - 1); // BEAR, ELK, SALMON, HAWK, FOX
  // // draw Wildlife token (circle)
  // // drawCircle(context, circleX, circleY, tokenSize, Color.RED);

  // // case BEAR -> drawCircle(context, circleX, circleY, tokenSize, Color.RED);
  // // case ELK -> drawCircle(context, circleX, circleY, tokenSize, Color.BLUE);
  // // case SALMON -> drawCircle(context, circleX, circleY, tokenSize,
  // Color.GREEN);
  // // case HAWK -> drawCircle(context, circleX, circleY, tokenSize,
  // Color.YELLOW);
  // // case FOX -> drawCircle(context, circleX, circleY, tokenSize, Color.CYAN);

  // switch (token) {
  // case BEAR->drawImage(graphics, images.image(token.ordinal()), circleX,
  // circleY,
  // scaledTokenSize, scaledTokenSize);
  // case ELK->drawImage(graphics, images.image(token.ordinal()), circleX,
  // circleY,
  // scaledTokenSize, scaledTokenSize);
  // case SALMON->drawImage(graphics, images.image(token.ordinal()), circleX,
  // circleY,
  // scaledTokenSize, scaledTokenSize);
  // case HAWK->drawImage(graphics, images.image(token.ordinal()), circleX,
  // circleY,
  // scaledTokenSize, scaledTokenSize);
  // case FOX -> drawImage(graphics, images.image(token.ordinal()), circleX,
  // circleY,
  // scaledTokenSize, scaledTokenSize);}
  // ;

  // // do not delete this code for now ! it's for internal use and see where the
  // // lines are
  // if (i != 4) {
  // drawLine(graphics, (int) (figureSize / 3), (int) (yPosition + figureSize *
  // 1.4), // y
  // // (int)
  // // (figureSize
  // // *
  // // 1.8f
  // // *
  // // i);
  // (int) (figureSize / 3 + figureSize * 2.35), (int) (yPosition + figureSize *
  // 1.4),
  // Color.BLACK);
  // }

  // /*
  // * Previos code used for draw game board // drawRectangle(context, // (int)
  // * (figureSize / 1.8), // (int) ((figureSize * 1.8 * i) + (figureSize * 1.8)),
  // * // figureSize, figureSize, COLOR_RIVER); // drawCircle(context, // (int)
  // * (figureSize / 1.8 + (figureSize * 1.05)) + (figureSize / 2), // (int)
  // * ((figureSize * 1.8 * i) + (figureSize * 1.8)) + (figureSize / 2), // (int)
  // * tokenSize, Color.RED);
  // */
  // }
  // }

  private void showScoringCard(Graphics2D graphics, int x, int y, int width, int height,
      int figureSize, boolean isFamilyScoringCard) {
    /*
     * drawRoundRectangle(context, width - (int) (figureSize * 2.35) - (int)
     * (figureSize / 3), // width figureSize, // height (int) (figureSize * 2.35),
     * environmentSquareHeight, figureSize, environmentSquareHeight / 10,
     * Color.MAGENTA);
     */

    drawRectangle(graphics, x, y + (height / 4), // x, y
        (int) (width), // width
        (int) (height / 2), // height
        // figureSize, height / 10,
        Color.RED);
  }

  public void showPossibleCoordinates(Graphics2D graphics) {
    var listOfCells = data.getCurrentPlayerCells();
    for (var cell : listOfCells) {
      if (cell == null) {
        throw new IllegalArgumentException("cell is null in showPossibleCoordinates()");
      }
      if (!cell.isOccupiedByTile()) {
        // var x = cell.getCoordinates().x() * figureSize + width / 2;
        // var y = cell.getCoordinates().y() * figureSize + height / 2;
        int x = (int) (((cell.getCoordinates().x() + width / 2 / figureSize)) * figureSize)
            + figureSize / 2;
        int y = (int) (((cell.getCoordinates().y() + height / 2 / figureSize)) * figureSize)
            + figureSize / 2;

        drawSquare(graphics, x, y, figureSize, Color.RED);
      }
    }
  }


  // to improve later this method
  private Coordinates getCoordinatesFromPointerEvent(PointerEvent pe, int width, int height) {
    switch (pe.action()) {
      case POINTER_UP -> {
        var location = pe.location();
        checkRange(0, location.x(), width);
        checkRange(0, location.y(), height);
        return new Coordinates(location.y(), location.x());
      }
      // case POINTER_DOWN, POINTER_MOVE -> {
      //   return null;
      // }
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

  /*
   * POINTER_DOWN: nothing to do, user clicked, but didn't release the button or
   * moved the mouse
   * POINTER_MOVE: user moves his mouse
   */
  public Coordinates getCoordinatesFromUser(ApplicationContext context, int width, int height) {
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



  public boolean handleTokenChange(
      ApplicationContext context, int fontSize) {
    if (data.tokensMustBeUpdated()) {
      data.updateTokens();
      context.renderFrame(graphics -> { showGameBoard(graphics, figureSize); });
      return true;
    } else if (data.tokensCouldBeUpdated()) { // done in CascadiaData.java
      context.renderFrame(graphics -> {
        restartRectangleWithText(graphics, "Click here to change",
          figureSize / 2, figureSize / 2, fontSize,
          (int) (figureSize * 3.5), figureSize * 10, figureSize * 8, Color.BLACK);
      });

      // to check coordinates from user
      var userCoordinates = getCoordinatesFromUser(context, width, height);
      if (userCoordinates == null) {
        System.err.println("Coordinates are invalid 1"); /* for tests, to delete later */
        return false;
      }

      // drawRectangle(context, (int) (figureSize / 5), 0,
      // (int) (figureSize * 3.3), (int) (figureSize * 0.8),
      // Color.BLACK);

      if (insideRectangle(userCoordinates.x(), userCoordinates.y(),
          (int) (figureSize / 5), 0, (int) (figureSize * 3.3), (int) (figureSize * 0.8))) {
        System.out.println("tokens are now updated");
        data.updateTokens();
      }

      context.renderFrame(graphics -> {
        drawRectangle(graphics, (int) (figureSize / 6), 0,
        (int) (figureSize * 3.5), (int) (figureSize * 0.9), Color.WHITE);
        showGameBoard(graphics, figureSize);
      });
      return true;
    }
    return false;
  }

  // private static int handleUserChoiceTileAndToken() {
  // int choice;
  // do {
  // choice = Integer
  // .parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile,
  // Token)\n"));
  // } while (!Constants.isValidChoice(choice));
  // return choice;
  // }


  private void showPossibleTokenPlacement(Graphics2D graphics, WildlifeType token, int tokenSize) {

    var listOfCells = data.getCurrentPlayerCells();

    for (var cell : listOfCells) {
      if (cell.isOccupiedByTile() && cell.couldBePlaced(token)) {
        var x = cell.getCoordinates().x();
        var y = cell.getCoordinates().y();
        // drawSquare(context,
        // (int) ((x + width / 2 / figureSize) * figureSize + (figureSize / 2)),
        // (int) ((y + height / 2 / figureSize) * figureSize + (figureSize / 2)),
        // figureSize,
        // Color.RED);
        drawCircle(graphics,
            (int) ((x + width / 2 / figureSize) * figureSize + (figureSize / 8.5)),
            (int) ((y + height / 2 / figureSize) * figureSize + (figureSize / 8.5)),
            (int) (tokenSize / 3),
            Color.WHITE);
      }
    }
  }


  public int selectGameBoardIndex(ApplicationContext context, int environmentSquareWidth, int tokenSize) {
    System.err.println("Before choosing tile and token");
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
   * @param context
   * @param player
   * @param chosenTile
   * @param width
   * @param height
   * @param figureSize
   * @return true if the tile was placed successfully, false otherwise
   */  
  public boolean handleTilePlacement(ApplicationContext context, Tile chosenTile) {
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

      System.err.println("Coordinates (TilePlacement) : (" +
                chosenCoordinates.x() + ", " + chosenCoordinates.y() + ")");  /* for tests, to delete later */

      if (data.placeTileIfPossible(chosenTile, chosenCoordinates)) {
        return true;
      }
    } while (true);
  }

  /**
   * Handles the user's choice of token.
   * @param context
   * @param player
   * @param chosenToken
   * @param width
   * @param height
   * @param figureSize
   * @param tokenSize
   * @return
   */
  public boolean handleTokenPlacement(ApplicationContext context, WildlifeType chosenToken, int tokenSize) {
    context.renderFrame(graphics -> {
      showPossibleTokenPlacement(graphics, chosenToken, tokenSize);
    });
    var userCoordinates = getCoordinatesFromUser(context, width, height);
    if (userCoordinates == null) {
      System.err.println("Coordinates are invalid WHEN PLACING TOKEN"); /* for tests, to delete later */
      return true;
    }
    System.err.println("Coordinates (TokenPlacement) : (" + userCoordinates.x() + "," + userCoordinates.y() + ")");
    var chosenCoordinates = new Coordinates(
        (int) ((userCoordinates.y() / figureSize) - height / 2 / figureSize),
        (int) ((userCoordinates.x() / figureSize) - width / 2 / figureSize));

    var tokenWasPlaced = data.placeAnimalIfPossible(chosenToken, chosenCoordinates);
    /* for tests, to delete later */
    System.err.println(tokenWasPlaced ? "Token was placed successfully" : "Token wasn't placed");
    return tokenWasPlaced;
  }

  private void drawString(Graphics2D graphics, String text, int x, int y, int fontSize, Color color) {
    graphics.setColor(color);
    // https://stackoverflow.com/questions/18249592/how-to-change-font-size-in-drawstring-java
    // graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
    graphics.setFont(new Font("Arial", Font.BOLD, fontSize));
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    graphics.drawString(text, x, y);
  }


  private String askName(ApplicationContext context) {
    var name = IO.readln("What is your name?");
    context.renderFrame(graphics -> {
      graphics.setColor(Color.WHITE);
      graphics.fill(new Rectangle2D.Float(0, 0, 800, 600));
    });
    return name;
  }


  private static boolean insideRectangle(int x, int y, int x1, int y1, int x2, int y2) {
    return x >= x1 && x <= x2 && 
           y >= y1 && y <= y2;
  }



  public int indexOfGameBoardCouple(int x, int y, int environmentSquareWidth) {
    for (var index = 0; index < 4; ++index) {
      int[] yOffsets = { figureSize,
        (int) (figureSize * 3.2f),
        (int) (figureSize * 5f),
        (int) (figureSize * 6.8f) };
        
        int[] heights = { (int) (figureSize * 2.2f),
          (int) (figureSize * 1.8f),
          (int) (figureSize * 1.8f),
          (int) (figureSize * 2.2f) };
          
      // to see their hitboxes
      // Color[] colors = { Color.GREEN, Color.BLUE, Color.YELLOW, Color.GREEN };
      // drawRectangle(context, figureSize / 3, yOffsets[index], (int)
      // (environmentSquareWidth / 5), heights[index], colors[index]);

      var x1 = figureSize / 3;
      var y1 = yOffsets[index];
      var x2 = x1 + (int) (environmentSquareWidth / 5);
      var y2 = y1 + heights[index];
      if (x >= x1 && x <= x2 &&
          y >= y1 && y <= y2) {
        return index;
      }
    }
    return -1;
  }


  public void restartEnvironmentRectangle(Graphics2D graphics, 
    int environmentSquareWidth, int environmentSquareHeight) {
    drawRoundRectangle(graphics,
        width / 2 - (environmentSquareWidth / 2), // center of the screen - x
        height / 2 - (environmentSquareHeight / 2), // center of the screen - y
        environmentSquareWidth,
        environmentSquareHeight,
        environmentSquareWidth / 10,
        environmentSquareHeight / 10,
        Color.PINK);
  }



  public void restartGameBoardRectangle(Graphics2D graphics, int environmentSquareHeight) {
    drawRoundRectangle(graphics,
        (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35), // to think about coefficient
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.PINK);
  }

  public void restartScoringCardRectangle(Graphics2D graphics, int environmentSquareHeight) {
    drawRoundRectangle(graphics,
        width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
        figureSize, // height
        (int) (figureSize * 2.35),
        environmentSquareHeight,
        figureSize,
        environmentSquareHeight / 10,
        Color.PINK);
  }


  public void restartRectangleWithText(
    Graphics2D graphics, String text,
    int x, int y, int fontSize, int width_rect,
    int environmentSquareWidth, int environmentSquareHeight, Color color) {
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



  /////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////
  //////////////////::::::: code added from GraphicSquare.java :::::://////////////////
  /////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////

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
   * Gets base coordinate for the given row /column.
   *
   * @param index  Index of the row / column.
   * @param origin Base coordinate of the display area.
   * @return Base coordinate of the row / column.
   */
  private float realCoordFromIndex(int index, int origin) { return origin + index * figureSize; }

  /**
   * Gets base abscissa for the column of index i.
   *
   * @param i Column index.
   * @return Base abscissa of the column.
   */
  private float xFromI(int i) { return realCoordFromIndex(i, xOrigin); }

  /**
   * Gets base ordinate for the row of index j.
   *
   * @param j Row index.
   * @return Base ordinate of the row.
   */
  private float yFromJ(int j) { return realCoordFromIndex(j, yOrigin); }

  /**
   * Displays an image in a given part of the display area.
   *
   * @param graphics Graphics engine that will actually display the image.
   * @param image    Image to be displayed.
   * @param x        Base abscissa of the part in which the image will be
   *                 displayed.
   * @param y        Base ordinate of the part in which the image will be
   *                 displayed.
   * @param dimX     Width of the part in which the image will be displayed.
   * @param dimY     Height of the part in which the image will be displayed.
   */
  // private void drawImage(Graphics2D graphics, BufferedImage image, float x,
  // float y, float dimX, float dimY) {
  // var width = image.getWidth();
  // var height = image.getHeight();
  // var scale = Math.min(dimX / width, dimY / height);
  // var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale *
  // width) / 2,
  // y + (dimY - scale * height) / 2);
  // graphics.drawImage(image, transform, null);
  // }

  /**
   * Displays the content of the cell in column i and row j.
   *
   * @param graphics Graphics engine that will actually display the cell.
   * @param data     GameData containing the game data.
   * @param i        Column of the cell.
   * @param j        Row of the cell.
   */
  private void drawCell(Graphics2D graphics, CascadiaData data, int i, int j) {
    // var x = xFromI(i);
    // var y = yFromJ(j);
    // var image = data.visible(i, j) ? loader.image(data.id(i, j)) :
    // loader.blank();
    // drawImage(graphics, image, x + 2, y + 2, squareSize - 4, squareSize - 4);
  }

  /**
   * Draws the game board from its data, using an existing Graphics2D object.
   *
   * @param graphics Graphics engine that will actually display the game.
   * @param data     GameData containing the game data.
   */
  private void draw(Graphics2D graphics, CascadiaData data) {
    // example
    // graphics.setColor(Color.WHITE);
    // graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, height, width));

    // for (int i = 0; i < data.lines(); i++) {
    // for (int j = 0; j < data.columns(); j++) {
    // drawCell(graphics, data, i, j);
    // }
    // }
  }

  /**
   * Draws the game board from its data, using an existing
   * {@code ApplicationContext}.
   *
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data.
   * @param view    GameView on which to draw.
   */
  public static void draw(ApplicationContext context, CascadiaData data, CascadiaView view) {
    context.renderFrame(graphics -> view.draw(graphics, data)); /* do not modify */
  }

}
