package fr.uge.ui;


import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints; // to think if we need it
import java.awt.Graphics2D;

import java.util.Objects;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Action;

import fr.uge.core.GameBoard;
import fr.uge.core.Player;

import com.github.forax.zen.PointerEvent;

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
 * @param squareSize Side of the (square) areas displaying individual cells /
 *                   images.
 * @param loader     ImageLoader that dealt with loading the pictures to be
 *                   memorized.
 */
public record CascadiaView(int xOrigin, int yOrigin, int height, int width, int squareSize,
    ImageLoader loader) {

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
  public static CascadiaView initGameGraphics(int xOrigin, int yOrigin, int length,
      CascadiaData data, ImageLoader loader) {
    // Objects.requireNonNull(data);
    // Objects.requireNonNull(loader);
    // var squareSize = length / data.lines();
    // return new CascadiaView(
    // xOrigin, yOrigin,
    // length, data.columns() * squareSize,
    // squareSize, loader);
    return null;
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
  private static void drawSquare(Graphics2D graphics, float x, float y, int size, Color color) {
    var rectangle = new Rectangle2D.Float(0, 0, 0, 0);
    // hide the previous rectangle
    // graphics.setColor(Color.WHITE);
    // graphics.fill(rectangle);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    rectangle.setRect(x - size / 2, y - size / 2, size, size);
    graphics.fill(rectangle);
  }

  private static void drawRectangle(Graphics2D graphics, float x, float y, int width, int height,
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

  private static void drawCircle(Graphics2D graphics, float x, float y, int size, Color color) {
    var ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    // hide the previous rectangle
    graphics.setColor(Color.WHITE);
    graphics.fill(ellipse);

    // show a new rectangle at the position of the pointer
    graphics.setColor(color);
    ellipse.setFrame(x - size / 2, y - size / 2, size, size);
    graphics.fill(ellipse);

  }

  public static void drawLine(Graphics2D graphics, int x1, int y1, int x2, int y2, Color color) {
    graphics.setColor(color);
    graphics.drawLine(x1, y1, x2, y2);
    ;
  }

  private static void drawRoundRectangle(Graphics2D graphics, float x, float y, float width,
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

  private static void drawImage(Graphics2D graphics, BufferedImage image, float x, float y,
      float dimX, float dimY) {
    var width = image.getWidth();
    var height = image.getHeight();
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
        y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }

  private static void drawEnvironment(Graphics2D graphics, Player player, int width, int height,
      int figureSize) {
    var listCells = player.getEnvironment().getCells();
    // drawSquare(context, (int) (figureSize / 2), (int) (figureSize / 2),
    // figureSize, Color.BLACK);
    // drawSquare(context, (int) ((width / 2) + figureSize / 2), (int) ((height / 2)
    // + figureSize / 2), figureSize, Color.WHITE);
    // drawSquare(context, (int) (figureSize / 2) + figureSize, (int) (figureSize /
    // 2) + figureSize, figureSize, Color.YELLOW);
    // drawSquare(context, (int) (figureSize / 2), (int) (figureSize / 2) +
    // figureSize, figureSize, Color.CYAN);
    // drawSquare(context, (int) (figureSize / 2) + figureSize, (int) (figureSize /
    // 2), figureSize, Color.ORANGE);

    var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png", "fox.png",
        "salmon.png");

    for (var cell : listCells) {
      if (cell.isOccupiedByTile()) {
        var habitatType = cell.getTile().firstHabitat();
        int x = (int) (((cell.getCoordinates().x() + width / 2 / figureSize)) * figureSize)
            + figureSize / 2;
        int y = (int) (((cell.getCoordinates().y() + height / 2 / figureSize)) * figureSize)
            + figureSize / 2;

        switch (habitatType) {
        case FOREST->drawSquare(graphics, x, y, figureSize, COLOR_FOREST);
        case WETLAND->drawSquare(graphics, x, y, figureSize, COLOR_WETLAND);
        case MOUNTAIN->drawSquare(graphics, x, y, figureSize, COLOR_MOUNTAIN);
        case RIVER->drawSquare(graphics, x, y, figureSize, COLOR_RIVER);
        case PRAIRIE -> drawSquare(graphics, x, y, figureSize, COLOR_PRAIRIE);
        }

        int placement = 0;
        if (cell.getAnimal() == null) {
          for (var animal :
          cell.getTile().animals()) {

            int scaledTokenSize = (int) (figureSize / 2);
            var xAnimal = (int) ((x - (figureSize / 2) + figureSize / 20)
                + placement * (figureSize / 2.5));
            var yAnimal = (int) (y - (figureSize / 2) + figureSize / 4); // + (placement *
                                                                         // figureSize / 5)
            placement++;

            switch (animal) {
            case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                scaledTokenSize, scaledTokenSize);
            case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                scaledTokenSize, scaledTokenSize);
            case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                scaledTokenSize, scaledTokenSize);
            case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                scaledTokenSize, scaledTokenSize);
            case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                scaledTokenSize, scaledTokenSize);}

              }

              }else {
                var animal = cell.getAnimal();
                int scaledTokenSize = (int) (figureSize / 1.5);
                var xAnimal = (int) (x - figureSize / 3);
                var yAnimal = (int) (y - figureSize / 3); // + (placement * figureSize / 5)
                // placement++;

                switch (animal) {
                case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                    scaledTokenSize, scaledTokenSize);
                case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                    scaledTokenSize, scaledTokenSize);
                case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                    scaledTokenSize, scaledTokenSize);
                case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                    scaledTokenSize, scaledTokenSize);
                case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal,
                    scaledTokenSize, scaledTokenSize);}
                  ;}
                  // drawSquare(graphics, cell.getCoordinates().x(), cell.getCoordinates().y(),
                  // figureSize, Color.RED);
                  // System.out.println(cell.toString());
                  }

                  }

                  }

                  // comment
                  private static void showGameBoard(Graphics2D graphics, GameBoard board,
                      int figureSize, int tokenSize /* int width, int height, int margin */
                  ) {
                    final var RECTANGLE_X_OFFSET = 1.8f;
                    final var ROW_MULTIPLIER = 1.8f;
                    final var CIRCLE_X_OFFSET = 1.05f;

                    var baseXOffset = (int) (figureSize / RECTANGLE_X_OFFSET); // common X offset
                                                                               // for both shapes

                    var listOfTiles = board.getCopyOfTiles();
                    var listOfTokens = board.getCopyOfTokens();

                    var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png",
                        "fox.png", "salmon.png");

                    for (var i = 1; i <= 4; ++i) {
                      var yPosition = (int) (figureSize * ROW_MULTIPLIER * i);

                      var scaledTokenSize = (int) (tokenSize / 1.5);
                      // calculate circle position relative to rectangle
                      // var halfFigureSize = figureSize / 2f;
                      // var halfTokenSize = tokenSize / 2f;
                      // var circleX = (int) (baseXOffset + (figureSize * CIRCLE_X_OFFSET) +
                      // halfFigureSize); // centered X for circle
                      // var circleY = (int) (yPosition + halfFigureSize); // centered Y for circle
                      var circleX = (int) (baseXOffset + (figureSize * CIRCLE_X_OFFSET)
                          + tokenSize / 5); // centered X
                                            // for circle
                      var circleY = (int) (yPosition + tokenSize / 5); // centered Y for circle

                      var tile = listOfTiles.get(i - 1);

                      // draw Habitat tile (rectangle)
                      switch (tile.firstHabitat()) {
                      case FOREST->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
                          figureSize, COLOR_FOREST);
                      case WETLAND->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
                          figureSize, COLOR_WETLAND);
                      case MOUNTAIN->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
                          figureSize, COLOR_MOUNTAIN);
                      case RIVER->drawRectangle(graphics, baseXOffset, yPosition, figureSize,
                          figureSize, COLOR_RIVER);
                      case PRAIRIE -> drawRectangle(graphics, baseXOffset, yPosition, figureSize,
                          figureSize, COLOR_PRAIRIE);
      }

      int placement = 0;
      for (var animal :
      tile.animals()) {
        var scale = (int) (figureSize / 2);
        var xAnimal = (int) ((baseXOffset + figureSize / 20) + placement * (figureSize / 2.5));
        var yAnimal = (int) (yPosition + figureSize / 4); // + (placement * figureSize / 5)
        placement++;

        switch (animal) {
        case BEAR->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal, scale,
            scale);
        case ELK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal, scale,
            scale);
        case SALMON->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal, scale,
            scale);
        case HAWK->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal, scale,
            scale);
        case FOX->drawImage(graphics, images.image(animal.ordinal()), xAnimal, yAnimal, scale,
            scale);}
          ;}

          var token = listOfTokens.get(i - 1); // BEAR, ELK, SALMON, HAWK, FOX
          // draw Wildlife token (circle)
          // drawCircle(context, circleX, circleY, tokenSize, Color.RED);

          // case BEAR -> drawCircle(context, circleX, circleY, tokenSize, Color.RED);
          // case ELK -> drawCircle(context, circleX, circleY, tokenSize, Color.BLUE);
          // case SALMON -> drawCircle(context, circleX, circleY, tokenSize, Color.GREEN);
          // case HAWK -> drawCircle(context, circleX, circleY, tokenSize, Color.YELLOW);
          // case FOX -> drawCircle(context, circleX, circleY, tokenSize, Color.CYAN);

          switch (token) {
          case BEAR->drawImage(graphics, images.image(token.ordinal()), circleX, circleY,
              scaledTokenSize, scaledTokenSize);
          case ELK->drawImage(graphics, images.image(token.ordinal()), circleX, circleY,
              scaledTokenSize, scaledTokenSize);
          case SALMON->drawImage(graphics, images.image(token.ordinal()), circleX, circleY,
              scaledTokenSize, scaledTokenSize);
          case HAWK->drawImage(graphics, images.image(token.ordinal()), circleX, circleY,
              scaledTokenSize, scaledTokenSize);
          case FOX -> drawImage(graphics, images.image(token.ordinal()), circleX, circleY,
              scaledTokenSize, scaledTokenSize);}
            ;

            // do not delete this code for now ! it's for internal use and see where the
            // lines are
            if (i != 4) {
              drawLine(graphics, (int) (figureSize / 3), (int) (yPosition + figureSize * 1.4), // y
                                                                                               // (int)
                                                                                               // (figureSize
                                                                                               // *
                                                                                               // 1.8f
                                                                                               // *
                                                                                               // i);
                  (int) (figureSize / 3 + figureSize * 2.35), (int) (yPosition + figureSize * 1.4),
                  Color.BLACK);
            }

            /*
             * Previos code used for draw game board // drawRectangle(context, // (int)
             * (figureSize / 1.8), // (int) ((figureSize * 1.8 * i) + (figureSize * 1.8)),
             * // figureSize, figureSize, COLOR_RIVER); // drawCircle(context, // (int)
             * (figureSize / 1.8 + (figureSize * 1.05)) + (figureSize / 2), // (int)
             * ((figureSize * 1.8 * i) + (figureSize * 1.8)) + (figureSize / 2), // (int)
             * tokenSize, Color.RED);
             */
            }
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
    return (int) ((coord - origin) / squareSize);
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
  private float realCoordFromIndex(int index, int origin) { return origin + index * squareSize; }

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
