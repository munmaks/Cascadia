package fr.uge.ui;

import fr.uge.core.Game;
import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.environment.Coordinates;
import fr.uge.environment.HabitatTile;
import fr.uge.environment.Tile;
import fr.uge.environment.WildlifeToken;
import fr.uge.util.Constants;
import java.io.IO;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;



public final class MainMenu {
  /*
   * Main menu contains, buttoms: Play and Settings (to choose number of players and Wildlife Scoring Cards)
   * 
   * */
  private final int version;

  public MainMenu(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    this.version = version;
    if (this.version == Constants.VERSION_SQUARE){
      playSquareTerminal();
    }
  }


  private String readName(int num){
    return IO.readln("Player " + num + ", what's your name? ");
  }
  

  private int chooseVersion(){
    System.out.println("Choose scoring card: ");
    var choice = IO.readln("1 for Family\n2 for Intermediate \n");
    int familyOrIntermediate = Integer.parseInt(choice);
    if (familyOrIntermediate != 1 && familyOrIntermediate != 2) {
      throw new IllegalArgumentException("Invalid choice");
    }
    // System.out.println("You have chosen " + ( 1 == familyOrIntermediate ? "Family" : "Intermediate") + " Scoring Card");
    return familyOrIntermediate;
  }


  private void showEnvironment(Player player){
    Objects.requireNonNull(player);
    System.out.println("\n\nIt's " + player.name() + "'s turn!");
    System.out.println("\nHere is " + player.name() + "'s environment: ");
    var listCells = player.environment().getCells();
    if (listCells.isEmpty()){
      System.out.println("No cells in the environment");  /* for tests */
    }

    for (var cell : listCells){
      System.out.println(cell.toString());
    }
  }


  private void showGameBoard(GameBoard board){
    Objects.requireNonNull(board);
    System.out.println("\nHere is the game board: ");
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.length; ++i){
      System.out.println((i + 1) + ") " + tiles[i].toString() + " and " + tokens[i].toString());
    }
    System.out.println("");
  }

  private void showPossibleCoordinates(Player player){
    Objects.requireNonNull(player);
    System.out.println("Here are the possible coordinates:\n`empty (x, y)` - neighbor tile");
    var setOfCells = player.environment().getPossibleCells();
    for (var cell : setOfCells){
      // System.out.println(cell.coordinates().toString());
      player.environment().printAllNeighbors(cell);
    }
  }



  private void showPossibleTokenPlacement(Player player, WildlifeToken token){
    Objects.requireNonNull(player);
    Objects.requireNonNull(token);
    System.out.println("Here are the possible coordinates to place the token: ");
    var listOfCells = player.environment().getCells();
    for (var cell : listOfCells){
      if (cell.getTile() instanceof HabitatTile && cell.getTile().canBePlaced(token)){
        System.out.println( cell.toString() + " :: ");
      }
    }
  }

  /**
   * to do later
   */
  private void showScore(Game game){
    System.out.println("Game is over!\nThank you for playing!");

    // Objects.requireNonNull(game);
    // var players = game.players();
    // for (var player : players){
    //   System.out.println(player.name() + " has " + player.getScore() + " points");
    // }
  }


  private Coordinates getCoordinatesFromUser(String message){
    int x, y;
    try (Scanner s = new Scanner(message).useDelimiter(",\\s*")){
      x = s.nextInt();
      y = s.nextInt();
    }
    return new Coordinates(y, x);
  }


  private void handleTokenChnage(Game game){
    if (game.board().tokensNeedUpdate()){
      game.board().updateTokens();
      System.out.println("Tokens were updated, because one token had 4 occurences");
    }
    if (game.board().tokensCanBeUpdated()){
      // showPossibleChangeTokens(game);
      var stringTokensToChange = IO.readln("Enter `yes` if you want to change the tokens, otherwise press enter");
      try (Scanner s = new Scanner(stringTokensToChange).useDelimiter("\\s*")) {
          if (s.hasNext() && s.next().equals("yes")){
              game.board().updateTokens();
          }
      }
      showGameBoard(game.board());
    }

  }



  private int handleUserChoiceTileAndToken(){
    int choice = Integer.parseInt(IO.readln("Please choose from 1 to 4 to take a couple: (Tile, Token)\n"));
    while(!Constants.isValidChoice(choice)){
      choice = Integer.parseInt(IO.readln("Please choose ONLY from 1 to 4 to take a couple: (Tile, Token)\n"));
    }
    return choice;
  }


  private void handleTurnChange(Game game){
    if (game.turnManager().getNeedToTurn()){
      game.turnManager().nextTurn();
      System.out.println("Turns left: " + (Constants.MAX_GAME_TURNS - game.turnManager().getTotalTurns()));
    }
    game.turnManager().changePlayer();
  }


  private void handleTokenPlacement(Player player, WildlifeToken chosedToken){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedToken);

    /* chosed token from `choice` */
    System.out.println("Now you need to place the wildlife token: " + chosedToken.toString());
    showPossibleTokenPlacement(player, chosedToken);
    
    var userCoordinatesString = IO.readln("Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");

    var userCoordinates = getCoordinatesFromUser(userCoordinatesString);
    var currCell = player.environment().getCell(userCoordinates.y(), userCoordinates.x());
    var tokenWasPlaced = player.environment().placeWildlifeToken(currCell, chosedToken);

    if (!tokenWasPlaced){
      System.err.println("Token wasn't placed");   // need to test this
    }
  }


  private void handleTilePlacement(Player player, Tile chosedTile){
    Objects.requireNonNull(player);
    Objects.requireNonNull(chosedTile);
    var userCoordinatesString = IO.readln("Give me coordinates of tile, that you want to place the token on (format: \"x, y\"): ");

    var userCoordinates = getCoordinatesFromUser(userCoordinatesString);
    var currCell = player.environment().getCell(userCoordinates.y(), userCoordinates.x());
    var tileWasPlaced = player.environment().placeTile(currCell, chosedTile);

    if (!tileWasPlaced){
      System.err.println("Tile wasn't placed / can't be placed");   // need to test this
    }

  }


  private void showPlayerEnvironmentAndGameBoard(Player player, GameBoard board){
    showEnvironment(player);
    showPossibleCoordinates(player);
    showGameBoard(board);
  }




  private List<Integer> calculateListOfScores(Game game){
    Objects.requireNonNull(game);
    var listOfPlayerScores = new ArrayList<Integer>();
    for (var i = 0; i < game.getPlayerCount(); ++i){
      var player = game.turnManager().getPlayerByIndex(i);
      var score = player.calculateScore();
      listOfPlayerScores.add(score);
    }
    return listOfPlayerScores;
  }


  private void calculateAndShowScore(Game game){
    var listOfPlayerScores = calculateListOfScores(game);
    
    // showPlayersScores(listOfPlayerScores);
    // showScore(game);
  }


  // under test
  private void gameLoopVersionSquare(Game game){
    Objects.requireNonNull(game);
    while (!game.turnManager().isGameEnd()) {
      var currentPlayer = game.turnManager().getCurrentPlayer();
      showPlayerEnvironmentAndGameBoard(currentPlayer, game.board());

      handleTokenChnage(game);  /* if we need to update tokens */

      int choice = handleUserChoiceTileAndToken();
      var chosedTile = game.board().getTile(choice - 1);
      var chosedToken = game.board().getToken(choice - 1);

      handleTilePlacement(currentPlayer, chosedTile);
      handleTokenPlacement(currentPlayer, chosedToken);

      // IO.readln("STOP before the next turn");
      handleTurnChange(game);
    }
    calculateAndShowScore(game);
  }


  private void playSquareTerminal(){
    System.out.println("Welcome to the Cascadia game (terminal version)!");
    System.out.println("We have two players, please introduce yourselves.");
    String firstPlayerName = readName(1);
    String secondPlayerName = readName(2);
    int familyOrIntermediate = chooseVersion();
    System.out.println("You have chosen " + (familyOrIntermediate == 1 ? "Family" : "Intermediate") + " Scoring Card");

    System.out.println("The game is starting!");
    var player1 = new Player(firstPlayerName, this.version);
    var player2 = new Player(secondPlayerName, this.version);
    
    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, this.version);
    var turnManager = new TurnManager(listOfPlayers, this.version);
    Game game = new Game(board, turnManager, listOfPlayers.size(), this.version);
    gameLoopVersionSquare(game);
  }






}
