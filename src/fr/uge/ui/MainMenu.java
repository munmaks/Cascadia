package fr.uge.ui;

import fr.uge.core.Game;
import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.util.Constants;
import java.io.IO;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;



public final class MainMenu {
  /*
   * Main menu contains, buttoms: Play and Settings (to choose number of players and Wildlife Scoring Cards)
   * 
   * */
  public MainMenu(int version) {
    if (!Constants.isValidVersion(version)) {
      throw new IllegalArgumentException(Constants.IllegalVersion);
    }
    if (version == Constants.VERSION_SQUARE){
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
    System.out.println("Here is " + player.name() + "'s environment: ");
    var listCells = player.environment().getCells();
    for (var cell : listCells){
      System.out.println(cell.toString());
    }
  }


  private void showGameBoard(GameBoard board){
    Objects.requireNonNull(board);
    System.out.println("Here is the game board: ");
    var tiles = board.getCopyOfTiles();
    var tokens = board.getCopyOfTokens();
    for (var i = 0; i < tiles.length; ++i){
      System.out.println((i + 1) + ") " + tiles[i].toString() + " and " + tokens[i].toString());
    }
    System.out.println("");
  }

  private void showPossibleCoordinates(Player player){
    Objects.requireNonNull(player);
    System.out.println("Here are the possible coordinates with next format: \nempty tile coordinates - neighbor");
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
    var setOfCells = player.environment().getPossibleCells();
    for (var cell : setOfCells){
      if (cell.getTile() instanceof HabitatTile){
        System.out.println(cell.getTile().canBePlaced(token));
      }
    }
  }


  // under test
  private void gameLoop(Game game){
    Objects.requireNonNull(game);
    int i = 0;
    while (!game.turnManager().isGameEnd() && i < 2) {
      i++;
      var currentPlayer = game.turnManager().getCurrentPlayer();
      System.out.println("It's " + currentPlayer.name() + "'s turn!");
      showEnvironment(currentPlayer);
      showPossibleCoordinates(currentPlayer);
      showGameBoard(game.board());
      // show (habitat neighrbors) : all coordiantes
      // like this:
      // (WETLAND, FOREST) : (1, 1), (2, 2)     because Wetland in (1, 2) and Forest in (2, 1)
      // (MOUNTAIN) : (5, 4), (4, 5), (6, 5), (5, 6)  because Mountain in (5, 5)
      // You can choose where to place the tile

      int choice = Integer.parseInt(IO.readln("Please choose from 1 to 4 to take a couple: (Habitat Tile, Wildlife Token)\n"));
      if (!Constants.isValidChoice(choice)) {
        throw new IllegalArgumentException("Invalid choice");
      }
      var chosedTile = game.board().getCopyOfTiles()[choice - 1];
      var coordiantesString = IO.readln("Please choose the coordinates to place the tile (format: \"x, y\"): ");
      Scanner s = new Scanner(coordiantesString).useDelimiter(",\\s*");
      int x = s.nextInt();
      int y = s.nextInt();

      var chosedToken = game.board().getCopyOfTokens()[choice - 1];
      System.out.println("You have chosen " + chosedTile.toString() + " to place in (" + x + ", " + y + ")");
      System.out.println("Now you need to place the wildlife token: " + chosedToken.toString());
      showPossibleTokenPlacement(currentPlayer, chosedToken);

      // " and " + chosedToken.toString() + 
      // System.out.println("to continue press c");
      // System.out.println("q. Quit the game");
    }
  }


  private void playSquareTerminal(){
    System.out.println("Welcome to the Cascadia game (terminal version)!");
    System.out.println("We have two players, please introduce yourselves.");
    String firstPlayerName = readName(1);
    String secondPlayerName = readName(2);
    int version = Constants.VERSION_SQUARE;
    int familyOrIntermediate = chooseVersion();
    System.out.println("You have chosen " + (familyOrIntermediate == 1 ? "Family" : "Intermediate") + " Scoring Card");

    System.out.println("The game is starting!");
    var player1 = new Player(firstPlayerName, version);
    var player2 = new Player(secondPlayerName, version);
    
    var listOfPlayers = List.of(player1, player2);
    var board = new GameBoard(Constants.NB_PLAYERS_SQUARE, version);
    var turnManager = new TurnManager(listOfPlayers, version);
    Game game = new Game(listOfPlayers, board, turnManager, version);
    gameLoop(game);
  }






}
