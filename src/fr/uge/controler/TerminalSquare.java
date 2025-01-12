package fr.uge.controler;


import fr.uge.data.core.*;
import fr.uge.data.environment.Coordinates;
import fr.uge.data.environment.Tile;
import fr.uge.data.environment.TileType;
import fr.uge.data.environment.WildlifeType;
import fr.uge.data.scoring.FamilyAndIntermediateScoringCards;
import fr.uge.data.util.Constants;

import java.io.IO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public final class TerminalSquare {

  // private final int numberOfPlayers;
  // private final boolean isIntermediateScoringCard;

  public TerminalSquare() {
    // throw new IllegalStateException("Utility class, cannot be instantiated");
  }

  private String readName(int num) { return IO.readln("Player " + num + ", what's your name? "); }

  /**
   * 1 for Family, 2 for Intermediate Still need to improve it
   */
  private int chooseVersion() {
    System.out.println("Choose scoring card: ");
    var choice = IO.readln("1 for Family\n2 for Intermediate \n");
    int familyOrIntermediate = Integer.parseInt(choice);
    if (familyOrIntermediate != 1 && familyOrIntermediate != 2) {
      throw new IllegalArgumentException("Invalid choice");
    }
    return familyOrIntermediate;
  }

  private void showEnvironment(Player player) {
    Objects.requireNonNull(player);
    System.out.println("\n\nIt's " + player.getName() + "'s turn!");
    System.out.println("\nHere is " + player.getName() + "'s environment: ");
    var listCells = player.getEnvironment().getCells();

    // System.err.println("Size of list : " + listCells.size()); // for test, to
    // delete later
    for (var cell : listCells) {
      if (cell.isOccupiedByTile()) {
        System.out.println(cell.toString());
      }
    }
  }

  private void showGameBoard(GameBoard board) {
    Objects.requireNonNull(board);
    System.out.println("\nHere is the game board: ");
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.size(); ++i) {
      var builder = new StringBuilder();
      builder.append(i + 1).append(") ").append(tiles.get(i).toString()).append(" and ")
          .append(tokens.get(i).toString());
      System.out.println(builder.toString());
    }
    System.out.println("");
  }

  private void showPossibleCoordinates(Player player) {
    Objects.requireNonNull(player);
    System.out.println("\nHere are the possible coordinates:\n`empty (x, y)` - neighbor tile");
    var setOfCells = player.getEnvironment().getPossibleCells();
    for (var cell : setOfCells) {
      player.getEnvironment().printAllNeighbors(cell);
    }
  }

  private void showPossibleTokenPlacement(Player player, WildlifeType token) {
    Objects.requireNonNull(player, "player is null in showPossibleTokenPlacement()");
    Objects.requireNonNull(token, "token is null in showPossibleTokenPlacement()");

    System.out.println("Here are the possible coordinates to place the token: ");
    var listOfCells = player.getEnvironment().getCells();

    for (var cell : listOfCells) {
      if (cell == null) {
        throw new IllegalArgumentException("cell is null in showPossibleTokenPlacement()");
      }
      if (cell.couldBePlaced(token)) {
        System.out.println(cell.toString());
      }
    }
  }

  private Coordinates getCoordinatesFromUser(String message) {
    Objects.requireNonNull(message);
    int x, y;
    try (Scanner s = new Scanner(message).useDelimiter(",\\s*")) {
      x = s.nextInt();
      y = s.nextInt();
    }
    return new Coordinates(y, x);
  }

  // don't forget to check if already was changed and if so we don't change it
  // setToDefault in `src/fr/uge/core/TurnManager.java`
  /**
   * */
  private void handleTokenChange(Game game) {
    Objects.requireNonNull(game);
    if (game.board().tokensNeedUpdate()) {
      game.board().updateTokens();
      System.out.println("Tokens were updated, because one token had 4 occurences");
      showGameBoard(game.board());
    } else if (game.board().tokensCanBeUpdated()) {
      var stringTokensToChange = IO
          .readln("Enter `yes` if you want to change the tokens, otherwise press enter ");
      try (Scanner s = new Scanner(stringTokensToChange)) {
        if (s.hasNext() && "yes".equals(s.next())) {
          System.out.println("tokens are now updated");
          game.board().updateTokens();
        }
      }
      showGameBoard(game.board());
    }
  }

  private int handleUserChoiceTileAndToken() {
    int choice;
    do {
      choice = Integer
          .parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile, Token)\n"));
    } while (!Constants.isValidChoice(choice));
    return choice;
  }

  private void handleTurnChange(Game game) {
    Objects.requireNonNull(game);
    game.board().setDefaultTokensAreUpdated(); // that means, next person can change tokens (if
                                               // needed)
    game.turnManager().changePlayer();
    game.turnManager().nextTurn();
  }

  private void handleTokenPlacement(Player player, WildlifeType chosedToken) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedToken);

    /* chosed token from `choice` */
    System.out.println("Now you need to place the wildlife token: " + chosedToken.toString());
    showPossibleTokenPlacement(player, chosedToken);

    var userCoordinatesString = IO.readln(
        "Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");

    var userCoordinates = getCoordinatesFromUser(userCoordinatesString);
    var currCell = player.getEnvironment().getCell(userCoordinates);
    var tokenWasPlaced = player.getEnvironment().placeAnimal(currCell, chosedToken);

    if (!tokenWasPlaced) {
      System.err.println("Token wasn't placed"); /* for tests, to delete later */
    }
  }

  private void handleTilePlacement(Player player, Tile chosedTile) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedTile);
    var possibleCoordinates = player.getEnvironment().getPossibleCells();

    /* player has to place tile correctly */
    do {
      var userCoordinatesString = IO.readln(
          "Give me coordinates of cell, that you want to place the tile on (format: \"x, y\"): ");
      var userCoordinates = getCoordinatesFromUser(userCoordinatesString);

      if (possibleCoordinates.stream()
          .anyMatch(coordinates -> coordinates.equals(userCoordinates))) {
        var currCell = player.getEnvironment().getCell(userCoordinates);
        if (player.getEnvironment().placeTile(currCell, chosedTile)) {
          System.out.println("Tile was placed successfully"); // for test, to delete later
          break;
        }
      }
    } while (true);
  }

  private void showPlayerEnvironmentAndGameBoard(Player player, GameBoard board) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(board);
    showEnvironment(player);
    showPossibleCoordinates(player);
    showGameBoard(board);
  }

  private String showScoreTile(Map<TileType, Integer> scoreTile) {
    Objects.requireNonNull(scoreTile);
    var builder = new StringBuilder();
    for (var entry : scoreTile.entrySet()) {
      builder.append(entry.getKey().toString()).append(": ").append(entry.getValue())
          .append(" pts\n");
    }
    return builder.toString();
  }

  private void showTokensMap(Player player, FamilyAndIntermediateScoringCards card) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(card);
    var values = WildlifeType.values();
    for (var value : values) {
      var map = card.getWildlifeTokenMap(player.getEnvironment(), value);
      for (var entry : map.entrySet()) {
        System.out.println("For " + value.toString() + ": " + entry.getKey().toString() + ": "
            + card.getFamilyAndIntermediateGroupSizeToPoints(entry.getKey()) * entry.getValue()
            + " points");
      }
    }
  }

  private void showPlayerScore(Player player, FamilyAndIntermediateScoringCards card) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(card);
    var score = card.getScore(player.getEnvironment());

    System.out.println("\nPlayer " + player.getName() + " here is your details.\n");
    var scoreTile = player.getEnvironment().calculateTileScore();
    System.out.println("Based on the scoring card: \n" + showScoreTile(scoreTile) + "\n");

    showTokensMap(player, card);
    System.out.println(
        "\n" + player.getName() + " your final score: " + (score + player.calculateScore()));
  }

  private void calculateAndShowScore(Game game, int familyOrIntermediate) {
    Objects.requireNonNull(game);
    var scoringCard = new FamilyAndIntermediateScoringCards(familyOrIntermediate);

    for (var i = 0; i < game.getPlayerCount(); ++i) {
      showPlayerScore(game.getPlayerByIndex(i), scoringCard);
    }
  }

  // under test
  private void gameLoopVersionSquare(Game game) {
    Objects.requireNonNull(game);
    while (!game.turnManager().isGameEnd()) {
      var currIndex = game.turnManager().getCurrentPlayerIndex();
      var currentPlayer = game.getPlayerByIndex(currIndex);
      showPlayerEnvironmentAndGameBoard(currentPlayer, game.board());

      handleTokenChange(game); /* if we need to update tokens */

      int choice = handleUserChoiceTileAndToken();
      var chosedTile = game.board().getTile(choice - 1);
      var chosedToken = game.board().getToken(choice - 1);

      handleTilePlacement(currentPlayer, chosedTile);
      handleTokenPlacement(currentPlayer, chosedToken);

      // IO.readln("STOP before the next turn");
      handleTurnChange(game);
    }

  }

  public void playSquareTerminal() {
    System.out.println("Welcome to the Cascadia game (terminal version)!");
    System.out.println("We have two players, please introduce yourselves.\n");
    var firstPlayerName = readName(1);
    var secondPlayerName = readName(2);
    var familyOrIntermediate = chooseVersion();
    System.out.println("You have chosen " + (familyOrIntermediate == 1 ? "Family" : "Intermediate")
        + " Scoring Card");

    System.out.println("The game is starting!");
    var player1 = new Player(firstPlayerName, Constants.VERSION_SQUARE);
    var player2 = new Player(secondPlayerName, Constants.VERSION_SQUARE);

    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, Constants.VERSION_SQUARE);
    var turnManager = new TurnManager(listOfPlayers.size());
    Game game = new Game(board, turnManager, listOfPlayers, Constants.VERSION_SQUARE);
    gameLoopVersionSquare(game);
    calculateAndShowScore(game, familyOrIntermediate);
  }

}
