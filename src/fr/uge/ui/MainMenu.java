package fr.uge.ui;

import fr.uge.core.Game;
import fr.uge.core.GameBoard;
import fr.uge.core.Player;
import fr.uge.core.TurnManager;
import fr.uge.util.Constants;
import java.io.IO;
import java.util.List;
import java.util.Objects;




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


  // under test
  private void gameLoop(Game game){
    Objects.requireNonNull(game);
    int i = 0;
    while (!TurnManager.isGameEnd() && i < 5) {
      i++;
      var currentPlayer = game.turnManager().getCurrentPlayer();
      System.out.println("It's " + currentPlayer.name() + "'s turn!");
      showEnvironment(currentPlayer);
      
      // show (habitat neighrbors) : all coordiantes
      // like this:
      // (WETLAND, FOREST) : (1, 1), (2, 2)     because Wetland in (1, 2) and Forest in (2, 1)
      // (MOUNTAIN) : (5, 4), (4, 5), (6, 5), (5, 6)  because Mountain in (5, 5)
      // You can choose where to place the tile

      System.out.println("What would you like to do?");
      System.out.println("1. Draw a tile and token");
      
      // System.out.println("3. Place a tile");
      // System.out.println("4. Place a token");
      // System.out.println("5. End turn");
      // var choice = IO.readln("Enter your choice: ");
      // int choiceInt = Integer.parseInt(choice);
      // switch (choiceInt) {
      //   case 1:
      //     currentPlayer.drawTile(game.board().drawTile());
      //     break;
      //   case 2:
      //     currentPlayer.drawToken(game.board().drawToken());
      //     break;
      //   case 3:
      //     currentPlayer.placeTile();
      //     break;
      //   case 4:
      //     currentPlayer.placeToken();
      //     break;
      //   case 5:
      //     game.turnManager().changePlayer();
      //     game.turnManager().nextTurn();
      //     break;
      //   default:
      //     System.out.println("Invalid choice");
      //     break;
      // }
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
