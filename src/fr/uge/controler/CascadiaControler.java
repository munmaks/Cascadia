package fr.uge.controler;


import java.util.List;

import com.github.forax.zen.ApplicationContext;

import fr.uge.data.CascadiaData;
import fr.uge.data.core.GameBoard;
import fr.uge.data.core.Player;
import fr.uge.data.core.TurnManager;
import fr.uge.view.CascadiaView;
import fr.uge.view.ImageLoader;

import com.github.forax.zen.ApplicationContext;
// import com.github.forax.zen.Application;
// import com.github.forax.zen.KeyboardEvent;
// import com.github.forax.zen.PointerEvent;

public class CascadiaControler {
  private static final int VERSION_SQUARE = 1;
  private static final int NB_PLAYERS_SQUARE = 2;

  public CascadiaControler() {
    // TO DO
  }

  public static void cascadiaSquareGame(ApplicationContext context) {
    var screenInfo = context.getScreenInfo();
    var width = screenInfo.width();
    var height = screenInfo.height();

    /* makeCalculations(width, height); */
    // var coeffSizePolygone = 0.069; /* optimal, to have 8 hexagonal on screen */
    var coeffSizeSquare = 0.1; /* optimal, to have 10 square on screen */
    var coeffSizeToken = 0.08; /* to test */

    var coeffFontSizeInstructions = 0.01328125;

    var figureSize = (int) (coeffSizeSquare * Math.min(width, height));
    var tokenSize = (int) (coeffSizeToken * Math.min(width, height));

    var environmentSquareWidth = width - (figureSize * 6);
    var environmentSquareHeight = height - (figureSize * 2);

    System.out.println("size of the screen (" + width + " x " + height + ")");

    var firstPlayerName = "Player 1";
    var secondPlayerName = "Player 2";
    // var familyOrIntermediate = 1;

    var player1 = new Player(firstPlayerName, VERSION_SQUARE);
    var player2 = new Player(secondPlayerName, VERSION_SQUARE);

    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(NB_PLAYERS_SQUARE, VERSION_SQUARE);
    var turnManager = new TurnManager(listOfPlayers.size());

    var data = new CascadiaData(board, turnManager, listOfPlayers, VERSION_SQUARE);

    var images = new ImageLoader("img/tokens", "bear.png", "elk.png", "hawk.png", "fox.png",
        "salmon.png");

    var view = CascadiaView.initGameGraphics(0, 0, width, height, figureSize, data, images);

    for (;;) {

      // drawRectangle(graphics, 0, 0, width, height, Color.WHITE); /* clear the
      // screen */

      CascadiaView.drawStart(context, data, view, width, height,
          (int) (width * coeffFontSizeInstructions), figureSize, environmentSquareWidth,
          environmentSquareHeight);

      /* if we need to update tokens */
      if (view.handleTokenChange(context, (int) (width * coeffFontSizeInstructions))) {
        view.drawInformation(context, data, view, "Tokens have been changed", width,
            (int) (width * coeffFontSizeInstructions), figureSize, environmentSquareWidth,
            environmentSquareHeight);
      }

      int choice = view.selectGameBoardIndex(context, environmentSquareWidth, tokenSize);

      if (choice == -2) {
        context.dispose();
        return;
      }

      var chosenTile = data.getTileByChoice(choice);

      context.renderFrame(graphics -> {
        view.showPossibleCoordinates(graphics);
      });

      view.drawInformation(context, data, view, "Place a tile", width,
          (int) (width * coeffFontSizeInstructions), figureSize, environmentSquareWidth,
          environmentSquareHeight);

      view.handleTilePlacement(context, chosenTile);

      var chosenToken = data.getWildlifeTypeByChoice(choice);

      /* refresh the screen */
      context.renderFrame(graphics -> {
        view.drawEnvironment(graphics);
      });

      view.drawInformation(context, data, view, "Place " + chosenToken + " token", width,
          (int) (width * coeffFontSizeInstructions), figureSize, environmentSquareWidth,
          environmentSquareHeight);

      view.handleTokenPlacement(context, chosenToken, tokenSize);

      data.handleTurnChange();
      if (data.isGameEnd()) {
        System.out.println("Game is over");
        context.dispose();
        return;
      }
    }
  }

}
