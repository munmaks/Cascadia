package fr.uge.ui;


import fr.uge.core.Player;
import fr.uge.core.GameBoard;


import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.RenderingHints; // to think if we need it
// import java.awt.Rectangle;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.PointerEvent;

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

  private int sizeOfTile;
  private int sizeOfToken;
  private int spaceBetweenTileAndToken;
  private int spaceBetweenCouples;
  private int sizeOfScoringCard;
  private int sizeOfPlayerBoard;  /* optional */


  private int heightShift = 0;  /* that's count when user wants to move his screen into left/right */
  private int widthShift = 0;   /* that's count when user wants to move his screen into up/down */

  private GraphicSquare() {
    throw new IllegalStateException("No instance of GraphicSquare");
  }


  private void makeCalculations(int width, int height) {
    var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
    var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */

    var coeffSizeToken = 0.05; /* to test */
    var coeffSizeScoringCard = 0.1; /* to test */
    var coeffSizePlayerBoard = 0.1; /* to test */

    this.sizeOfTile = (int) (coeffSizeSquare * Math.min(width, height));
    this.sizeOfToken = (int) (coeffSizeToken * Math.min(width, height));
    this.spaceBetweenTileAndToken = 10;  /* to determine later based on width and height */
    this.spaceBetweenCouples = 10; /* to determine later based on width and height */
    this.sizeOfScoringCard = 10; /* to determine later based on width and height */
    this.sizeOfPlayerBoard = 10; /* optional */
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
  private static void drawRectangle(ApplicationContext context, float x, float y, int width, int height, Color color) {

    var qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
    qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );

    // var rectangle = new Rectangle2D.Float(0, 0, 0, 0);
    var rectangle = new RoundRectangle2D.Float(x, y, width, height, 50, 50);
    // rectangle.setArcWidth(20);
    // rectangle.setArcHeight(20);
    
    context.renderFrame(graphics -> {
      graphics.setPaint(Color.ORANGE);
      graphics.setRenderingHints( qualityHints );         
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


  private void drawEnvironment(ApplicationContext context, Player player, int figureSize) {
    var listCells = player.getEnvironment().getCells();
    for (var cell : listCells){
      if (cell.isOccupiedByTile()){
        var habitatType = cell.getTile().firstHabitat();
        switch (habitatType){
          case FOREST -> drawSquare(context, cell.getCoordinates().x() * figureSize, cell.getCoordinates().y() * figureSize, figureSize, COLOR_FOREST);
          case WETLAND -> drawSquare(context, cell.getCoordinates().x() * figureSize, cell.getCoordinates().y() * figureSize, figureSize, COLOR_WETLAND);
          case MOUNTAIN -> drawSquare(context, cell.getCoordinates().x() * figureSize, cell.getCoordinates().y() * figureSize, figureSize, COLOR_MOUNTAIN);
          case RIVER -> drawSquare(context, cell.getCoordinates().x() * figureSize, cell.getCoordinates().y() * figureSize, figureSize, COLOR_RIVER);
          case PRAIRIE -> drawSquare(context, cell.getCoordinates().x() * figureSize, cell.getCoordinates().y() * figureSize, figureSize, COLOR_PRAIRIE);
        }
        // drawSquare(context, cell.getCoordinates().x(), cell.getCoordinates().y(), figureSize, Color.RED);
        // System.out.println(cell.toString());
      }
    }
  }



  // private void drawToken(ApplicationContext context, WildlifeType token, int tokenSize, float x, float y){

  // }



  private void showGameBoard(
      ApplicationContext context,
      GameBoard board,
      int figureSize,
      int spaceBetweenTileAndToken,
      int spaceBetweenCouples
    ) {
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.size(); ++i){

      var builder = new StringBuilder();
      builder.append(i + 1)
            .append(") ")
            .append(tiles.get(i).toString())
            .append(" and ")
            .append(tokens.get(i).toString());
      System.out.println(builder.toString());
    }
    System.out.println("");
  }



  // private static void memoryGame(ApplicationContext context) {
  //   var screenInfo = context.getScreenInfo();
  //   var width = screenInfo.width();
  //   var height = screenInfo.height();
  //   var margin = 50;

  //   var images = new ImageLoader("data", "blank.png",
  //     "lego1.png", "lego2.png", "lego3.png", "lego4.png",
  //     "lego5.png", "lego6.png", "lego7.png", "lego8.png");

  //   var data = new SimpleGameData(4, 4);
  //   var view = SimpleGameView.initGameGraphics(
  //     margin, margin,
  //     (int) Math.min(width, height) - 2 * margin,
  //     data, images);
  //   SimpleGameView.draw(context, data, view);
  //   while (true) {
  //     if (!gameLoop(context, data, view)) {
  //       System.out.println("Thank you for quitting!");
  //       context.dispose();
  //       return;
  //     }
  //   }
  // }


  private static void cascadiaSquareGame(ApplicationContext context) {
      var counter = 0;
      var screenInfo = context.getScreenInfo();
      var width = screenInfo.width();
      var height = screenInfo.height();

      // makeCalculations(width, height);
      var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
      var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */

      var figureSize = (int) (coeffSizeSquare * Math.min(width, height));

      var environmentSquareWidth = width - (figureSize * 6);
      var environmentSquareHeight = height - (figureSize * 2);

      System.out.println("size of the screen (" + width + " x " + height + ")");

      // not really usefull, can be removed later
      // context.renderFrame(graphics -> {
      //   graphics.setColor(Color.WHITE);
      //   graphics.fill(new Rectangle2D.Float(0, 0, width, height));
      // });

      // rectangle for player's environment
      drawRectangle(context,
                    width / 2 - (environmentSquareWidth / 2),
                    height / 2 - (environmentSquareHeight / 2),
                    environmentSquareWidth,
                    environmentSquareHeight,
                    Color.PINK);

      // game board
      drawRectangle(context,
                    (int) (figureSize / 3), // width
                    figureSize,             // height
                    (int) (figureSize * 2.35),  // to think about coefficient
                    environmentSquareHeight,
                    Color.ORANGE);

      // scoring cards:
      drawRectangle(context,
                    width - (int) (figureSize * 2.35) - (int) (figureSize / 3), // width
                    figureSize,             // height
                    (int) (figureSize * 2.35),
                    environmentSquareHeight,
                    Color.MAGENTA);
      // var countHeight = height / figureSize;
      // var countWidth = width / figureSize;

      // var color_forest = Color.GREEN; // new Color(46, 111, 64); // 39, 66, 26
      // var color_wetland = new Color(166, 185, 111);
      // var color_mountain = Color.GRAY; // new Color(105, 105, 105);
      // var color_river = Color.BLUE;
      // var color_prairie = Color.ORANGE;

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

      // var area = new Area();
      var area = new RectangleSquare();
      // var area = new HexagonalPolygon();
      // var area = new HexagonalPointyTop();

      while (true) {
        var event = context.pollOrWaitEvent(10);
        switch (event) {
          case null -> {
            continue;
          }
          case PointerEvent e -> {
            var location = e.location();
            checkRange(0, location.x(), width);
            checkRange(0, location.y(), height);
            // area.draw(context, location.x(), location.y(), (int) (coeffSizePolygone *
            // Math.min(width, height)));
            area.draw(context, location.x(), location.y(),
                figureSize,
                COLOR_PRAIRIE);
            break;
          }
          case KeyboardEvent e -> {
            switch (e.action()) { // KeyboardEvent.Key.Q
              case Action.KEY_RELEASED -> {
                counter++;
                if (counter == 1) {
                  context.dispose();
                  return;
                }
              }
              case Action.KEY_PRESSED -> {
                System.out.println(e.key());
              }
              default -> {
              }
            }
          }
        }
      }

  }

  public static void main(String[] args) {
    Application.run(Color.WHITE, GraphicSquare::cascadiaSquareGame);
  }

}