package fr.uge.ui;

public class CascadiaController {

  public CascadiaController() {
    // TO DO
  }
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

// var images = new ImageLoader("data", "blank.png", "lego1.png", "lego2.png",
// "lego3.png", "lego4.png", "lego5.png",
// "lego6.png", "lego7.png", "lego8.png");
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

// /**
// * Executable program.
// *
// * @param args Spurious arguments.
// */
// public static void main(String[] args) {
// Application.run(Color.WHITE, SimpleGameController::memoryGame);
// }
// }