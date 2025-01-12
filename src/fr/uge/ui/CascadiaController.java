package fr.uge.ui;


import java.util.List;

import com.github.forax.zen.ApplicationContext;

import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.environment.Coordinates;
import fr.uge.util.Constants;

import java.awt.Color;

// import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
// import com.github.forax.zen.KeyboardEvent;
// import com.github.forax.zen.PointerEvent;

public class CascadiaController {

  public CascadiaController() {
    // TO DO
  }

  public static void cascadiaSquareGame(ApplicationContext context) {
    var screenInfo = context.getScreenInfo();
    var width = screenInfo.width();
    var height = screenInfo.height();

    /* makeCalculations(width, height); */
    var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
    var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */
    var coeffSizeToken = 0.08; /* to test */

    var coeffFontSizeInstructions = 0.01328125;

    var figureSize = (int) (coeffSizeSquare * Math.min(width, height));
    var tokenSize = (int) (coeffSizeToken * Math.min(width, height));

    var environmentSquareWidth = width - (figureSize * 6);
    var environmentSquareHeight = height - (figureSize * 2);

    System.out.println("size of the screen (" + width + " x " + height + ")");

    // var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png",
    // "fox.png", "salmon.png");

    var firstPlayerName = "Alice"; // to be changed later
    var secondPlayerName = "Bob"; // to be changed later
    var familyOrIntermediate = 1; // to be changed later

    var player1 = new Player(firstPlayerName, Constants.VERSION_SQUARE);
    var player2 = new Player(secondPlayerName, Constants.VERSION_SQUARE);

    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, Constants.VERSION_SQUARE);
    var turnManager = new TurnManager(listOfPlayers.size(), Constants.VERSION_SQUARE);

    var data = new CascadiaData(board, turnManager, listOfPlayers, Constants.VERSION_SQUARE);

    var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png", "fox.png",
        "salmon.png");

    var view = CascadiaView.initGameGraphics(0, 0, width, height, figureSize, data, images);
    // gameLoopVersionSquare(context, game, width, height, figureSize, tokenSize);
    // calculateAndShowScore(game, familyOrIntermediate);

    context.renderFrame(graphics -> {
      view.drawRectangle(graphics, 0, 0, width, height, Color.WHITE);
      view.showGameBoard(graphics, tokenSize);
      // view.drawSquares(graphics, width, height, figureSize);
    });

    // ask players for their names
    // drawRoundRectangle(context,
    // width / 2 - (environmentSquareWidth / 4), // center of the screen - x
    // height / 2 - (environmentSquareHeight / 4), // center of the screen - y
    // environmentSquareWidth / 2,
    // environmentSquareHeight / 2,
    // environmentSquareWidth / 15,
    // environmentSquareHeight / 10,
    // Color.RED);
    // drawString(context, "Welcome to the Cascadia game!", width / 2 - 150, height
    // / 2, 20, Color.WHITE);

    // drawRoundRectangle(context,
    // width / 2 + (environmentSquareWidth / 4), // center of the screen - x
    // height / 2 - (environmentSquareHeight / 4), // center of the screen - y
    // environmentSquareWidth / 4,
    // environmentSquareHeight / 2,
    // environmentSquareWidth / 15,
    // environmentSquareHeight / 10,
    // Color.MAGENTA);
    // drawString(context, "Validate", (int) (width / 2 + (environmentSquareWidth /
    // 3)), height / 2, 20, Color.WHITE);

    var x1 = width / 2 + (environmentSquareWidth / 4);
    var y1 = height / 2 - (environmentSquareHeight / 4);
    var x2 = x1 + environmentSquareWidth / 4;
    var y2 = y1 + environmentSquareHeight / 2;

    var x1_board = (int) (figureSize / 3);
    var y1_board = figureSize;
    var x2_board = x1_board + (int) (figureSize * 2.35);
    var y2_board = y1_board + environmentSquareHeight;

    // showScoringCard(graphics,
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

    // var area = new RectangleSquare();
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

    for (;;) {

      // drawRectangle(graphics, 0, 0, width, height, Color.WHITE); /* clear the
      // screen */
      var currentPlayer = data.getCurrentPlayer();

      context.renderFrame(graphics -> {
        view.restartEnvironmentRectangle(graphics, environmentSquareWidth, environmentSquareHeight);
        view.restartGameBoardRectangle(graphics, environmentSquareHeight);
        view.restartScoringCardRectangle(graphics, environmentSquareHeight);
        view.drawEnvironment(graphics);
        view.showGameBoard(graphics, figureSize);

        view.restartRectangleWithText(graphics, currentPlayer.getName() + ", Game Board",
            (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
            (int) (width * coeffFontSizeInstructions), width, environmentSquareWidth,
            environmentSquareHeight, Color.BLACK);

      });

      // System.err.println("before handleTokenChange");
      /* if we need to update tokens */
      if (view.handleTokenChange(context, (int) (width * coeffFontSizeInstructions))) {
        System.err.println("Tokens have been changed"); /* for tests, to delete later */
        context.renderFrame(graphics -> {
          view.restartRectangleWithText(graphics, "Tokens have been changed",
              (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
              (int) (width * coeffFontSizeInstructions), width, environmentSquareWidth,
              environmentSquareHeight, Color.BLACK);
        });
      }
      // System.err.println("after handleTokenChange");

      // while (true) {

      int choice = view.selectGameBoardIndex(context, environmentSquareWidth, tokenSize);

      if (choice == -2) {
        System.err.println("Coordinates are invalid"); /* for tests, to delete later */
        context.dispose();
        return;
      }
      // Coordinates coordinates = null;
      // System.err.println("before choosing tile and token");
      // for (;;) {
      // coordinates = view.getCoordinatesFromUser(context, width, height);
      // if (coordinates == null) {
      // System.err.println("Coordinates are invalid"); /* for tests, to delete later
      // */
      // context.dispose();
      // return;
      // }
      // choice = view.indexOfGameBoardCouple(coordinates.x(), coordinates.y(),
      // environmentSquareWidth);
      // if (choice != -1) {
      // break;
      // }
      // }
      /*
       * !insideRectangle(coordinates.x(), coordinates.y(), x1_board, y1_board,
       * x2_board, y2_board)
       */
      // System.out.println("You are inside game board rectangle");
      // sleep(1000);
      // if (true){
      // System.err.println("Coordinates are invalid"); /* for tests, to delete later
      // */
      // context.dispose();
      // return;
      // }

      var chosenTile = data.getTileByChoice(choice); // done in CascadiaData.java

      context.renderFrame(graphics -> {
        view.showPossibleCoordinates(graphics);
        view.restartRectangleWithText(graphics, "Place a tile",
            (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
            (int) (width * coeffFontSizeInstructions), width, environmentSquareWidth,
            environmentSquareHeight, Color.BLACK);
      });

      // boolean flag;
      // do {
      // flag = handleTilePlacement(context, currentPlayer, chosenTile, width, height,
      // figureSize);
      // } while (!flag);
      // view.showPossibleCoordinates(graphics, currentPlayer, width, height,
      // figureSize);

      view.handleTilePlacement(context, chosenTile);

      var chosenToken = data.getWildlifeTypeByChoice(choice); // done in CascadiaData.java

      /* refresh the screen */
      context.renderFrame(graphics -> {
        view.drawEnvironment(graphics);
        view.restartRectangleWithText(graphics, "Place " + chosenToken + " token",
            (int) (width - (environmentSquareWidth / 3.5)), figureSize / 2,
            (int) (width * coeffFontSizeInstructions), width, environmentSquareWidth,
            environmentSquareHeight, Color.BLACK);
      });

      // System.err.println("tile: " + chosenTile.toString() + " token: " +
      // chosenToken.toString()); // for tests, to delete later

      view.handleTokenPlacement(context, chosenToken, tokenSize);

      // handleTurnChange(game);
      data.handleTurnChange();
      System.out.println("Next player");
      if (data.isGameEnd()) {
        System.err.println("Game is over");
        context.dispose();
        return;
      }
      // area.draw(context, location.x(), location.y(), figureSize / 10, Color.BLACK);
    }
  }

  // public static void main(String[] args) {
  // Application.run(Color.WHITE, CascadiaController::cascadiaSquareGame);
  // }

}

// package fr.uge.ui;

// import java.awt.Color;

// import com.github.forax.zen.Application;
// import com.github.forax.zen.ApplicationContext;
// import com.github.forax.zen.KeyboardEvent;
// import com.github.forax.zen.PointerEvent;

// /**
// * The SimpleGameController class deals with the main game loop, including
// * retrieving raw user actions, sending them for analysis to the GameView and
// * GameData, and dealing with time events.
// *
// * @author vincent
// */
// public class SimpleGameController {
// /**
// * Default constructor, which does basically nothing.
// */
// public SimpleGameController() {

// }

// /**
// * Goes once in the game loop, which consists in retrieving user actions,
// * transmitting it to the GameView and GameData, and dealing with time events.
// *
// * @param context {@code ApplicationContext} of the game.
// * @param data GameData of the game.
// * @param view GameView of the game.
// * @return True if the game must continue, False if it must stop.
// */
// private static boolean gameLoop(ApplicationContext context, SimpleGameData
// data, SimpleGameView view) {
// var event = context.pollOrWaitEvent(10);
// switch (event) {
// case null:
// return true;
// case KeyboardEvent ke:
// return ke.key() != KeyboardEvent.Key.Q;
// case PointerEvent pe:
// if (pe.action() != PointerEvent.Action.POINTER_DOWN) {
// return true;
// }
// var location = pe.location();
// data.clickOnCell(view.columnFromX(location.x()),
// view.lineFromY(location.y()));
// SimpleGameView.draw(context, data, view);
// if (data.mustSleep()) {
// if (!sleep(1000)) {
// return false;
// }
// data.wakeUp();
// SimpleGameView.draw(context, data, view);
// }
// if (data.win()) {
// System.out.println("You have won!");
// sleep(1000);
// return false;
// }
// return true;
// }
// }

// /**
// * Tries sleeping for a given duration.
// *
// * @param duration Sleep duration, in milliseconds.
// * @return True if you managed to sleep that long, False otherwise.
// */
// private static boolean sleep(int duration) {
// try {
// Thread.sleep(duration);
// } catch (InterruptedException ex) {
// return false;
// }
// return true;
// }

// /**
// * Sets up the game, then launches the game loop.
// *
// * @param context {@code ApplicationContext} of the game.
// */
// private static void memoryGame(ApplicationContext context) {
// var screenInfo = context.getScreenInfo();
// var width = screenInfo.width();
// var height = screenInfo.height();
// var margin = 50;

// var images = new ImageLoader("data", "lego1.png", "lego2.png", "lego3.png",
// "lego4.png",
// "lego5.png", "lego6.png", "lego7.png", "lego8.png");
// var data = new CascadiaData();
// var view = CascadiaView.initGameGraphics(margin, margin,
// (int) Math.min(width, height) - 2 * margin, data, images);
// CascadiaView.draw(context, data, view);

// while (true) {
// if (!gameLoop(context, data, view)) {
// System.out.println("Thank you for quitting!");
// context.dispose();
// return;
// }
// }
// }

// /**
// * Executable program.
// *
// * @param args Spurious arguments.
// */
// public static void main(String[] args) {
// Application.run(Color.WHITE, SimpleGameController::memoryGame);
// }
// }